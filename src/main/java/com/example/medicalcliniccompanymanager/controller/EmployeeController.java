package com.example.medicalcliniccompanymanager.controller;

import com.example.medicalcliniccompanymanager.model.request.EmployeeRequest;
import com.example.medicalcliniccompanymanager.model.response.MedicalExaminationResponse;
import com.example.medicalcliniccompanymanager.service.EmployeesAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Controller for logged-in employee actions, login, logout and register of an account.
 */
@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeesAuthService employeesAuthService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(
            @CookieValue(value = "eAuth", required = false) String eSessionId,
            @RequestBody EmployeeRequest request,
            HttpServletResponse response){
        employeesAuthService.login(eSessionId, request.getEmployeeLogin(), response);
        return ResponseEntity.ok("Successfully logged in");
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> register(
            @CookieValue(value = "eAuth", required = false) String eSessionId,
            @RequestBody EmployeeRequest request,
            HttpServletResponse response){
        Integer id = employeesAuthService.register(eSessionId, request.getEmployeeRegister(), response);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/employee/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(value = "/logout")
    public ResponseEntity<Object> logout(
            @CookieValue(value = "eAuth", required = false) String eSessionId){
        employeesAuthService.logout(eSessionId);
        return ResponseEntity.ok("Successfully logout");
    }

    @PostMapping(value = "/add-patient", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addPatient(
        @CookieValue(value = "eAuth", required = false) String eSessionId,
        @RequestBody EmployeeRequest request){
        Long id = employeesAuthService.addPatientData(eSessionId, request.getPatient());

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/employee/patient/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/patient/{id}/edit")
    public ResponseEntity<Object> editPatientData(
            @CookieValue(value = "eAuth", required = false) String eSessionId,
            @PathVariable Long id,
            @RequestBody EmployeeRequest request){
        employeesAuthService.editPatientData(eSessionId, id, request.getPatient());
        return ResponseEntity.ok("Successfully updated patient data");
    }

    @GetMapping(value = "/patient/{id}")
    public ResponseEntity<Object> getPatientData(
            @CookieValue(value = "eAuth", required = false) String eSessionId,
            @PathVariable(value = "id") Long patientId){
        return ResponseEntity.ok(employeesAuthService.getPatientData(eSessionId, patientId));
    }

    @DeleteMapping(value = "/patient/{id}")
    public ResponseEntity<Object> deletePatient(
            @CookieValue(value = "eAuth", required = false) String eSessionId,
            @PathVariable(value = "id") Long patientId){
        employeesAuthService.deletePatient(eSessionId,patientId);
        return ResponseEntity.ok("Successfully deleted patient data");
    }

    @GetMapping(value = "/patient/{id}/examinations")
    public ResponseEntity<Object> getPatientMedicalExaminations(
            @CookieValue(value = "eAuth", required = false) String eSessionId,
            @PathVariable(value = "id") Long patientId){
        return ResponseEntity.ok(new MedicalExaminationResponse(employeesAuthService
                .getPatientMedicalExaminations(eSessionId, patientId)));
    }

    @GetMapping(value = "/institution/{id}/examinations")
    public ResponseEntity<Object> getInstitutionMedicalExaminations(
            @CookieValue(value = "eAuth", required = false) String eSessionId,
            @PathVariable(value = "id") Integer institutionId){
        return ResponseEntity.ok(new MedicalExaminationResponse(employeesAuthService
                .getInstitutionMedicalExaminations(eSessionId, institutionId)));
    }

    @GetMapping(value = "/examination/{id}")
    public ResponseEntity<Object> getMedicalExamination(
            @CookieValue(value = "eAuth", required = false) String eSessionId,
            @PathVariable(value = "id") Long id){
        return ResponseEntity.ok(employeesAuthService.getMedicalExamination(eSessionId, id));
    }

    @DeleteMapping(value = "/examination/{id}")
    public ResponseEntity<Object> deleteMedicalExamination(
            @CookieValue(value = "eAuth", required = false) String eSessionId,
            @PathVariable(value = "id") Long id){
        employeesAuthService.deleteMedicalExamination(eSessionId,id);
        return ResponseEntity.ok("Successfully deleted examination");
    }

    @PostMapping(value = "/add-examination", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addMedicalExamination(
            @CookieValue(value = "eAuth", required = false) String eSessionId,
            @RequestBody EmployeeRequest request){
        Long id = employeesAuthService.addMedicalExamination(eSessionId, request.getMedicalExaminationCreation());

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/employee/examination/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/generate-register-code") //not sure if it should be GetMapping
    public ResponseEntity<Object> generateRegisterCode(
            @CookieValue(value = "eAuth", required = false) String eSessionId,
            @RequestParam(value = "permission-level") Short permissionLevel,
            @RequestParam(value = "institution-id") Integer institutionId){
        return ResponseEntity.ok(employeesAuthService.generateRegisterCode(eSessionId, permissionLevel, institutionId));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getEmployee(
            @CookieValue(value = "eAuth", required = false) String eSessionId,
            @PathVariable(value = "id") Integer employeeId){
        return ResponseEntity.ok(employeesAuthService.getEmployee(eSessionId, employeeId));
    }

    @PatchMapping(value = "/{id}/edit-permission-level")
    public ResponseEntity<Object> editPermissionLevel(
            @CookieValue(value = "eAuth", required = false) String eSessionId,
            @PathVariable(value = "id") Integer employeeId,
            @RequestParam(value = "permission-level") Short permissionLevel){
        employeesAuthService.editPermissionLevel(eSessionId, employeeId, permissionLevel);
        return ResponseEntity.ok("Successfully changed permission level");
    }
}
