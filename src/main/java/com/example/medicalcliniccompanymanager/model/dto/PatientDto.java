package com.example.medicalcliniccompanymanager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDto {
    private Long patientId;
    private String pesel;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String street;
    private String city;
    private String zipCode;
    private String phoneNumber;
}
