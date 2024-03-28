package com.example.medicalcliniccompanymanager.service;

import com.example.medicalcliniccompanymanager.exception.appexceptions.InstitutionCoreException;
import com.example.medicalcliniccompanymanager.model.dto.InstitutionDto;
import com.example.medicalcliniccompanymanager.repository.InstitutionRepository;
import com.example.medicalcliniccompanymanager.repository.entity.Institution;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstitutionService {
    private final InstitutionRepository institutionRepository;

    public InstitutionDto mapToDto(Institution institution) {
        return InstitutionDto.builder()
                .institutionId(institution.getId())
                .name(institution.getName())
                .street(institution.getStreet())
                .city(institution.getCity())
                .zipCode(institution.getZipCode())
                .build();
    }

    protected Institution getInstitution(Integer institutionId){
        Optional<Institution> i = institutionRepository.findInstitutionById(institutionId);
        if (i.isEmpty())
            throw new InstitutionCoreException(HttpStatus.NOT_FOUND,"Institution with given id wasn't found");
        return i.get();
    }
}
