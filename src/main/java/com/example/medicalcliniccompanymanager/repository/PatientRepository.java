package com.example.medicalcliniccompanymanager.repository;

import com.example.medicalcliniccompanymanager.repository.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findPatientByPesel(String pesel);
    Optional<Patient> findPatientById(Long patientId);
}