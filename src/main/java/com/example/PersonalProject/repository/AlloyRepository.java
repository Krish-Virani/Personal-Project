package com.example.PersonalProject.repository;

import com.example.PersonalProject.entity.Alloy;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlloyRepository extends MongoRepository<Alloy, ObjectId> {

    Optional<Alloy> findById(ObjectId id);

    void deleteById(ObjectId id);

}

