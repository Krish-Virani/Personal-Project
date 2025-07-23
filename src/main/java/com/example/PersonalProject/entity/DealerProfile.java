package com.example.PersonalProject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DealerProfile {

    private String shopName;
    private String shopCity;
    private String shopState;
    private String shopImage;
    private boolean isVerified = false;
    private Long likes= 0L;
}
