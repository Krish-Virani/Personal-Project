package com.example.PersonalProject.dto;

import com.example.PersonalProject.entity.DealerProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealerModifyRequest {
    private String name;
    private String phoneNumber;
    private DealerProfile dealerProfile;
}
