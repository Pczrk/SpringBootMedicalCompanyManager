package com.example.medicalcliniccompanymanager.repository;

import com.example.medicalcliniccompanymanager.repository.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findEmployeeByMail(String mail);
    Optional<Employee> findEmployeeById(Integer id);

}