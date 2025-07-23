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

    private double size;                 // e.g., "15 inch", "16 inch", etc.

    private double width;                // e.g., "6.5J", optional

    private int offset;

    private String pcd;

    private double price;                // Optional price info

    private String imageUrl;             // Store public S3/image server URL

    private String imageKey;

    @DBRef
    private User dealer;                 // Reference to dealer who uploaded it

    @DBRef
    private List<CarModel> compatibleModels;
}
