package com.clinicalai.clinical_api.patient;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClinicalRecordRepository extends JpaRepository<ClinicalRecord, Long> {

    List<ClinicalRecord> findByPatientIdOrderByRecordDateDesc(Long patientId);
}
