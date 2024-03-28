package com.example.medicalcliniccompanymanager.repository;

import com.example.medicalcliniccompanymanager.repository.entity.PatientsSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientsSessionRepository extends JpaRepository<PatientsSession, String> {
    Optional<PatientsSession> findBySessionId(String pSessionId);
}