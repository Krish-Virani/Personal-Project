package com.example.PersonalProject.controller;

import com.example.PersonalProject.entity.User;
import com.example.PersonalProject.service.AdminService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("get-all-dealers")
    public ResponseEntity<List<User>> getAllDealers(){
        try {
            List<User> users = adminService.getAllDealers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<User>> getAllUsers(){
       try {
            List<User> users = adminService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get-all-unverified-dealers")
    public ResponseEntity<List<User>> getAllUnverifiedDealers(){
        try {
            List<User> users = adminService.getAllUnverifiedDealers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/verify-dealer/{id}")
    public ResponseEntity<User> makeDealerVerified(@PathVariable ObjectId id){
        try{
            Optional<User> old = adminService.getDealerById(id);
            if(old.isPresent()){
                User u = old.get();
                u.getDealerProfile().setVerified(true);
                adminService.makeDealerVerified(u);
                return new ResponseEntity<>(u, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/get-dealer-by-id/{id}")
    public ResponseEntity<Optional<User>> getDealerById(@PathVariable ObjectId id){
        try{
            Optional<User> user = adminService.getDealerById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
