package com.example.PersonalProject.dto;

import com.example.PersonalProject.entity.DealerProfile;
import com.example.PersonalProject.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String name;
    private String phoneNumber;
    private Role role;
    private DealerProfile dealerProfile = null;
    private String createdAt;
}
