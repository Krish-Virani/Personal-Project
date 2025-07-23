package com.example.PersonalProject.service;

import com.example.PersonalProject.dto.CAlloyResponse;
import com.example.PersonalProject.entity.Alloy;
import com.example.PersonalProject.repository.AlloyRepositoryImpl;
import com.example.PersonalProject.utils.AlloyMapper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private AlloyRepositoryImpl alloyRepositoryImpl;

    public List<CAlloyResponse> searchAlloys(ObjectId carModelId, double size, double width) {
        List<Alloy> list = alloyRepositoryImpl.searchAlloys(carModelId, size, width);

        List<CAlloyResponse> responseList = AlloyMapper.CtoResponseList(list);
        return responseList;
    }

}
