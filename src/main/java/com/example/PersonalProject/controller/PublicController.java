package com.example.PersonalProject.controller;

import com.example.PersonalProject.dto.CarBrandResponse;
import com.example.PersonalProject.dto.CarResponse;
import com.example.PersonalProject.repository.car.CarBrandRepository;
import com.example.PersonalProject.repository.car.CarModelRepositoryImpl;
import com.example.PersonalProject.service.CarService;
import com.example.PersonalProject.service.S3Service;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private S3Service s3Service;

    @Autowired
    private CarModelRepositoryImpl carModelRepositoryImpl;

    @Autowired
    private CarBrandRepository carBrandRepository;

    @Autowired
    private CarService carService;

    @GetMapping("/download/{key}")
    public ResponseEntity<byte[]> downloadImage(@PathVariable String key) {
        try {
            byte[] data = s3Service.downloadFile(key);
            // Determine content type (you might want to store it during upload)
            // For simplicity, assuming image/jpeg or image/png
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Or MediaType.IMAGE_PNG, etc.
                    .body(data);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("Image not found or failed to download: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/carbrands")
    public ResponseEntity<List<CarBrandResponse>> getAllBrands() {
        return ResponseEntity.ok(carService.findCarBrands());
    }

    @GetMapping("/carmodels/{id}")
    public ResponseEntity<List<CarResponse>> getAllModels(@PathVariable ObjectId id) {
        return ResponseEntity.ok(carService.findModelsByCarId(id));
    }

}
