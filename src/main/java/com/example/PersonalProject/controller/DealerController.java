package com.example.PersonalProject.controller;

import com.example.PersonalProject.dto.*;
import com.example.PersonalProject.entity.*;
import com.example.PersonalProject.repository.car.CarModelRepository;
import com.example.PersonalProject.service.DealerService;
import com.example.PersonalProject.service.JwtService;
import com.example.PersonalProject.service.PublicService;
import com.example.PersonalProject.utils.AlloyMapper;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dealer")
public class DealerController {

    @Autowired
    private DealerService dealerService;

    @Autowired
    private CarModelRepository carModelRepository;

    @Autowired
    private PublicService  publicService;

    @Autowired
    private JwtService jwtService;

    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        return (User) principal;
    }

    @PostMapping("/add-alloy")
    public ResponseEntity<?> addAlloy(@RequestBody AddAlloyRequest addAlloyRequest) {
        try{
            User loggedInUser = getLoggedInUser();
            Alloy alloy = new Alloy();
            alloy.setName(addAlloyRequest.getName());
            alloy.setSize(addAlloyRequest.getSize());
            alloy.setWidth(addAlloyRequest.getWidth());
            alloy.setOffset(addAlloyRequest.getOffset());
            alloy.setPcd(addAlloyRequest.getPcd());
            alloy.setPrice(addAlloyRequest.getPrice());

            List<CarModel> models = addAlloyRequest.getCompatibleModels().stream()
                    .map(ObjectId::new)
                    .map(id -> carModelRepository.findById(id).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            alloy.setCompatibleModels(models);


            alloy.setDealer(loggedInUser);
            dealerService.addAlloy(alloy);
            return new ResponseEntity<>("successsful",HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("Bad request",HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/modify-alloy/{alloyId}")
    public ResponseEntity<?> modifyAlloy(@PathVariable ObjectId alloyId, @RequestBody AddAlloyRequest addAlloyRequest) {
        try {
            User loggedInUser = getLoggedInUser();

            Optional<Alloy> optionalAlloy = dealerService.getAlloyById(alloyId);
            if (optionalAlloy.isEmpty()) {
                return new ResponseEntity<>("Alloy not found",HttpStatus.BAD_REQUEST);
            }

            Alloy existingAlloy = optionalAlloy.get();

            if (!existingAlloy.getDealer().getId().equals(loggedInUser.getId())) {
                return new ResponseEntity<>("AYou do not own this alloy",HttpStatus.FORBIDDEN);
            }

            // ✅ Apply only allowed modifications
            existingAlloy.setName(addAlloyRequest.getName());
            existingAlloy.setSize(addAlloyRequest.getSize());
            existingAlloy.setWidth(addAlloyRequest.getWidth());
            existingAlloy.setOffset(addAlloyRequest.getOffset());
            existingAlloy.setPcd(addAlloyRequest.getPcd());
            existingAlloy.setPrice(addAlloyRequest.getPrice());
            List<CarModel> models = addAlloyRequest.getCompatibleModels().stream()
                    .map(ObjectId::new)
                    .map(id -> carModelRepository.findById(id).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            existingAlloy.setCompatibleModels(models);

            dealerService.addAlloy(existingAlloy);
            return new ResponseEntity<>("alloy edit successful",HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping("/delete-alloy/{alloyId}")
    public ResponseEntity<?> deleteAlloyById(@PathVariable ObjectId alloyId){
        try{
            User loggedInUser = getLoggedInUser();

            Optional<Alloy> optionalAlloy = dealerService.getAlloyById(alloyId);
            if (optionalAlloy.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alloy not found");
            }

            Alloy existingAlloy = optionalAlloy.get();

            if (!existingAlloy.getDealer().getId().equals(loggedInUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not own this alloy");
            }
            dealerService.deleteAlloy(alloyId);
            return new ResponseEntity<>(existingAlloy, HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-all-alloys-of-dealer")
    public ResponseEntity<List<AlloyResponse>> getAllAlloysOfDealer(){
        try{
            User loggedInUser = getLoggedInUser();
            ObjectId userId = loggedInUser.getId();
            List<AlloyResponse> list = dealerService.getAllAlloysOfDealer(userId);
            return new ResponseEntity<>(list, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-alloy-by-id/{id}")
    public ResponseEntity<?> getAlloyById(@PathVariable ObjectId id){
        try{
            User loggedInUser = getLoggedInUser();

            Optional<Alloy> optionalAlloy = dealerService.getAlloyById(id);
            if (optionalAlloy.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Alloy existingAlloy = optionalAlloy.get();

            if (!existingAlloy.getDealer().getId().equals(loggedInUser.getId())) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            AlloyResponse alloyResponse = AlloyMapper.toResponse(existingAlloy);
            return new ResponseEntity<>(alloyResponse, HttpStatus.OK);

        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-alloy-by-name/{name}")
    public ResponseEntity<?> getAlloyById(@PathVariable String name){
        try{
            User loggedInUser = getLoggedInUser();
            ObjectId userId = loggedInUser.getId();
            List<Alloy> list = dealerService.getAlloysByName(name,userId);
            return new ResponseEntity<>(list, HttpStatus.OK);
        }catch(Exception e){
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
                user.setDealerProfile(loggedInUser.getDealerProfile());
                user.setCreatedAt(loggedInUser.getCreatedAt().toString());
                return new ResponseEntity<>(user, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/modify-dealer/{id}")
    public ResponseEntity<?> modifyDealer(@PathVariable ObjectId id, @RequestBody DealerModifyRequest dealerModifyRequest){
        try{
            if(dealerModifyRequest.getPhoneNumber() == null || dealerModifyRequest.getPhoneNumber().isBlank()) {
                 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            User loggedInUser = getLoggedInUser();

            if (!Objects.equals(dealerModifyRequest.getPhoneNumber(), loggedInUser.getPhoneNumber()) && publicService.existsByPhoneNumber(dealerModifyRequest.getPhoneNumber())) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }

            if(loggedInUser.getId().equals(id)){
                loggedInUser.setName(dealerModifyRequest.getName());
                loggedInUser.setPhoneNumber(dealerModifyRequest.getPhoneNumber());
                loggedInUser.setDealerProfile(dealerModifyRequest.getDealerProfile());

                loggedInUser.setCreatedAt(LocalDateTime.now());
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

    

}
