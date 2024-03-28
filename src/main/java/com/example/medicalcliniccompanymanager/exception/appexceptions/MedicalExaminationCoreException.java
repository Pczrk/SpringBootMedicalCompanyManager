package com.example.medicalcliniccompanymanager.exception.appexceptions;

import com.example.medicalcliniccompanymanager.exception.AppException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class MedicalExaminationCoreException extends RuntimeException implements AppException {
    private final HttpStatus errorStatus;
    private String message;
}
