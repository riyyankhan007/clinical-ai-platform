package com.clinicalai.clinical_api.patient;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByPatientIdentifierContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String patientIdentifier,
            String firstName,
            String lastName
    );
}
