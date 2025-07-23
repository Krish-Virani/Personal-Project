package com.example.PersonalProject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.util.Pair;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlloyResponse {
    private String id;

    private String name;

    private double size;

    private double width;

    private int offset;

    private String pcd;

    private double price;

    private String imageUrl;

    private String imageKey;

    private List<Pair<String,Pair<String,String>>> compatibleModels;
}
