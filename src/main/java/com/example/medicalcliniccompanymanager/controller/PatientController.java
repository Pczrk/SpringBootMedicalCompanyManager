package com.example.medicalcliniccompanymanager.controller;

import com.example.medicalcliniccompanymanager.model.request.PatientRequest;
import com.example.medicalcliniccompanymanager.model.response.MedicalExaminationResponse;
import com.example.medicalcliniccompanymanager.service.PatientsAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controller for logged-in patients actions, login, logout and register of an account.
 */
@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {
    private final PatientsAuthService patientsAuthService;
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(
            @CookieValue(value = "pAuth", required = false) String pSessionId,
            @RequestBody PatientRequest request,
            HttpServletResponse response){
        patientsAuthService.login(pSessionId, request.getPatientLogin(), response);
        return ResponseEntity.ok("Successfully logged in");
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> register(
            @CookieValue(value = "pAuth", required = false) String pSessionId,
            @RequestBody PatientRequest request,
            HttpServletResponse response){
        patientsAuthService.register(pSessionId, request.getPatientRegister(), response);
        return ResponseEntity.ok("Successfully registered");
    }

    @DeleteMapping(value = "/logout")
    public ResponseEntity<Object> logout(
            @CookieValue(value = "pAuth", required = false) String pSessionId){
        patientsAuthService.logout(pSessionId);
        return ResponseEntity.ok("Successfully logout");
    }

    @GetMapping(value = "/my-medical-examinations")
    public ResponseEntity<Object> myMedicalExaminations(
            @CookieValue(value = "pAuth", required = false) String pSessionId){
        return ResponseEntity.ok(new MedicalExaminationResponse(patientsAuthService.patientMedicalExaminations(pSessionId)));
    }

    @GetMapping(value = "/account")
    public ResponseEntity<Object> accountInfo(
            @CookieValue(value = "pAuth", required = false) String pSessionId){
        return ResponseEntity.ok(patientsAuthService.getAccountInfo(pSessionId));
    }
}
