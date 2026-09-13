package com.clinicalai.clinical_api.lab;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class LabAnalyticsService {

    private final LabReferenceRangeRepository referenceRangeRepository;
    private final LabObservationRepository observationRepository;

    public LabAnalyticsService(
            LabReferenceRangeRepository referenceRangeRepository,
            LabObservationRepository observationRepository) {
        this.referenceRangeRepository = referenceRangeRepository;
        this.observationRepository = observationRepository;
    }

    public LabFlag calculateFlag(String analyteCode, String unit, BigDecimal value) {
        LabReferenceRange range = referenceRangeRepository
                .findByAnalyteCodeAndUnit(analyteCode, unit)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No reference range found for " + analyteCode + " " + unit));

        if (range.getLowerValue() != null
                && value.compareTo(range.getLowerValue()) < 0) {
            return LabFlag.LOW;
        }

        if (value.compareTo(range.getUpperValue()) > 0) {
            return LabFlag.HIGH;
        }

        return LabFlag.NORMAL;
    }

    public BigDecimal calculateBaseline(Long patientId, String analyteCode) {
        List<LabObservation> observations =
                observationRepository
                        .findByPatientIdAndAnalyteCodeOrderByObservedAtAsc(
                                patientId, analyteCode);

        if (observations.isEmpty()) {
            throw new IllegalArgumentException(
                    "No observations found for patient " + patientId
                            + " and analyte " + analyteCode);
        }

        BigDecimal total = observations.stream()
                .map(LabObservation::getValueNumeric)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(
                BigDecimal.valueOf(observations.size()),
                4,
                RoundingMode.HALF_UP
        );
    }

    public BigDecimal calculateAbsoluteDelta(
            BigDecimal currentValue,
            BigDecimal baseline) {

        return currentValue.subtract(baseline);
    }

    public BigDecimal calculatePercentageDelta(
            BigDecimal currentValue,
            BigDecimal baseline) {

        if (baseline.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Baseline cannot be zero");
        }

        return currentValue
                .subtract(baseline)
                .divide(baseline, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
