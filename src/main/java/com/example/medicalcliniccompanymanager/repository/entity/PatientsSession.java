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
@Table(name = "patients_sessions")
public class PatientsSession {
    @Id
    @Column(name = "session_id", nullable = false, length = 32)
    private String sessionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "expiration_date", nullable = false)
    private Instant expirationDate;

}