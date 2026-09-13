CREATE TABLE audit_events (
    id BIGSERIAL PRIMARY KEY,
    actor_id VARCHAR(255) NOT NULL,
    action VARCHAR(100) NOT NULL,
    patient_id BIGINT REFERENCES patients(id),
    occurred_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_events_patient
    ON audit_events (patient_id);

CREATE INDEX idx_audit_events_occurred_at
    ON audit_events (occurred_at DESC);
