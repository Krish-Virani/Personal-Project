package com.example.PersonalProject.service;

import com.example.PersonalProject.dto.AlloyResponse;
import com.example.PersonalProject.dto.CarBrandResponse;
import com.example.PersonalProject.dto.CarResponse;
import com.example.PersonalProject.entity.CarBrand;
import com.example.PersonalProject.entity.CarModel;
import com.example.PersonalProject.repository.car.CarBrandRepository;
import com.example.PersonalProject.repository.car.CarModelRepository;
import com.example.PersonalProject.repository.car.CarModelRepositoryImpl;
import com.example.PersonalProject.utils.AlloyMapper;
import com.example.PersonalProject.utils.CarBrandMapper;
import com.example.PersonalProject.utils.CarModelMapper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarModelRepositoryImpl carModelRepositoryImpl;

    @Autowired
    private CarBrandRepository carBrandRepository;

    public List<CarResponse> findModelsByCarId(ObjectId id) {

        List<CarModel> list = carModelRepositoryImpl.findModelsByCarId(id);
        List<CarResponse> responseList = CarModelMapper.toResponseList(list);
        return responseList;

    }

    public List<CarBrandResponse> findCarBrands() {
        List<CarBrand> list = carBrandRepository.findAll();
        List<CarBrandResponse> responseList = CarBrandMapper.toResponseList(list);
        return responseList;
    }
}
