package com.example.PersonalProject.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.PersonalProject.entity.Alloy;
import com.example.PersonalProject.repository.AlloyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class S3Service {

    private final AlloyRepository alloyRepository;
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    private final AmazonS3 s3Client;

    public S3Service(AmazonS3 s3Client, AlloyRepository alloyRepository) {
        this.s3Client = s3Client;
        this.alloyRepository = alloyRepository;
    }

    @Autowired
    private BackgroundRemoverService backgroundRemoverService;

    // --- UPLOAD METHOD ---
    public Map<String, String> uploadFile(MultipartFile file, Alloy alloy) throws IOException {

        byte[] processedImage = backgroundRemoverService.callRemoveBgApi(file);

        if (processedImage == null) {
            throw new IOException("Failed to process image via remove.bg");
        }

        // Generate a unique file name to avoid collisions in S3
        String key = UUID.randomUUID().toString() + ".png";

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(processedImage.length);
        metadata.setContentType("image/png");

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(processedImage);
            s3Client.putObject(new PutObjectRequest(bucketName, key, inputStream, metadata));

            String imageUrl = s3Client.getUrl(bucketName, key).toString();

            if(alloy.getImageUrl()!=null && alloy.getImageKey()!=null) {
                //s3Client.deleteObject(bucketName, alloy.getImageKey());
                deleteFile(alloy.getImageKey());
            }

            alloy.setImageKey(key);
            alloy.setImageUrl(imageUrl);
            alloyRepository.save(alloy);

            Map<String, String> map = new HashMap<>();
            map.put(key, imageUrl);
            return map;
        } catch (Exception e) {
            // If DB fails, clean up from S3
            s3Client.deleteObject(bucketName, key);
            throw e;  // rethrow to controller
        }
    }

    // --- RETRIEVE (Download) METHOD ---
    public byte[] downloadFile(String key) throws IOException {
        S3Object s3Object = s3Client.getObject(bucketName, key);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } finally {
            inputStream.close(); // Ensure the input stream is closed
        }
    }

    // --- DELETE METHOD (Optional) ---
    public void deleteFile(String key) {
        s3Client.deleteObject(bucketName, key);
    }
}