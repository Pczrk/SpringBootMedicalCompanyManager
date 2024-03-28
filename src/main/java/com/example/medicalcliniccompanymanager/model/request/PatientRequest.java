package com.example.medicalcliniccompanymanager.model.request;

import com.example.medicalcliniccompanymanager.model.dto.PatientLoginDto;
import com.example.medicalcliniccompanymanager.model.dto.PatientRegisterDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequest {
    private PatientLoginDto patientLogin;
    private PatientRegisterDto patientRegister;
}
