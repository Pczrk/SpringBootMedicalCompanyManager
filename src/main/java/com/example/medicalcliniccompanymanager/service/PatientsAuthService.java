package com.example.medicalcliniccompanymanager.service;

import com.example.medicalcliniccompanymanager.exception.appexceptions.EmployeeAuthCoreException;
import com.example.medicalcliniccompanymanager.exception.appexceptions.PatientsAuthCoreException;
import com.example.medicalcliniccompanymanager.model.dto.MedicalExaminationDto;
import com.example.medicalcliniccompanymanager.model.dto.PatientLoginDto;
import com.example.medicalcliniccompanymanager.model.dto.PatientRegisterDto;
import com.example.medicalcliniccompanymanager.repository.PatientsSessionRepository;
import com.example.medicalcliniccompanymanager.repository.entity.PatientsSession;
import com.example.medicalcliniccompanymanager.repository.entity.Patient;
import com.example.medicalcliniccompanymanager.model.dto.PatientDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class PatientsAuthService {
    private final PatientsSessionRepository patientsSessionRepository;

    private final PatientService patientService;
    private final MedicalExaminationService medicalExaminationService;
    private final SecureRandom secureRandom;

    private String generateSessionId() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String sessionId = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes).substring(0,32);
        return patientsSessionRepository.existsById(sessionId) ? generateSessionId():sessionId;
    }

    private void createSession(Patient patient, HttpServletResponse response) {
        PatientsSession patientsSession = patientsSessionRepository.save(PatientsSession.builder()
                .sessionId(generateSessionId())
                .patient(patient)
                .expirationDate(Instant.now().plus(Duration.ofMinutes(30)))
                .build());

        Cookie authCookie = new Cookie("pAuth",patientsSession.getSessionId());
        authCookie.setPath("/");
        authCookie.setMaxAge(1800);

        response.addCookie(authCookie);
    }

    public void login(String pSessionId, PatientLoginDto patientLogin, HttpServletResponse response) {
        noActiveSessionAssertion(pSessionId);
        Patient patient = patientService.getPatientIfCorrectCredentials(patientLogin);
        createSession(patient, response);
    }

    public void logout(String pSessionId) {
        if (pSessionId == null)
            throw new EmployeeAuthCoreException(UNAUTHORIZED,"No session to logout");
        Optional<PatientsSession> pS = patientsSessionRepository.findBySessionId(pSessionId);
        if (pS.isEmpty())
            throw new EmployeeAuthCoreException(CONFLICT,"Session already expired");
        patientsSessionRepository.delete(pS.get());
    }

    private void noActiveSessionAssertion(String pSessionId){
        if (pSessionId == null)
            return;
        Optional<PatientsSession> session = patientsSessionRepository.findBySessionId(pSessionId);
        if (session.isEmpty() || session.get().getExpirationDate().isBefore(Instant.now()))
            return;

        throw new PatientsAuthCoreException(CONFLICT,"Active session detected, can't perform this action");
    }

    private Patient validateSession(String pSessionId){
        if (pSessionId == null)
            throw new PatientsAuthCoreException(UNAUTHORIZED,"Patient is not logged-in or session expired");

        Optional<PatientsSession> s = patientsSessionRepository.findBySessionId(pSessionId);

        if (s.isEmpty() || s.get().getExpirationDate().isBefore(Instant.now()))
            throw new PatientsAuthCoreException(UNAUTHORIZED,"Patient is not logged-in or session expired");

        prolongSession(s.get());
        return s.get().getPatient();
    }

    private void prolongSession(PatientsSession session) {
        session.setExpirationDate(Instant.now().plus(Duration.ofMinutes(30)));
        patientsSessionRepository.save(session);
    }

    public void register(String pSessionId, PatientRegisterDto patientRegister, HttpServletResponse response) {
        noActiveSessionAssertion(pSessionId);
        Patient patient = patientService.createPatientAccountIfCorrectCredentials(patientRegister);
        createSession(patient, response);
    }

    public List<MedicalExaminationDto> patientMedicalExaminations(String pSessionId) {
        Patient patient = validateSession(pSessionId);
        return medicalExaminationService.getPatientMedicalExaminations(patient);
    }

    public PatientDto getAccountInfo(String pSessionId) {
        Patient patient = validateSession(pSessionId);
        return patientService.createPatientDto(patient);
    }

}
