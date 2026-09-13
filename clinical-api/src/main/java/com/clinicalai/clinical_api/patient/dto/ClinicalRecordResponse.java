package com.clinicalai.clinical_api.patient.dto;

import java.time.OffsetDateTime;

public record ClinicalRecordResponse(
        Long id,
        String recordType,
        OffsetDateTime recordDate,
        String title,
        String content
) {
}
