package com.example.PersonalProject.service;

import com.example.PersonalProject.entity.Location;
import com.example.PersonalProject.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Location findById(String id) {
        return locationRepository.findById(id).orElse(null);
    }
}
