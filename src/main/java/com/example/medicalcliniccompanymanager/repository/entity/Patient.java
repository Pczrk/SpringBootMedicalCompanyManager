package com.example.medicalcliniccompanymanager.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id", nullable = false)
    private Long id;

    @Column(name = "pesel", nullable = false, length = 11)
    private String pesel;

    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 70)
    private String lastName;

    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;

    @Column(name = "street", nullable = false, length = 70)
    private String street;

    @Column(name = "city", nullable = false, length = 30)
    private String city;

    @Column(name = "zip_code", nullable = false, length = 6)
    private String zipCode;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(name = "password", length = 300)
    private String password;

    @OneToMany(mappedBy = "patient")
    private Set<MedicalExamination> medicalExaminations = new LinkedHashSet<>();
}