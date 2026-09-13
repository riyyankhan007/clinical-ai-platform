package com.clinicalai.clinical_api.patient;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "clinical_records")
public class ClinicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "record_type", nullable = false)
    private String recordType;

    @Column(name = "record_date", nullable = false)
    private OffsetDateTime recordDate;

    @Column(name = "source_system")
    private String sourceSystem;

    @Column(name = "source_record_id")
    private String sourceRecordId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    public Long getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getRecordType() {
        return recordType;
    }

    public OffsetDateTime getRecordDate() {
        return recordDate;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public String getSourceRecordId() {
        return sourceRecordId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
