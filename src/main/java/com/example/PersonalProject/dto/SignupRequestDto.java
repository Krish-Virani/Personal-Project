package com.example.PersonalProject.dto;

import com.example.PersonalProject.entity.DealerProfile;
import com.example.PersonalProject.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    private String name;
    private String phoneNumber;
    private Role role;
    private DealerProfile dealerProfile = null;

}
