package com.example.medicalcliniccompanymanager.service;

import com.example.medicalcliniccompanymanager.exception.appexceptions.PatientCoreException;
import com.example.medicalcliniccompanymanager.model.dto.PatientDto;
import com.example.medicalcliniccompanymanager.model.dto.PatientLoginDto;
import com.example.medicalcliniccompanymanager.model.dto.PatientRegisterDto;
import com.example.medicalcliniccompanymanager.repository.PatientRepository;
import com.example.medicalcliniccompanymanager.repository.entity.Patient;
import com.example.medicalcliniccompanymanager.exception.appexceptions.NullArgumentException;

import com.example.medicalcliniccompanymanager.repository.entity.RegisterCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    private final HashingService hashingService;
    private final RegisterCodeService registerCodeService;
    private final ValidationService validationService;

    protected Patient getPatientById(Long patientId){
        Optional<Patient> p = patientRepository.findPatientById(patientId);
        if (p.isEmpty())
            throw new PatientCoreException(HttpStatus.NOT_FOUND,"Patient with given id doesn't exist");
        return p.get();
    }

    protected Patient getPatientByPESEL(String PESEL){
        Optional<Patient> p = patientRepository.findPatientByPesel(PESEL);
        if (p.isEmpty())
            throw new PatientCoreException(HttpStatus.NOT_FOUND,"Patient with given PESEL doesn't exist");
        return p.get();
    }

    protected Patient getPatientIfCorrectCredentials(PatientLoginDto patientLogin) {
        if (patientLogin == null || patientLogin.getPesel() == null || patientLogin.getPassword() == null)
            throw new NullArgumentException();
        Patient patient = getPatientByPESEL(patientLogin.getPesel());
        if (patient.getPhoneNumber() == null)
            throw new PatientCoreException(HttpStatus.CONFLICT,"Patient doesn't have account");
        if (!hashingService.verifyPassword(patientLogin.getPassword(), patient.getPassword()))
            throw new PatientCoreException(HttpStatus.UNAUTHORIZED,"Password is invalid");
        return patient;
    }
    protected Patient createPatientAccountIfCorrectCredentials(PatientRegisterDto patientRegister) {
        if (patientRegister == null || patientRegister.getPesel() == null || patientRegister.getPhoneNumber() == null
                || patientRegister.getRegisterCode() == null || patientRegister.getPassword() == null
                || patientRegister.getPasswordAgain() == null)
            throw new NullArgumentException();
        if (!patientRegister.getPassword().equals(patientRegister.getPasswordAgain()))
            throw new PatientCoreException(HttpStatus.CONFLICT, "Passwords are not equal");

        validationService.validatePassword(patientRegister.getPassword());

        Patient patient = getPatientByPESEL(patientRegister.getPesel());
        if (patient.getPhoneNumber() != null)
            throw new PatientCoreException(HttpStatus.CONFLICT,"Patient already has account");
        RegisterCode registerCode = registerCodeService.assertPatientRegisterCode(patientRegister.getRegisterCode());
        patient.setPhoneNumber(patientRegister.getPhoneNumber());
        patient.setPassword(hashingService.hashPassword(patientRegister.getPassword()));
        patientRepository.save(patient);
        registerCodeService.deleteRegisterCode(registerCode);
        return patient;
    }

    protected Patient createPatient(PatientDto patient){
        if (patient == null || patient.getPesel() == null || patient.getFirstName() == null || patient.getLastName() == null
                || patient.getBirthdate() == null || patient.getStreet() == null || patient.getCity() == null
                || patient.getZipCode() == null) //ignore id and phoneNumber
            throw new NullArgumentException();

        validationService.validatePESEL(patient.getPesel());

        Optional<Patient> p = patientRepository.findPatientByPesel(patient.getPesel());
        if (p.isPresent())
            throw new PatientCoreException(HttpStatus.CONFLICT,"Patient with given PESEL already exists");
        return patientRepository.save(Patient.builder()
                        .pesel(patient.getPesel())
                        .firstName(patient.getFirstName())
                        .lastName(patient.getLastName())
                        .birthdate(patient.getBirthdate())
                        .street(patient.getStreet())
                        .city(patient.getCity())
                        .zipCode(patient.getZipCode())
                .build());
    }

    protected PatientDto createPatientDto(Patient patient) {
        return PatientDto.builder()
                .patientId(patient.getId())
                .pesel(patient.getPesel())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .birthdate(patient.getBirthdate())
                .street(patient.getStreet())
                .city(patient.getCity())
                .zipCode(patient.getZipCode())
                .phoneNumber(patient.getPhoneNumber())
                .build();
    }

    protected void employeeEditPatient(Long id, PatientDto patientDto) {
        if (patientDto == null || patientDto.getPesel() == null || patientDto.getFirstName() == null
                || patientDto.getLastName() == null || patientDto.getBirthdate() == null || patientDto.getStreet() == null
                || patientDto.getCity() == null || patientDto.getZipCode() == null) //ignore id and phoneNumber
            throw new NullArgumentException();

        validationService.validatePESEL(patientDto.getPesel());

        Patient patient = getPatientById(id);
        patient.setPesel(patientDto.getPesel());
        patient.setFirstName(patientDto.getFirstName());
        patient.setLastName(patientDto.getLastName());
        patient.setBirthdate(patientDto.getBirthdate());
        patient.setStreet(patientDto.getStreet());
        patient.setCity(patientDto.getCity());
        patient.setZipCode(patientDto.getZipCode());
        patientRepository.save(patient);
    }

    public void delete(Patient patient) {
        patientRepository.delete(patient);
    }
}
