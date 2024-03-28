package com.example.medicalcliniccompanymanager.service;

import com.example.medicalcliniccompanymanager.exception.appexceptions.MedicalExaminationCoreException;
import com.example.medicalcliniccompanymanager.exception.appexceptions.NullArgumentException;
import com.example.medicalcliniccompanymanager.model.dto.InstitutionDto;
import com.example.medicalcliniccompanymanager.model.dto.MedicalExaminationCreationDto;
import com.example.medicalcliniccompanymanager.model.dto.MedicalExaminationDto;
import com.example.medicalcliniccompanymanager.model.dto.PatientBriefDto;
import com.example.medicalcliniccompanymanager.repository.MedicalExaminationRepository;
import com.example.medicalcliniccompanymanager.repository.entity.Employee;
import com.example.medicalcliniccompanymanager.repository.entity.Institution;
import com.example.medicalcliniccompanymanager.repository.entity.MedicalExamination;
import com.example.medicalcliniccompanymanager.repository.entity.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicalExaminationService {
    private final MedicalExaminationRepository medicalExaminationRepository;

    private final InstitutionService institutionService;
    private final PatientService patientService;

    protected List<MedicalExaminationDto> getPatientMedicalExaminations(Patient patient){
        return patient.getMedicalExaminations().stream().map(me -> MedicalExaminationDto.builder()
                .examinationId(me.getId())
                .patientBrief(PatientBriefDto.builder()
                        .patientId(patient.getId())
                        .firstName(patient.getFirstName())
                        .lastName(patient.getLastName())
                        .build())
                .institution(institutionService.mapToDto(me.getInstitution()))
                .employeeId(me.getEmployee().getId())
                .examinationDate(me.getExaminationDate())
                .details(me.getDetails())
                .build()).toList();
    }

    public MedicalExaminationDto getMedicalExaminationWithCorrectRequest(Long id, LocalDate date) {
        if (id == null || date == null)
            throw new NullArgumentException();
        MedicalExamination medicalExamination = getMedicalExamination(id);
        if (!medicalExamination.getPatient().getBirthdate().isEqual(date))
            throw new MedicalExaminationCoreException(HttpStatus.CONFLICT, "Login credentials doesn't match");
        return mapToDto(medicalExamination);
    }

    public MedicalExamination addMedicalExamination(MedicalExaminationCreationDto medicalExaminationCreation, Employee employee) {
        if (medicalExaminationCreation == null || medicalExaminationCreation.getPesel() == null
                || medicalExaminationCreation.getExaminationDate() == null
                || medicalExaminationCreation.getDetails() == null)
            throw new NullArgumentException();

        Institution institution = (employee.getPermissionLevel() == 1 || medicalExaminationCreation.getInstitutionId() == null)
                ? employee.getInstitution() : institutionService.getInstitution(medicalExaminationCreation.getInstitutionId());

        Patient patient = patientService.getPatientByPESEL(medicalExaminationCreation.getPesel());

        return medicalExaminationRepository.save(MedicalExamination.builder()
                .patient(patient)
                .institution(institution)
                .employee(employee)
                .examinationDate(medicalExaminationCreation.getExaminationDate())
                .details(medicalExaminationCreation.getDetails())
                .build());
    }

    public MedicalExamination getMedicalExamination(Long id) {
        Optional<MedicalExamination> me = medicalExaminationRepository.findMedicalExaminationById(id);
        if (me.isEmpty())
            throw new MedicalExaminationCoreException(HttpStatus.NOT_FOUND,"Not found medical examination with given id");
        return me.get();
    }

    protected MedicalExaminationDto mapToDto(MedicalExamination medicalExamination){
        return MedicalExaminationDto.builder()
                .examinationId(medicalExamination.getId())
                .patientBrief(PatientBriefDto.builder()
                        .patientId(medicalExamination.getPatient().getId())
                        .firstName(medicalExamination.getPatient().getFirstName())
                        .lastName(medicalExamination.getPatient().getFirstName())
                        .build())
                .institution(InstitutionDto.builder()
                        .institutionId(medicalExamination.getInstitution().getId())
                        .name(medicalExamination.getInstitution().getName())
                        .street(medicalExamination.getInstitution().getStreet())
                        .city(medicalExamination.getInstitution().getCity())
                        .zipCode(medicalExamination.getInstitution().getZipCode())
                        .build())
                .employeeId(medicalExamination.getEmployee().getId())
                .examinationDate(medicalExamination.getExaminationDate())
                .details(medicalExamination.getDetails())
                .build();
    }

    public void deleteMedicalExamination(MedicalExamination medicalExamination) {
        medicalExaminationRepository.delete(medicalExamination);
    }

    public List<MedicalExaminationDto> getMedicalExaminationsByInstitution(Institution institution) {
       return institution.getMedicalExaminations().stream().map(this::mapToDto)
               .toList();
    }
}
