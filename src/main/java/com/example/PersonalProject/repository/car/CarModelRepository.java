package com.example.PersonalProject.repository.car;

import com.example.PersonalProject.entity.CarModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarModelRepository extends MongoRepository<CarModel, ObjectId> {
}
