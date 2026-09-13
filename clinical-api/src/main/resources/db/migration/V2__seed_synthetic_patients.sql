-- Synthetic patients for local development/testing only.

INSERT INTO patients
    (patient_identifier, first_name, last_name, date_of_birth, gender)
VALUES
    ('SYN-0001', 'Aarav', 'Sharma', '1988-04-12', 'Male'),
    ('SYN-0002', 'Maya', 'Patel', '1995-09-23', 'Female'),
    ('SYN-0003', 'Daniel', 'Wilson', '1972-01-18', 'Male'),
    ('SYN-0004', 'Ananya', 'Rao', '2001-07-05', 'Female'),
    ('SYN-0005', 'Liam', 'Johnson', '1965-11-30', 'Male');

INSERT INTO clinical_records
    (patient_id, record_type, record_date, source_system,
     source_record_id, title, content)
SELECT
    p.id,
    r.record_type,
    r.record_date::timestamptz,
    r.source_system,
    r.source_record_id,
    r.title,
    r.content
FROM patients p
JOIN (
    VALUES
    ('SYN-0001', 'LAB', '2026-08-10 09:00:00+05:30',
     'Synthetic Lab System', 'LAB-SYN-0001-001',
     'Complete Blood Count',
     'Hemoglobin 14.2 g/dL. WBC 6.8 x10^9/L. Platelets 245 x10^9/L.'),

    ('SYN-0001', 'VISIT', '2026-08-15 14:30:00+05:30',
     'Synthetic EHR', 'VISIT-SYN-0001-001',
     'Routine Follow-up',
     'Patient reports intermittent fatigue. No acute symptoms reported.'),

    ('SYN-0002', 'LAB', '2026-08-12 10:15:00+05:30',
     'Synthetic Lab System', 'LAB-SYN-0002-001',
     'Metabolic Panel',
     'Glucose 102 mg/dL. Creatinine 0.8 mg/dL. Sodium 139 mmol/L.'),

    ('SYN-0002', 'VISIT', '2026-08-20 11:00:00+05:30',
     'Synthetic EHR', 'VISIT-SYN-0002-001',
     'Primary Care Visit',
     'Patient reports occasional headaches. No neurological deficits documented.'),

    ('SYN-0003', 'LAB', '2026-08-14 08:45:00+05:30',
     'Synthetic Lab System', 'LAB-SYN-0003-001',
     'Lipid Panel',
     'Total cholesterol 218 mg/dL. LDL 142 mg/dL. HDL 48 mg/dL.'),

    ('SYN-0003', 'IMAGING', '2026-08-18 16:00:00+05:30',
     'Synthetic Imaging System', 'IMG-SYN-0003-001',
     'Chest X-Ray',
     'Synthetic imaging record placeholder. No real patient image data.'),

    ('SYN-0004', 'LAB', '2026-08-16 09:30:00+05:30',
     'Synthetic Lab System', 'LAB-SYN-0004-001',
     'Thyroid Panel',
     'TSH 2.1 mIU/L. Free T4 1.2 ng/dL.'),

    ('SYN-0004', 'VISIT', '2026-08-22 13:15:00+05:30',
     'Synthetic EHR', 'VISIT-SYN-0004-001',
     'General Consultation',
     'Patient reports difficulty sleeping over the previous two weeks.'),

    ('SYN-0005', 'LAB', '2026-08-11 07:50:00+05:30',
     'Synthetic Lab System', 'LAB-SYN-0005-001',
     'Basic Metabolic Panel',
     'Glucose 118 mg/dL. Creatinine 1.0 mg/dL. Potassium 4.3 mmol/L.'),

    ('SYN-0005', 'VISIT', '2026-08-25 15:45:00+05:30',
     'Synthetic EHR', 'VISIT-SYN-0005-001',
     'Chronic Care Follow-up',
     'Patient reports reduced exercise tolerance. Further clinical evaluation documented.')
) AS r(
    patient_identifier,
    record_type,
    record_date,
    source_system,
    source_record_id,
    title,
    content
)
ON p.patient_identifier = r.patient_identifier;