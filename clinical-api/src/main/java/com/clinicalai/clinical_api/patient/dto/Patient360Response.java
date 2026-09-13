package com.clinicalai.clinical_api.patient.dto;

import java.time.LocalDate;
import java.util.List;

public record Patient360Response(
        Long id,
        String patientIdentifier,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String gender,
        List<ClinicalRecordResponse> records
) {
}
