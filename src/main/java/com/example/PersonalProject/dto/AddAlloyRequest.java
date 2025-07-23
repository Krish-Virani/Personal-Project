package com.example.PersonalProject.dto;

import com.example.PersonalProject.entity.CarModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddAlloyRequest {
    private String name;

    private double size;

    private double width;

    private int offset;

    private String pcd;

    private double price;

    private List<String> compatibleModels;
}
