package com.example.medicalcliniccompanymanager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientRegisterDto {
    private String pesel;
    private String phoneNumber;
    private String registerCode;
    private String password;
    private String passwordAgain;
}
