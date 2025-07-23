package com.example.PersonalProject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "car_models")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarModel {

    @Id
    private ObjectId id;

    private String modelName;

    @DBRef
    private CarBrand brand;  // Reference back to the brand
}

