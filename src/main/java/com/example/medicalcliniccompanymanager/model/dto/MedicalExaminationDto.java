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
public class MedicalExaminationDto {
    private Long examinationId;
    private PatientBriefDto patientBrief;
    private InstitutionDto institution;
    private Integer employeeId;
    private LocalDate examinationDate;
    private String details;
}
