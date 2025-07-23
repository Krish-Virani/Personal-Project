package com.example.PersonalProject.controller;

import com.example.PersonalProject.entity.Location;
import com.example.PersonalProject.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/public/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/states")
    public ResponseEntity<?> findAll() {
        try{
            List<Location> states = locationService.findAll();
            return new ResponseEntity<>(states, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/cities/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {
        try{
            Location location = locationService.findById(id);
            return new ResponseEntity<>(location, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}