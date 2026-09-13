package com.clinicalai.clinical_api.lab;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "lab_observations")
public class LabObservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clinical_record_id", nullable = false)
    private Long clinicalRecordId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "analyte_code", nullable = false)
    private String analyteCode;

    @Column(name = "analyte_name", nullable = false)
    private String analyteName;

    @Column(name = "value_numeric", nullable = false)
    private BigDecimal valueNumeric;

    @Column(nullable = false)
    private String unit;

    @Column(name = "observed_at", nullable = false)
    private OffsetDateTime observedAt;

    public Long getId() {
        return id;
    }

    public Long getClinicalRecordId() {
        return clinicalRecordId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public String getAnalyteCode() {
        return analyteCode;
    }

    public String getAnalyteName() {
        return analyteName;
    }

    public BigDecimal getValueNumeric() {
        return valueNumeric;
    }

    public String getUnit() {
        return unit;
    }

    public OffsetDateTime getObservedAt() {
        return observedAt;
    }
}
