package com.example.medicalcliniccompanymanager.service;

import com.example.medicalcliniccompanymanager.exception.appexceptions.RegisterCodeCoreException;
import com.example.medicalcliniccompanymanager.model.dto.RegisterCodeDto;
import com.example.medicalcliniccompanymanager.repository.RegisterCodeRepository;
import com.example.medicalcliniccompanymanager.repository.entity.Institution;
import com.example.medicalcliniccompanymanager.repository.entity.RegisterCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegisterCodeService {
    private final RegisterCodeRepository registerCodeRepository;

    private final SecureRandom secureRandom;

    protected RegisterCode getRegisterCode(String registerCode){
        Optional<RegisterCode> rc = registerCodeRepository.findRegisterCodeByCode(registerCode);
        if (rc.isEmpty())
            throw new RegisterCodeCoreException(HttpStatus.CONFLICT,"Given register code doesn't exists");
        return rc.get();
    }
    protected RegisterCode assertPatientRegisterCode(String registerCodeString) {
        RegisterCode registerCode = getRegisterCode(registerCodeString);
        if (registerCode.getPermissionLevel() != 0)
            throw new RegisterCodeCoreException(HttpStatus.CONFLICT,"Given register code invalid for patient account");
        return registerCode;
    }

    protected void deleteRegisterCode(RegisterCode registerCode) {
        registerCodeRepository.delete(registerCode);
    }

    protected RegisterCode createRegisterCode(Short permissionLevel, Institution institution) {
        return registerCodeRepository.save(RegisterCode.builder()
                        .code(generateRegisterCode())
                        .permissionLevel(permissionLevel)
                        .institution(institution)
                        .expirationDate(Instant.now().plus(Duration.ofDays(14)))
                .build());
    }

    private String generateRegisterCode() {
        byte[] randomBytes = new byte[8];
        secureRandom.nextBytes(randomBytes);
        String code = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes).substring(0,8);
        return registerCodeRepository.existsByCode(code) ? generateRegisterCode():code;
    }

    protected RegisterCodeDto mapToDto(RegisterCode registerCode) {
        return RegisterCodeDto.builder()
                .code(registerCode.getCode())
                .permissionLevel(registerCode.getPermissionLevel())
                .institutionId(registerCode.getInstitution().getId())
                .expirationDate(registerCode.getExpirationDate())
                .build();
    }

}
