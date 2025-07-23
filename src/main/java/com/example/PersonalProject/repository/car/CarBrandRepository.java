package com.example.PersonalProject.repository.car;

import com.example.PersonalProject.entity.CarBrand;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarBrandRepository extends MongoRepository<CarBrand, ObjectId> { }

