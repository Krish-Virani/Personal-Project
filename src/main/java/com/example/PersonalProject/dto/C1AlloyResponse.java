package com.example.PersonalProject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class C1AlloyResponse {
    private String id;
    private String name;
    private String imageUrl;
}
