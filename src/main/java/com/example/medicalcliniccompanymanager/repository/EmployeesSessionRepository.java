package com.example.medicalcliniccompanymanager.repository;

import com.example.medicalcliniccompanymanager.repository.entity.EmployeesSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeesSessionRepository extends JpaRepository<EmployeesSession, String> {
    Optional<EmployeesSession> findBySessionId(String eSessionId);
}