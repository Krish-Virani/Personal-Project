package com.example.PersonalProject.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private String message;
    private String code;
    private T data = null;

    public ApiResponse(String message, String code) {
        this.message = message;
        this.code = code;

    }
}
