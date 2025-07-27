package com.example.PersonalProject.service;

import com.example.PersonalProject.dto.C1AlloyResponse;
import com.example.PersonalProject.dto.CAlloyResponse;
import com.example.PersonalProject.entity.Alloy;
import com.example.PersonalProject.repository.AlloyRepositoryImpl;
import com.example.PersonalProject.utils.AlloyMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private AlloyRepositoryImpl alloyRepositoryImpl;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<?> processAlloy(MultipartFile carImage, String alloyUrl) {
        try {
            // Step 1: Download alloy image
            byte[] alloyImageBytes = downloadImage(alloyUrl);
            if (alloyImageBytes == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to download alloy image.");
            }

            // Step 2: Prepare multipart body for FastAPI
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            // Car image
            ByteArrayResource carResource = new ByteArrayResource(carImage.getBytes()) {
                @Override
                public String getFilename() {
                    return carImage.getOriginalFilename();
                }
            };
            HttpHeaders carHeaders = new HttpHeaders();
            carHeaders.setContentType(MediaType.IMAGE_JPEG);
            HttpEntity<Resource> carEntity = new HttpEntity<>(carResource, carHeaders);
            body.add("car_image", carEntity);

            // Alloy image
            ByteArrayResource alloyResource = new ByteArrayResource(alloyImageBytes) {
                @Override
                public String getFilename() {
                    return "alloy.png";
                }
            };
            HttpHeaders alloyHeaders = new HttpHeaders();
            alloyHeaders.setContentType(MediaType.IMAGE_PNG);
            HttpEntity<Resource> alloyEntity = new HttpEntity<>(alloyResource, alloyHeaders);
            body.add("new_alloy", alloyEntity);

            // Step 3: Request FastAPI
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<byte[]> fastApiResponse = restTemplate.exchange(
                    "http://localhost:8000/process/",
                    HttpMethod.POST,
                    requestEntity,
                    byte[].class
            );

            if (fastApiResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(fastApiResponse.getBody());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("FastAPI processing failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Processing failed: " + e.getMessage());
        }
    }

    private byte[] downloadImage(String url) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = client.execute(request)) {
                return response.getEntity() != null ? EntityUtils.toByteArray(response.getEntity()) : null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<C1AlloyResponse> searchAlloys(ObjectId carModelId, double size, double width, String state, String city) {
        List<Alloy> list = alloyRepositoryImpl.searchAlloys(carModelId, size, width, state, city);

        List<C1AlloyResponse> responseList = AlloyMapper.C1toResponseList(list);
        return responseList;
    }

}
