package com.clinicalai.clinical_api.lab;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LabAnalyticsServiceTest {

    @Test
    void calculatesLowNormalAndHighFlags() {
        LabReferenceRangeRepository repository = mock(LabReferenceRangeRepository.class);

        LabReferenceRange range = mock(LabReferenceRange.class);

        when(range.getLowerValue()).thenReturn(new BigDecimal("70"));
        when(range.getUpperValue()).thenReturn(new BigDecimal("99"));

        when(repository.findByAnalyteCodeAndUnit("GLUCOSE", "mg/dL"))
                .thenReturn(Optional.of(range));

        LabAnalyticsService service = new LabAnalyticsService(repository);

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
}
