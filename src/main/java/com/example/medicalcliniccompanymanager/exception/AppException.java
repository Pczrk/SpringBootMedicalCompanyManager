package com.example.medicalcliniccompanymanager.exception;

import org.springframework.http.HttpStatus;

public interface AppException {
    default ExceptionEntity buildEntity(){
        return ExceptionEntity.builder()
                .errorStatus(this.getErrorStatus())
                .message(this.getMessage())
                .build();
    }

    String getMessage();
    HttpStatus getErrorStatus();
}
