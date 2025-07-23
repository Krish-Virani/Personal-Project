package com.example.PersonalProject.repository;

import com.example.PersonalProject.entity.Role;
import com.example.PersonalProject.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findByRole(Role role);
}

