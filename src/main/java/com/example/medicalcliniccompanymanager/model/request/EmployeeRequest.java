package com.example.medicalcliniccompanymanager.model.request;

import com.example.medicalcliniccompanymanager.model.dto.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {
    private EmployeeLoginDto employeeLogin;
    private EmployeeRegisterDto employeeRegister;
    private PatientDto patient; //ignore id and phoneNumber
    private MedicalExaminationCreationDto medicalExaminationCreation;
}
