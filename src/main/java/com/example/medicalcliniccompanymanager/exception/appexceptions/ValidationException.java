package com.example.medicalcliniccompanymanager.exception.appexceptions;

import com.example.medicalcliniccompanymanager.exception.AppException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ValidationException extends IllegalArgumentException implements AppException {
    private final HttpStatus errorStatus = HttpStatus.CONFLICT;
    private String message;
}