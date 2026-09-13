package com.clinicalai.clinical_api.patient;

import com.clinicalai.clinical_api.patient.dto.ClinicalRecordResponse;
import com.clinicalai.clinical_api.patient.dto.Patient360Response;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final ClinicalRecordRepository clinicalRecordRepository;

    public PatientService(
            PatientRepository patientRepository,
            ClinicalRecordRepository clinicalRecordRepository) {
        this.patientRepository = patientRepository;
        this.clinicalRecordRepository = clinicalRecordRepository;
    }

    public List<Patient> searchPatients(String query) {
        return patientRepository
                .findByPatientIdentifierContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        query, query, query);
    }

    public Patient360Response getPatient360(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        List<ClinicalRecordResponse> records = clinicalRecordRepository
                .findByPatientIdOrderByRecordDateDesc(patientId)
                .stream()
                .map(record -> new ClinicalRecordResponse(
                        record.getId(),
                        record.getRecordType(),
                        record.getRecordDate(),
                        record.getSourceSystem(),
                        record.getSourceRecordId(),
                        record.getTitle(),
                        record.getContent()
                ))
                .toList();

        return new Patient360Response(
                patient.getId(),
                patient.getPatientIdentifier(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDateOfBirth(),
                patient.getGender(),
                records
        );
    }
}
