package com.example.PersonalProject.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();

    public String generateOtp(String phoneNumber) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStorage.put(phoneNumber, otp);
        System.out.println("Mock OTP for " + phoneNumber + ": " + otp); // log for now
        return otp;
    }

    public boolean verifyOtp(String phoneNumber, String otp) {
        String storedOtp = otpStorage.get(phoneNumber);
        return storedOtp != null && storedOtp.equals(otp);
    }

    public void clearOtp(String phoneNumber) {
        otpStorage.remove(phoneNumber);
    }
}
