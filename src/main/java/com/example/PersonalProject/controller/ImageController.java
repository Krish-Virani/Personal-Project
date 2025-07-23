package com.example.PersonalProject.controller;

import com.example.PersonalProject.entity.Alloy;
import com.example.PersonalProject.entity.User;
import com.example.PersonalProject.repository.AlloyRepository;
import com.example.PersonalProject.service.S3Service;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/dealer")
public class ImageController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private AlloyRepository alloyRepository;

    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        return (User) principal;
    }

    @PostMapping("/alloy-image-upload/{alloyId}")
    public ResponseEntity<?> uploadImage(@PathVariable ObjectId alloyId, @RequestParam("file") MultipartFile file) {
        try {
            User user = getLoggedInUser();
            Optional<Alloy> optionalAlloy = alloyRepository.findById(alloyId);
            if (optionalAlloy.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alloy not found");
            }

            Alloy alloy = optionalAlloy.get();

            // ✅ Check if the dealer owns this alloy
            if (!alloy.getDealer().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not own this alloy");
            }

            String contentType = file.getContentType();
            if (contentType == null ||
                    !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
                return ResponseEntity.badRequest().body("Only JPG and PNG files are allowed.");
            }

            // ✅ Validate file size (< 5 MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest().body("File size must be less than 5 MB.");
            }

            Map<String,String> map = s3Service.uploadFile(file,alloy);
            return ResponseEntity.ok("Image uploaded successfully: " + map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }

}
