package com.example.medicalcliniccompanymanager.repository;

import com.example.medicalcliniccompanymanager.repository.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Integer> {
    Optional<Institution> findInstitutionById(Integer institutionId);
}