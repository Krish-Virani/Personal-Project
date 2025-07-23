package com.example.PersonalProject.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadAlloy {
    MultipartFile file;
    String format = "png";
    boolean crop = true;
}
