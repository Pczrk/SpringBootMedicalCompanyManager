package com.example.medicalcliniccompanymanager.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees_sessions")
public class EmployeesSession {
    @Id
    @Column(name = "session_id", nullable = false, length = 32)
    private String sessionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "expiration_date", nullable = false)
    private Instant expirationDate;

}