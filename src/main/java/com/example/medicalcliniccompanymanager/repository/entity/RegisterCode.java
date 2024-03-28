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
@Table(name = "register_codes")
public class RegisterCode {
    @Id
    @Column(name = "register_code", nullable = false, length = 8)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "institution_id", nullable = false)
    private Institution institution;

    @Column(name = "permission_level", nullable = false)
    private Short permissionLevel;

    @Column(name = "expiration_date", nullable = false)
    private Instant expirationDate;
}