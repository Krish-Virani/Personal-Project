package com.example.PersonalProject.controller.car;

import com.example.PersonalProject.entity.CarBrand;
import com.example.PersonalProject.entity.CarModel;
import com.example.PersonalProject.repository.car.CarBrandRepository;
import com.example.PersonalProject.repository.car.CarModelRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/carmodels")
@RequiredArgsConstructor
public class CarModelController {

    @Autowired
    private CarModelRepository carModelRepository;

    @Autowired
    private CarBrandRepository carBrandRepository;

    @PostMapping
    public ResponseEntity<?> createModel(@RequestBody CarModel request) {
        Optional<CarBrand> brandOpt = carBrandRepository.findById(request.getBrand().getId());
        if (brandOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Brand not found");
        }

        carModelRepository.save(request);
        return ResponseEntity.ok(request);
    }

}


