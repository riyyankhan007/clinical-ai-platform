package com.clinicalai.clinical_api.lab;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LabAnalyticsServiceTest {

    @Test
    void calculatesLowNormalAndHighFlags() {
        LabReferenceRangeRepository repository = mock(LabReferenceRangeRepository.class);
        LabObservationRepository observationRepository = mock(LabObservationRepository.class);

        LabReferenceRange range = mock(LabReferenceRange.class);

        when(range.getLowerValue()).thenReturn(new BigDecimal("70"));
        when(range.getUpperValue()).thenReturn(new BigDecimal("99"));

        when(repository.findByAnalyteCodeAndUnit("GLUCOSE", "mg/dL"))
                .thenReturn(Optional.of(range));

        LabAnalyticsService service =
                new LabAnalyticsService(repository, observationRepository);

        assertEquals(
                LabFlag.LOW,
                service.calculateFlag("GLUCOSE", "mg/dL", new BigDecimal("60"))
        );

        assertEquals(
                LabFlag.NORMAL,
                service.calculateFlag("GLUCOSE", "mg/dL", new BigDecimal("90"))
        );

        assertEquals(
                LabFlag.HIGH,
                service.calculateFlag("GLUCOSE", "mg/dL", new BigDecimal("110"))
        );
    }

    @Test
    void calculatesPatientBaseline() {
        LabReferenceRangeRepository repository = mock(LabReferenceRangeRepository.class);
        LabObservationRepository observationRepository = mock(LabObservationRepository.class);

        LabObservation first = mock(LabObservation.class);
        LabObservation second = mock(LabObservation.class);
        LabObservation third = mock(LabObservation.class);

        when(first.getValueNumeric()).thenReturn(new BigDecimal("100"));
        when(second.getValueNumeric()).thenReturn(new BigDecimal("110"));
        when(third.getValueNumeric()).thenReturn(new BigDecimal("120"));

        when(observationRepository
                .findByPatientIdAndAnalyteCodeOrderByObservedAtAsc(6L, "GLUCOSE"))
                .thenReturn(List.of(first, second, third));

        LabAnalyticsService service =
                new LabAnalyticsService(repository, observationRepository);

        assertEquals(
                new BigDecimal("110.0000"),
                service.calculateBaseline(6L, "GLUCOSE")
        );
    }
}
