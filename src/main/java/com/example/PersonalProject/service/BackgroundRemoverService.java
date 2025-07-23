package com.example.PersonalProject.service;

import com.example.PersonalProject.utils.MultipartInputStreamFileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class BackgroundRemoverService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${bgremover.apiKey}")
    private String apiKey;

    private String api = "https://api.remove.bg/v1.0/removebg";

    public byte[] callRemoveBgApi(MultipartFile file) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("X-Api-Key", apiKey);

        // Prepare body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image_file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
        body.add("format", "png");
        body.add("crop", "false");

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Send request

        ResponseEntity<byte[]> response = restTemplate.exchange(
                api,
                HttpMethod.POST,
                requestEntity,
                byte[].class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new IOException("Remove.bg API failed: " + response.getStatusCode());
        }
    }

}
