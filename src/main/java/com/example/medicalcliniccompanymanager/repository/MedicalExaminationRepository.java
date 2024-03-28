package com.example.medicalcliniccompanymanager.repository;

import com.example.medicalcliniccompanymanager.repository.entity.MedicalExamination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalExaminationRepository extends JpaRepository<MedicalExamination, Long> {
    Optional<MedicalExamination> findMedicalExaminationById(Long id);
}