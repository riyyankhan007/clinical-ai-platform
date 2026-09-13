package com.clinicalai.clinical_api.lab;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LabReferenceRangeRepository
        extends JpaRepository<LabReferenceRange, Long> {

    Optional<LabReferenceRange> findByAnalyteCodeAndUnit(
            String analyteCode,
            String unit
    );
}
