package com.example.medicalcliniccompanymanager.model.response;

import com.example.medicalcliniccompanymanager.model.dto.MedicalExaminationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalExaminationResponse {
    private List<MedicalExaminationDto> medicalExaminationList;
}
