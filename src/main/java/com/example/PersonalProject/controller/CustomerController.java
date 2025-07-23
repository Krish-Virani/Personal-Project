package com.example.PersonalProject.controller;

import com.example.PersonalProject.dto.AlloyResponse;
import com.example.PersonalProject.dto.CAlloyResponse;
import com.example.PersonalProject.entity.Alloy;
import com.example.PersonalProject.entity.User;
import com.example.PersonalProject.service.CustomerService;
import com.example.PersonalProject.service.JwtService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/search-alloy")
    public ResponseEntity<List<CAlloyResponse>> searchAlloys(
            @RequestParam(required = false) ObjectId carModelId,
            @RequestParam(required = false) double size,
            @RequestParam(required = false) double width
    ) {
        try{
            List<CAlloyResponse> alloys = customerService.searchAlloys(carModelId, size, width);
            return new ResponseEntity<>(alloys, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
