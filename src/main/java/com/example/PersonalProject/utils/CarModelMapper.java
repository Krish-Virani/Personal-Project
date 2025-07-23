package com.example.PersonalProject.utils;

import com.example.PersonalProject.dto.AlloyResponse;
import com.example.PersonalProject.dto.CarResponse;
import com.example.PersonalProject.entity.Alloy;
import com.example.PersonalProject.entity.CarModel;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class CarModelMapper {

    public static CarResponse toResponse(CarModel carModel) {
        return CarResponse.builder()
                .id(String.valueOf(carModel.getId()))
                .modelName(carModel.getModelName())
                .build();
    }


    public static List<CarResponse> toResponseList(List<CarModel> carModels) {
        return carModels.stream()
                .map(CarModelMapper::toResponse)
                .collect(Collectors.toList());
    }
}
