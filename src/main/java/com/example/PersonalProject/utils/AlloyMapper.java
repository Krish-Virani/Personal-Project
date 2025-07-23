package com.example.PersonalProject.utils;

import com.example.PersonalProject.dto.AlloyResponse;
import com.example.PersonalProject.dto.CAlloyResponse;
import com.example.PersonalProject.entity.Alloy;
import org.bson.types.ObjectId;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class AlloyMapper {

    public static AlloyResponse toResponse(Alloy alloy) {
        return AlloyResponse.builder()
                .id(String.valueOf(alloy.getId()))
                .name(alloy.getName())
                .size(alloy.getSize())
                .width(alloy.getWidth())
                .offset(alloy.getOffset())
                .pcd(alloy.getPcd())
                .price(alloy.getPrice())
                .imageUrl(alloy.getImageUrl())
                .imageKey(alloy.getImageKey())
                .compatibleModels(
                        alloy.getCompatibleModels() == null ? List.of() :
                                alloy.getCompatibleModels().stream()
                                        .map(model -> {
                                            String modelId = String.valueOf(model.getId());
                                            String modelName = model.getModelName();
                                            String companyName = model.getBrand() != null ? model.getBrand().getBrandName() : "Unknown";
                                            return Pair.of(modelId,Pair.of(companyName, modelName));
                                        })
                                        .collect(Collectors.toList())
                )
                .build();
    }


    public static List<AlloyResponse> toResponseList(List<Alloy> alloys) {
        return alloys.stream()
                .map(AlloyMapper::toResponse)
                .collect(Collectors.toList());
    }


    public static CAlloyResponse CtoResponse(Alloy alloy) {
        return CAlloyResponse.builder()
                .name(alloy.getName())
                .size(alloy.getSize())
                .width(alloy.getWidth())
                .offset(alloy.getOffset())
                .pcd(alloy.getPcd())
                .price(alloy.getPrice())
                .imageUrl(alloy.getImageUrl())
                .imageKey(alloy.getImageKey())
                .build();
    }


    public static List<CAlloyResponse> CtoResponseList(List<Alloy> alloys) {
        return alloys.stream()
                .map(AlloyMapper::CtoResponse)
                .collect(Collectors.toList());
    }
}

