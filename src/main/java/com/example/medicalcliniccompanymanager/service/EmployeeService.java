package com.example.medicalcliniccompanymanager.service;

import com.example.medicalcliniccompanymanager.exception.appexceptions.EmployeeCoreException;
import com.example.medicalcliniccompanymanager.exception.appexceptions.NullArgumentException;
import com.example.medicalcliniccompanymanager.model.dto.EmployeeDto;
import com.example.medicalcliniccompanymanager.model.dto.EmployeeLoginDto;
import com.example.medicalcliniccompanymanager.model.dto.EmployeeRegisterDto;
import com.example.medicalcliniccompanymanager.repository.EmployeeRepository;
import com.example.medicalcliniccompanymanager.repository.entity.Employee;
import com.example.medicalcliniccompanymanager.repository.entity.Institution;
import com.example.medicalcliniccompanymanager.repository.entity.RegisterCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    private final RegisterCodeService registerCodeService;
    private final InstitutionService institutionService;
    private final HashingService hashingService;
    private final ValidationService validationService;

    private Employee getEmployeeByMail(String mail){
        Optional<Employee> e = employeeRepository.findEmployeeByMail(mail);
        if (e.isEmpty())
            throw new EmployeeCoreException(HttpStatus.NOT_FOUND,"Patient with given PESEL doesn't exist");
        return e.get();
    }

    protected Employee getEmployeeIfCorrectCredentials(EmployeeLoginDto employeeLogin) {
        if (employeeLogin == null || employeeLogin.getMail() == null || employeeLogin.getPassword() == null)
            throw new NullArgumentException();
        Employee employee = getEmployeeByMail(employeeLogin.getMail());
        if (!hashingService.verifyPassword(employeeLogin.getPassword(), employee.getPassword()))
            throw new EmployeeCoreException(HttpStatus.UNAUTHORIZED,"Password is invalid");
        return employee;
    }

    protected Employee createEmployeeIfCorrectCredentials(EmployeeRegisterDto employeeRegister) {
        if (employeeRegister == null || employeeRegister.getMail() == null || employeeRegister.getPassword() == null
                || employeeRegister.getPasswordAgain() == null || employeeRegister.getFirstName() == null
                || employeeRegister.getLastName() == null || employeeRegister.getRegisterCode() == null)
            throw new NullArgumentException();
        if (!employeeRegister.getPassword().equals(employeeRegister.getPasswordAgain()))
            throw new EmployeeCoreException(HttpStatus.CONFLICT, "Passwords are not equal");

        validationService.validateMail(employeeRegister.getMail());
        validationService.validatePassword(employeeRegister.getPassword());

        RegisterCode registerCode = registerCodeService.getRegisterCode(employeeRegister.getRegisterCode());
        if (registerCode.getPermissionLevel() == 0)
            throw new EmployeeCoreException(HttpStatus.CONFLICT,"Given register code is for patients only");
        Institution institution = registerCode.getInstitution();
        Short permissionLevel = registerCode.getPermissionLevel();
        registerCodeService.deleteRegisterCode(registerCode);

        return employeeRepository.save(Employee.builder()
                        .institution(institution)
                        .mail(employeeRegister.getMail())
                        .password(hashingService.hashPassword(employeeRegister.getPassword()))
                        .firstName(employeeRegister.getFirstName())
                        .lastName(employeeRegister.getLastName())
                        .permissionLevel(permissionLevel)
                .build());
    }

    protected Employee getEmployee(Integer employeeId) {
        Optional<Employee> e = employeeRepository.findEmployeeById(employeeId);
        if (e.isEmpty())
            throw new EmployeeCoreException(HttpStatus.NOT_FOUND,"Employee with given Id doesn't exist");
        return e.get();
    }

    public EmployeeDto mapToDto(Employee employee) {
        return EmployeeDto.builder()
                .id(employee.getId())
                .institution(institutionService.mapToDto(employee.getInstitution()))
                .mail(employee.getMail())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .permissionLevel(employee.getPermissionLevel())
                .build();
    }

    public void changePermissionLevel(Employee employee, Short permissionLevel) {
        employee.setPermissionLevel(permissionLevel);
        employeeRepository.save(employee);
    }
}
