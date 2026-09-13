CREATE TABLE lab_reference_ranges (
    id BIGSERIAL PRIMARY KEY,
    analyte_code VARCHAR(50) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    lower_value NUMERIC(12,4),
    upper_value NUMERIC(12,4) NOT NULL
);

CREATE UNIQUE INDEX uq_lab_reference_range
    ON lab_reference_ranges (analyte_code, unit);

INSERT INTO lab_reference_ranges
    (analyte_code, unit, lower_value, upper_value)
VALUES
    ('HGB', 'g/dL', 12.0, 17.5),
    ('WBC', 'x10^9/L', 4.0, 11.0),
    ('PLT', 'x10^9/L', 150.0, 450.0),
    ('GLUCOSE', 'mg/dL', 70.0, 99.0),
    ('CREATININE', 'mg/dL', 0.6, 1.3),
    ('SODIUM', 'mmol/L', 135.0, 145.0),
    ('TOTAL_CHOLESTEROL', 'mg/dL', 0.0, 200.0),
    ('LDL', 'mg/dL', 0.0, 100.0),
    ('HDL', 'mg/dL', 40.0, 100.0),
    ('TSH', 'mIU/L', 0.4, 4.0),
    ('FREE_T4', 'ng/dL', 0.8, 1.8),
    ('POTASSIUM', 'mmol/L', 3.5, 5.1);
