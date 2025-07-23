package com.example.PersonalProject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "car_brands")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarBrand {

    @Id
    private ObjectId id;

    private String brandName;

}
