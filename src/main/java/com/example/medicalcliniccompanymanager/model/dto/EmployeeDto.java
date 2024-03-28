package com.example.medicalcliniccompanymanager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDto {
    private Integer id;
    private InstitutionDto institution;
    private String mail;
    private String firstName;
    private String lastName;
    private Short permissionLevel;
}
