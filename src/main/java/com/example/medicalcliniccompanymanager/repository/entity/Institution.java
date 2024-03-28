package com.example.medicalcliniccompanymanager.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "institutions")
public class Institution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "institution_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 70)
    private String name;

    @Column(name = "street", nullable = false, length = 70)
    private String street;

    @Column(name = "city", nullable = false, length = 30)
    private String city;

    @Column(name = "zip_code", nullable = false, length = 6)
    private String zipCode;

    @OneToMany(mappedBy = "institution")
    private Set<MedicalExamination> medicalExaminations = new LinkedHashSet<>();

}