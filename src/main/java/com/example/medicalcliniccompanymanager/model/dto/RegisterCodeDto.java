package com.example.medicalcliniccompanymanager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterCodeDto {
    private String code;
    private Integer institutionId;
    private Short permissionLevel;
    private Instant expirationDate;

}
