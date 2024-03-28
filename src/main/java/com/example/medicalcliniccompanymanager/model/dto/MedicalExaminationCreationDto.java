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
public class MedicalExaminationCreationDto {
    private String pesel;
    private Integer institutionId; // might be null
    private LocalDate examinationDate;
    private String details;
}
