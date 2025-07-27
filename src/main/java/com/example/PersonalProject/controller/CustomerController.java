package com.example.PersonalProject.controller;

import com.example.PersonalProject.dto.*;
import com.example.PersonalProject.entity.Alloy;
import com.example.PersonalProject.entity.User;
import com.example.PersonalProject.service.CustomerService;
import com.example.PersonalProject.service.DealerService;
import com.example.PersonalProject.service.JwtService;
import com.example.PersonalProject.service.PublicService;
import com.example.PersonalProject.utils.AlloyMapper;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        return (User) principal;
    }

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DealerService dealerService;

    @Autowired
    private PublicService publicService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/try-alloy")
    public ResponseEntity<?> tryAlloy(
            @RequestParam("url") String alloyUrl,
            @RequestParam("file") MultipartFile carImage
    ) {
        return customerService.processAlloy(carImage, alloyUrl);
    }

    @GetMapping("/search-alloy")
    public ResponseEntity<List<C1AlloyResponse>> searchAlloys(
            @RequestParam(required = false) ObjectId carModelId,
            @RequestParam(required = false) double size,
            @RequestParam(required = false) double width,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city
    ) {
        try{
            System.out.println("size = " + size);
            System.out.println("width = " + width);
            System.out.println("state = " + state);
            System.out.println("city = " + city);
            List<C1AlloyResponse> alloys = customerService.searchAlloys(carModelId, size, width, state, city);
            return new ResponseEntity<>(alloys, HttpStatus.OK);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("get-alloy-by-id/{id}")
    public ResponseEntity<CAlloyResponse> getAlloyById(@PathVariable ObjectId id) {
        try{
            Optional<Alloy> optionalAlloy = dealerService.getAlloyById(id);
            if (optionalAlloy.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Alloy existingAlloy = optionalAlloy.get();
            CAlloyResponse response = AlloyMapper.CtoResponse(existingAlloy);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/modify-customer/{id}")
    public ResponseEntity<?> modifyCustomer(@PathVariable ObjectId id, @RequestBody CustomerModifyRequest customerModifyRequest){
        try{
            if(customerModifyRequest.getPhoneNumber() == null || customerModifyRequest.getPhoneNumber().isBlank()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            User loggedInUser = getLoggedInUser();

            if (!Objects.equals(customerModifyRequest.getPhoneNumber(), loggedInUser.getPhoneNumber()) && publicService.existsByPhoneNumber(customerModifyRequest.getPhoneNumber())) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            if(loggedInUser.getId().equals(id)){
                loggedInUser.setName(customerModifyRequest.getName());
                loggedInUser.setPhoneNumber(customerModifyRequest.getPhoneNumber());
                
                publicService.createUser(loggedInUser);

                String token = jwtService.generateToken(loggedInUser.getPhoneNumber(), loggedInUser.getId().toString());
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setToken(token);
                loginResponse.setRole(loggedInUser.getRole().toString());
                loginResponse.setUserId(loggedInUser.getId().toString());

                return new ResponseEntity<>(loginResponse, HttpStatus.OK);

            }else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id){
        try{
            User loggedInUser = getLoggedInUser();
            if(loggedInUser.getId().toString().equals(id)){
                UserDto user = new UserDto();
                user.setName(loggedInUser.getName());
                user.setPhoneNumber(loggedInUser.getPhoneNumber());
                user.setRole(loggedInUser.getRole());
                user.setCreatedAt(loggedInUser.getCreatedAt().toString());
                return new ResponseEntity<>(user, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
