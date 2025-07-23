package com.example.PersonalProject.service;

import com.example.PersonalProject.entity.Role;
import com.example.PersonalProject.entity.User;
import com.example.PersonalProject.repository.UserRepository;
import com.example.PersonalProject.repository.UserRepositoryImpl;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    public List<User> getAllDealers(){
        return userRepository.findByRole(Role.DEALER);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public List<User> getAllUnverifiedDealers(){
        return userRepositoryImpl.getAllUnverifiedDealers();
    }

    public void makeDealerVerified(User user){
        userRepository.save(user);
    }
    
    public Optional<User> getDealerById(ObjectId id){
        return userRepository.findById(id);
    }
}
