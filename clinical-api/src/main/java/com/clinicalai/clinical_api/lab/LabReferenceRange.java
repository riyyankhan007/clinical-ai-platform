package com.clinicalai.clinical_api.lab;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "lab_reference_ranges")
public class LabReferenceRange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "analyte_code", nullable = false)
    private String analyteCode;

    @Column(nullable = false)
    private String unit;

    @Column(name = "lower_value")
    private BigDecimal lowerValue;

    @Column(name = "upper_value", nullable = false)
    private BigDecimal upperValue;

    public Long getId() {
        return id;
    }

    public String getAnalyteCode() {
        return analyteCode;
    }

    public String getUnit() {
        return unit;
    }

    public BigDecimal getLowerValue() {
        return lowerValue;
    }

    public BigDecimal getUpperValue() {
        return upperValue;
    }
}
