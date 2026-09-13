CREATE TABLE patients (
    id BIGSERIAL PRIMARY KEY,
    patient_identifier VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(30),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_patients_name
    ON patients (last_name, first_name);

CREATE TABLE clinical_records (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL REFERENCES patients(id),
    record_type VARCHAR(50) NOT NULL,
    record_date TIMESTAMPTZ NOT NULL,
    source_system VARCHAR(100),
    source_record_id VARCHAR(100),
    title VARCHAR(255),
    content TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_clinical_records_patient
    ON clinical_records (patient_id);

CREATE INDEX idx_clinical_records_patient_date
    ON clinical_records (patient_id, record_date DESC);