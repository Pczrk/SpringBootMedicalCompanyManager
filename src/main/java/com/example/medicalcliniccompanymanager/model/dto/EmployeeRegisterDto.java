package com.example.medicalcliniccompanymanager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeRegisterDto {
    private String mail;
    private String password;
    private String passwordAgain;
    private String firstName;
    private String lastName;
    private String registerCode;
}
