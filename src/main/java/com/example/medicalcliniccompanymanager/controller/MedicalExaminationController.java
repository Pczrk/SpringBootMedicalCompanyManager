package com.example.medicalcliniccompanymanager.controller;

import com.example.medicalcliniccompanymanager.service.MedicalExaminationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Controller for not logged in people to get medical examination info
 */
@RestController
@RequestMapping("/api/medical-examination")
@RequiredArgsConstructor
public class MedicalExaminationController {
    private final MedicalExaminationService medicalExaminationService;

    @GetMapping("")
    public ResponseEntity<Object> getMedicalExamination(
            @RequestParam(value = "medical-examination-id") Long id,
            @RequestParam(value = "date-of-birth") LocalDate date){
        return ResponseEntity.ok(medicalExaminationService.getMedicalExaminationWithCorrectRequest(id, date));
    }
}
