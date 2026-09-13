CREATE TABLE lab_observations (
    id BIGSERIAL PRIMARY KEY,
    clinical_record_id BIGINT NOT NULL REFERENCES clinical_records(id),
    patient_id BIGINT NOT NULL REFERENCES patients(id),
    analyte_code VARCHAR(50) NOT NULL,
    analyte_name VARCHAR(100) NOT NULL,
    value_numeric NUMERIC(12,4) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    observed_at TIMESTAMPTZ NOT NULL,
    source_system VARCHAR(100),
    source_record_id VARCHAR(100)
);

CREATE INDEX idx_lab_observations_patient
    ON lab_observations (patient_id);

CREATE INDEX idx_lab_observations_patient_analyte
    ON lab_observations (patient_id, analyte_code);

CREATE INDEX idx_lab_observations_observed_at
    ON lab_observations (observed_at DESC);

INSERT INTO lab_observations
    (clinical_record_id, patient_id, analyte_code, analyte_name,
     value_numeric, unit, observed_at, source_system, source_record_id)
SELECT
    r.id,
    r.patient_id,
    o.analyte_code,
    o.analyte_name,
    o.value_numeric,
    o.unit,
    r.record_date,
    r.source_system,
    r.source_record_id
FROM clinical_records r
JOIN (
    VALUES
    ('LAB-SYN-0001-001', 'HGB', 'Hemoglobin', 14.2, 'g/dL'),
    ('LAB-SYN-0001-001', 'WBC', 'White Blood Cell Count', 6.8, 'x10^9/L'),
    ('LAB-SYN-0001-001', 'PLT', 'Platelet Count', 245, 'x10^9/L'),

    ('LAB-SYN-0002-001', 'GLUCOSE', 'Glucose', 102, 'mg/dL'),
    ('LAB-SYN-0002-001', 'CREATININE', 'Creatinine', 0.8, 'mg/dL'),
    ('LAB-SYN-0002-001', 'SODIUM', 'Sodium', 139, 'mmol/L'),

    ('LAB-SYN-0003-001', 'TOTAL_CHOLESTEROL', 'Total Cholesterol', 218, 'mg/dL'),
    ('LAB-SYN-0003-001', 'LDL', 'LDL Cholesterol', 142, 'mg/dL'),
    ('LAB-SYN-0003-001', 'HDL', 'HDL Cholesterol', 48, 'mg/dL'),

    ('LAB-SYN-0004-001', 'TSH', 'TSH', 2.1, 'mIU/L'),
    ('LAB-SYN-0004-001', 'FREE_T4', 'Free T4', 1.2, 'ng/dL'),

    ('LAB-SYN-0005-001', 'GLUCOSE', 'Glucose', 118, 'mg/dL'),
    ('LAB-SYN-0005-001', 'CREATININE', 'Creatinine', 1.0, 'mg/dL'),
    ('LAB-SYN-0005-001', 'POTASSIUM', 'Potassium', 4.3, 'mmol/L')
) AS o(source_record_id, analyte_code, analyte_name, value_numeric, unit)
ON r.source_record_id = o.source_record_id
WHERE r.record_type = 'LAB';
