package com.clinicalai.clinical_api.lab;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LabAnalyticsService {

    private final LabReferenceRangeRepository referenceRangeRepository;

    public LabAnalyticsService(LabReferenceRangeRepository referenceRangeRepository) {
        this.referenceRangeRepository = referenceRangeRepository;
    }

    public LabFlag calculateFlag(String analyteCode, String unit, BigDecimal value) {
        LabReferenceRange range = referenceRangeRepository
                .findByAnalyteCodeAndUnit(analyteCode, unit)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No reference range found for " + analyteCode + " " + unit));

        if (range.getLowerValue() != null && value.compareTo(range.getLowerValue()) < 0) {
            return LabFlag.LOW;
        }

        if (value.compareTo(range.getUpperValue()) > 0) {
            return LabFlag.HIGH;
        }

        return LabFlag.NORMAL;
    }
}
