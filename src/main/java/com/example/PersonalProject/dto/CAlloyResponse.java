package com.example.PersonalProject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CAlloyResponse {
    private String name;

    private double size;

    private double width;

    private int offset;

    private String pcd;

    private double price;

    private String imageUrl;

    private String imageKey;
}
