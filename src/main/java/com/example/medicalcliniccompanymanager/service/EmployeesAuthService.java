package com.example.medicalcliniccompanymanager.service;

import com.example.medicalcliniccompanymanager.exception.appexceptions.EmployeeAuthCoreException;
import com.example.medicalcliniccompanymanager.exception.appexceptions.EmployeeCoreException;
import com.example.medicalcliniccompanymanager.exception.appexceptions.PatientsAuthCoreException;
import com.example.medicalcliniccompanymanager.model.dto.*;
import com.example.medicalcliniccompanymanager.repository.EmployeesSessionRepository;
import com.example.medicalcliniccompanymanager.repository.entity.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class EmployeesAuthService {
    private final EmployeesSessionRepository employeesSessionRepository;

    private final EmployeeService employeeService;
    private final PatientService patientService;
    private final MedicalExaminationService medicalExaminationService;
    private final RegisterCodeService registerCodeService;
    private final InstitutionService institutionService;
    private final SecureRandom secureRandom;

    private String generateSessionId() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String sessionId = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes).substring(0,32);
        return employeesSessionRepository.existsById(sessionId) ? generateSessionId():sessionId;
    }

    private void createSession(Employee employee, HttpServletResponse response) {
        EmployeesSession employeesSession = employeesSessionRepository.save(EmployeesSession.builder()
                .sessionId(generateSessionId())
                .employee(employee)
                .expirationDate(Instant.now().plus(Duration.ofMinutes(30)))
                .build());

        Cookie authCookie = new Cookie("eAuth",employeesSession.getSessionId());
        authCookie.setPath("/api");

        response.addCookie(authCookie);
    }

    public void login(String eSessionId, EmployeeLoginDto employeeLogin, HttpServletResponse response){
        noActiveSessionAssertion(eSessionId);
        Employee employee = employeeService.getEmployeeIfCorrectCredentials(employeeLogin);
        createSession(employee,response);
    }

    public Integer register(String eSessionId, EmployeeRegisterDto employeeRegister, HttpServletResponse response) {
        noActiveSessionAssertion(eSessionId);
        Employee employee = employeeService.createEmployeeIfCorrectCredentials(employeeRegister);
        createSession(employee,response);
        return employee.getId();
    }

    private void noActiveSessionAssertion(String eSessionId){
        if (eSessionId == null)
            return;
        Optional<EmployeesSession> session = employeesSessionRepository.findBySessionId(eSessionId);
        if (session.isEmpty() || session.get().getExpirationDate().isBefore(Instant.now()))
            return;

        throw new EmployeeAuthCoreException(CONFLICT,"Active session detected, can't perform this action");
    }

    private Employee validateSession(String eSessionId){
        if (eSessionId == null)
            throw new EmployeeCoreException(UNAUTHORIZED,"Employee is not logged-in or session expired");

        Optional<EmployeesSession> s = employeesSessionRepository.findBySessionId(eSessionId);

        if (s.isEmpty() || s.get().getExpirationDate().isBefore(Instant.now()))
            throw new PatientsAuthCoreException(UNAUTHORIZED,"Employee is not logged-in or session expired");

        prolongSession(s.get());
        return s.get().getEmployee();
    }

    public void prolongSession(EmployeesSession session){
        session.setExpirationDate(Instant.now().plus(Duration.ofMinutes(30)));
        employeesSessionRepository.save(session);
    }

    public Long addPatientData(String eSessionId, PatientDto patient) {
        validateSession(eSessionId);
        return patientService.createPatient(patient).getId();
    }

    public PatientDto getPatientData(String eSessionId, Long patientId) {
        validateSession(eSessionId);
        Patient patient = patientService.getPatientById(patientId);
        return patientService.createPatientDto(patient);
    }

    public Long addMedicalExamination(String eSessionId, MedicalExaminationCreationDto medicalExaminationCreation) {
        Employee employee = validateSession(eSessionId);
        if (employee.getPermissionLevel() == 1 && medicalExaminationCreation.getInstitutionId() != null)
            throw new EmployeeAuthCoreException(UNAUTHORIZED,"Employee with given permission level cannot add medical " +
                    "examination for other institutions");
        MedicalExamination medicalExamination =
                medicalExaminationService.addMedicalExamination(medicalExaminationCreation,employee);
        return medicalExamination.getId();
    }

    public List<MedicalExaminationDto> getPatientMedicalExaminations(String eSessionId, Long patientId) {
        validateSession(eSessionId);
        Patient patient = patientService.getPatientById(patientId);
        return medicalExaminationService.getPatientMedicalExaminations(patient);
    }

    public List<MedicalExaminationDto> getInstitutionMedicalExaminations(String eSessionId, Integer institutionId) {
        Employee employee = validateSession(eSessionId);
        if (employee.getPermissionLevel() == 1 && !Objects.equals(employee.getInstitution().getId(), institutionId))
            throw new EmployeeAuthCoreException(UNAUTHORIZED,"Employee with given permission level can't access another " +
                    "institution medical examinations");
        return medicalExaminationService.getMedicalExaminationsByInstitution(institutionService.getInstitution(institutionId));
    }

    public void editPatientData(String eSessionId, Long id, PatientDto patient) {
        validateSession(eSessionId);
        patientService.employeeEditPatient(id, patient);
    }

    public void logout(String eSessionId) {
        if (eSessionId == null)
            throw new EmployeeAuthCoreException(UNAUTHORIZED,"No session to logout");
        Optional<EmployeesSession> eS = employeesSessionRepository.findBySessionId(eSessionId);
        if (eS.isEmpty())
            throw new EmployeeAuthCoreException(CONFLICT,"Session already expired");
        employeesSessionRepository.delete(eS.get());
    }

    public RegisterCodeDto generateRegisterCode(String eSessionId, Short permissionLevel, Integer institutionId) {
        Employee employee = validateSession(eSessionId);
        if (permissionLevel >= employee.getPermissionLevel())
            throw new EmployeeAuthCoreException(CONFLICT,"Cannot generate register code that gives permission higher " +
                    "or equal to current permissions");
        boolean generateForEmployeesInstitution = Objects.equals(institutionId, employee.getInstitution().getId());
        if (!generateForEmployeesInstitution && employee.getPermissionLevel() == 1)
            throw new EmployeeAuthCoreException(CONFLICT,"Employee with given permission level cannot create register" +
                    "code for other institution");
        Institution institution = generateForEmployeesInstitution
                ? employee.getInstitution() : institutionService.getInstitution(institutionId);

        RegisterCode registerCode = registerCodeService.createRegisterCode(permissionLevel,institution);
        return registerCodeService.mapToDto(registerCode);
    }

    public EmployeeDto getEmployee(String eSessionId, Integer employeeId) {
        Employee loggedInEmployee = validateSession(eSessionId);
        Employee employee = employeeService.getEmployee(employeeId);
        if (!Objects.equals(employeeId, loggedInEmployee.getId()) && employee.getPermissionLevel() >= loggedInEmployee.getPermissionLevel())
            throw new EmployeeAuthCoreException(UNAUTHORIZED,"Employee with given permission cannot access data about " +
                    "employee that has higher or equal permission level");
        return employeeService.mapToDto(employee);
    }

    public void editPermissionLevel(String eSessionId, Integer employeeId, Short permissionLevel) {
        Employee loggedInEmployee = validateSession(eSessionId);
        Employee employee = employeeService.getEmployee(employeeId);
        if (permissionLevel <= 0)
            throw new EmployeeAuthCoreException(CONFLICT,"Invalid permission level");

        if (employee.getPermissionLevel() >= loggedInEmployee.getPermissionLevel())
            throw new EmployeeAuthCoreException(UNAUTHORIZED,"Employee with given permission cannot change permission " +
                    "level of employee that has higher or equal permission level");
        employeeService.changePermissionLevel(employee, permissionLevel);
    }

    public MedicalExaminationDto getMedicalExamination(String eSessionId, Long id) {
        Employee employee = validateSession(eSessionId);
        MedicalExamination medicalExamination = medicalExaminationService.getMedicalExamination(id);
        if (employee.getPermissionLevel() == 1 &&
                !Objects.equals(medicalExamination.getInstitution().getId(), employee.getInstitution().getId()))
            throw new EmployeeAuthCoreException(UNAUTHORIZED,"Employee with given permission cannot directly access " +
                    "medical examination from other institution");
        return medicalExaminationService.mapToDto(medicalExamination);
    }

    public void deleteMedicalExamination(String eSessionId, Long id) {
        Employee employee = validateSession(eSessionId);
        MedicalExamination medicalExamination = medicalExaminationService.getMedicalExamination(id);
        if (employee.getPermissionLevel() == 1 &&
                !Objects.equals(medicalExamination.getInstitution().getId(), employee.getInstitution().getId()))
            throw new EmployeeAuthCoreException(UNAUTHORIZED,"Employee with given permission level can't directly " +
                    "access medical examination from other institution");
        medicalExaminationService.deleteMedicalExamination(medicalExamination);
    }

    public void deletePatient(String eSessionId, Long patientId) {
        Employee employee = validateSession(eSessionId);
        Patient patient = patientService.getPatientById(patientId);

        if (patient.getPhoneNumber() != null && employee.getPermissionLevel() == 1)
            throw new EmployeeAuthCoreException(UNAUTHORIZED, "Employee with given permission level can't delete " +
                    "patient with active account");
        patientService.delete(patient);
    }
}
