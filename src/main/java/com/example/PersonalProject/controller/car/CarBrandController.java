package com.example.PersonalProject.controller.car;

import com.example.PersonalProject.entity.CarBrand;
import com.example.PersonalProject.repository.car.CarBrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/carbrands")
@RequiredArgsConstructor
public class CarBrandController {

    @Autowired
    private final CarBrandRepository carBrandRepository;

    @PostMapping
    public ResponseEntity<CarBrand> createBrand(@RequestBody CarBrand brand) {
        CarBrand saved = carBrandRepository.save(brand);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<CarBrand>> getAllBrands() {
        return ResponseEntity.ok(carBrandRepository.findAll());
    }
}
