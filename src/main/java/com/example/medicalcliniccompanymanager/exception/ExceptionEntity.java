package com.example.medicalcliniccompanymanager.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionEntity {
    private HttpStatus errorStatus;
    private String message;
}
