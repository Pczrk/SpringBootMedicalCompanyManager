package com.example.medicalcliniccompanymanager.exception;

import com.example.medicalcliniccompanymanager.exception.appexceptions.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.Serializable;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({PatientsAuthCoreException.class, NullArgumentException.class, PatientCoreException.class,
            RegisterCodeCoreException.class, EmployeeAuthCoreException.class, EmployeeCoreException.class,
            InstitutionCoreException.class, ValidationException.class})
    protected ResponseEntity<ExceptionEntity> handleAppExceptions(AppException e){
        return ResponseEntity.status(e.getErrorStatus()).body(e.buildEntity());
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<ErrorDetails> handleAllExceptions(Exception e){
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(ErrorDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .errorMessage(e.getMessage())
                        .build());
    }
    @Data
    @Builder
    public static class ErrorDetails implements Serializable {
        protected final LocalDateTime timestamp;
        protected final String errorMessage;
    }
}
