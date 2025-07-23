package com.example.PersonalProject.utils;

import com.example.PersonalProject.dto.CarBrandResponse;
import com.example.PersonalProject.dto.CarResponse;
import com.example.PersonalProject.entity.CarBrand;
import com.example.PersonalProject.entity.CarModel;

import java.util.List;
import java.util.stream.Collectors;

public class CarBrandMapper {

    public static CarBrandResponse toResponse(CarBrand carBrand) {
        return CarBrandResponse.builder()
                .id(String.valueOf(carBrand.getId()))
                .brandName(carBrand.getBrandName())
                .build();
    }


    public static List<CarBrandResponse> toResponseList(List<CarBrand> carBrands) {
        return carBrands.stream()
                .map(CarBrandMapper::toResponse)
                .collect(Collectors.toList());
    }

}
