package com.example.PersonalProject.service;

import com.example.PersonalProject.dto.AlloyResponse;
import com.example.PersonalProject.entity.Alloy;
import com.example.PersonalProject.repository.AlloyRepository;
import com.example.PersonalProject.repository.AlloyRepositoryImpl;
import com.example.PersonalProject.utils.AlloyMapper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DealerService {

    @Autowired
    private AlloyRepository alloyRepository;

    @Autowired
    private AlloyRepositoryImpl alloyRepositoryImpl;

    public void addAlloy(Alloy alloy){
        alloyRepository.save(alloy);
    }

    public void deleteAlloy(ObjectId id){
        alloyRepository.deleteById(id);
    }

    public List<Alloy> getAllAlloys(){
        return alloyRepository.findAll();
    }

    public List<AlloyResponse> getAllAlloysOfDealer(ObjectId userId){

        List<Alloy> list =  alloyRepositoryImpl.findByUserId(userId);

        List<AlloyResponse> responseList = AlloyMapper.toResponseList(list);
        return responseList;


    }

    public Optional<Alloy> getAlloyById(ObjectId id){
        return alloyRepository.findById(id);
    }

    public List<Alloy> getAlloysByName(String name,  ObjectId userId){
        return alloyRepositoryImpl.findAlloysByName(name,userId);
    }

}
