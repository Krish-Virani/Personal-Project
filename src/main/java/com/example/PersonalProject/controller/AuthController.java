package com.example.PersonalProject.controller;

import com.example.PersonalProject.dto.LoginRequest;
import com.example.PersonalProject.dto.LoginResponse;
import com.example.PersonalProject.dto.SignupRequestDto;
import com.example.PersonalProject.entity.ApiResponse;
import com.example.PersonalProject.entity.Role;
import com.example.PersonalProject.entity.User;
import com.example.PersonalProject.repository.UserRepository;
import com.example.PersonalProject.service.JwtService;
import com.example.PersonalProject.service.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PublicService publicService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto signupRequestDto) {
        if(signupRequestDto.getPhoneNumber() == null || signupRequestDto.getPhoneNumber().isBlank()) {
            return new ResponseEntity<>(new ApiResponse("Phone number is required", HttpStatus.BAD_REQUEST.toString()), HttpStatus.BAD_REQUEST);
        }
        if (signupRequestDto.getRole() == null || (signupRequestDto.getRole() != Role.CUSTOMER && signupRequestDto.getRole() != Role.DEALER)) {
            return new ResponseEntity<>(new ApiResponse("Invalid role selection. Only DEALER or CUSTOMER allowed.", HttpStatus.FORBIDDEN.toString()), HttpStatus.FORBIDDEN);
        }

        if (publicService.existsByPhoneNumber(signupRequestDto.getPhoneNumber())) {
            return new ResponseEntity<>(new ApiResponse("Phone number already exists", HttpStatus.CONFLICT.toString()), HttpStatus.CONFLICT);
        }

        if (signupRequestDto.getRole() == Role.CUSTOMER && signupRequestDto.getDealerProfile() != null) {
            return new ResponseEntity<>(new ApiResponse("Customer cannot submit dealer profile", HttpStatus.FORBIDDEN.toString()), HttpStatus.FORBIDDEN);

        }

        User user = new User();
        user.setName(signupRequestDto.getName());
        user.setPhoneNumber(signupRequestDto.getPhoneNumber());
        user.setRole(signupRequestDto.getRole());
        user.setDealerProfile(signupRequestDto.getDealerProfile());

        user.setCreatedAt(LocalDateTime.now());
        publicService.createUser(user);

        String token = jwtService.generateToken(user.getPhoneNumber(), user.getId().toString());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setRole(user.getRole().toString());
        loginResponse.setUserId(user.getId().toString());

        return new ResponseEntity<>(new ApiResponse("User created.", HttpStatus.OK.toString(), loginResponse), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String phone = request.getPhoneNumber();

        Optional<User> userOpt = userRepository.findByPhoneNumber(phone);

        if (userOpt.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("User not found.", HttpStatus.NOT_FOUND.toString()), HttpStatus.NOT_FOUND);
        }

        User user = userOpt.get();

        String token = jwtService.generateToken(user.getPhoneNumber(), user.getId().toString());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setRole(user.getRole().toString());
        loginResponse.setUserId(user.getId().toString());

        return new ResponseEntity<>(new ApiResponse("User logged in.", HttpStatus.OK.toString(), loginResponse), HttpStatus.OK);
    }
}
