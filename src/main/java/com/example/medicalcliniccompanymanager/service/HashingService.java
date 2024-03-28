package com.example.medicalcliniccompanymanager.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class HashingService {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    protected String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    protected boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
