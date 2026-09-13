package com.clinicalai.clinical_api.lab;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabObservationRepository
        extends JpaRepository<LabObservation, Long> {

    List<LabObservation> findByPatientIdAndAnalyteCodeOrderByObservedAtAsc(
            Long patientId,
            String analyteCode
    );
}
