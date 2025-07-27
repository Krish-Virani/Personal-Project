package com.example.PersonalProject.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@Document(collection = "alloys")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alloy {

    @Id
    private ObjectId id;

    private String name;

    private double size;

    private double width;

    private int offset;

    private String pcd;

    private double price;

    private String imageUrl;

    private String imageKey;

    @DBRef
    private User dealer;

    @DBRef
    private List<CarModel> compatibleModels;
}
