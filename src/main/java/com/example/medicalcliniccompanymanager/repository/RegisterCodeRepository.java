package com.example.medicalcliniccompanymanager.repository;

import com.example.medicalcliniccompanymanager.repository.entity.RegisterCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisterCodeRepository extends JpaRepository<RegisterCode, String> {
    Optional<RegisterCode> findRegisterCodeByCode(String code);
    boolean existsByCode(String code);
}