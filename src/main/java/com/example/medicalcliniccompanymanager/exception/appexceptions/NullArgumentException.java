package com.example.medicalcliniccompanymanager.exception.appexceptions;

import com.example.medicalcliniccompanymanager.exception.AppException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class NullArgumentException extends IllegalArgumentException implements AppException {
    private final HttpStatus errorStatus = HttpStatus.BAD_REQUEST;
    private final String message = "At least one of the attributes is null";
}