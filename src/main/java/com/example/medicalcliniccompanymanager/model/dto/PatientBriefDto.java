package com.example.medicalcliniccompanymanager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientBriefDto {
    private Long patientId;
    private String firstName;
    private String lastName;
}
