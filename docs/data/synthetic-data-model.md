# AI Clinical Intelligence Platform

## Synthetic Patient Data Model & Seed Strategy

**Document:** Synthetic Patient Data Model & Seed Strategy
**Version:** 1.0
**Status:** Approved for Development
**Scope:** MVP development and testing

---

## 1. Purpose

This document defines the structure, relationships, scenarios, and generation strategy for synthetic clinical data used by the AI Clinical Intelligence Platform.

The purpose of the synthetic dataset is to provide realistic development and testing data without using real patient-identifiable information.

The dataset must support:

* Patient 360
* Laboratory trend analysis
* Clinical RAG
* Clinical agent workflows
* Authorization testing
* Audit testing
* AI evaluation
* Future imaging workflows

---

## 2. Synthetic Data Principle

All development data must be clearly synthetic.

Synthetic data must not represent real individuals.

Example:

```text
Patient ID: PAT-000001
Name: Alex Morgan
```

The generated identities must not intentionally correspond to real patients.

Synthetic records should be realistic enough to exercise application logic and AI workflows.

---

## 3. Core Entities

The initial data model contains:

```text
Patient
   │
   ├── Encounter
   │
   ├── Condition
   │
   ├── Medication
   │
   ├── Lab Result
   │
   ├── Clinical Document
   │
   └── Imaging Study
```

Additional supporting entities may be introduced during implementation.

---

## 4. Patient

The Patient entity represents the person whose clinical information is stored in the platform.

Initial fields:

```text
patientId
firstName
lastName
dateOfBirth
gender
contactInformation
createdAt
updatedAt
```

Example:

```json
{
  "patientId": "PAT-000001",
  "firstName": "Alex",
  "lastName": "Morgan",
  "dateOfBirth": "1972-04-15",
  "gender": "MALE"
}
```

The exact database schema will be finalized during the clinical-data implementation stage.

---

## 5. Encounter

An Encounter represents a clinical interaction involving the patient.

Examples:

* Outpatient consultation
* Emergency visit
* Follow-up appointment
* Hospital admission
* Discharge

Initial fields:

```text
encounterId
patientId
encounterDate
encounterType
department
reason
clinicalSummary
```

Example:

```text
ENC-000001
Patient: PAT-000001
Date: 2026-09-01
Type: OUTPATIENT
Department: CARDIOLOGY
Reason: Fatigue
```

---

## 6. Conditions

Conditions represent diagnoses or clinically relevant conditions recorded for a patient.

Initial fields:

```text
conditionId
patientId
name
status
onsetDate
recordedAt
```

Possible statuses:

```text
ACTIVE
RESOLVED
HISTORICAL
```

The synthetic dataset should include patients with both single and multiple conditions.

---

## 7. Medications

Medication records represent medications associated with a patient.

Initial fields:

```text
medicationId
patientId
name
dose
frequency
startDate
endDate
status
```

Possible statuses:

```text
ACTIVE
DISCONTINUED
COMPLETED
```

Medication data should be synthetic and should support longitudinal scenarios.

---

## 8. Laboratory Results

Laboratory results are one of the most important data types for the MVP.

Initial fields:

```text
labResultId
patientId
encounterId
testName
value
unit
referenceRangeLow
referenceRangeHigh
status
observedAt
```

Example:

```text
LAB-000001
Patient: PAT-000001
Test: Hemoglobin
Value: 10.8
Unit: g/dL
Reference Range: 13.0 - 17.0
Status: ABNORMAL
Date: 2026-09-01
```

Laboratory data must support repeated measurements so that longitudinal trends can be analyzed.

---

## 9. Clinical Documents

Clinical documents represent reports or notes associated with a patient.

Examples:

* Consultation notes
* Discharge summaries
* Laboratory reports
* Specialist reports
* Procedure reports

Initial fields:

```text
documentId
patientId
encounterId
documentType
title
content
createdAt
authorId
```

Documents should support retrieval through the RAG pipeline where appropriate.

---

## 10. Imaging Studies

Imaging studies represent medical imaging records.

Initial fields:

```text
imagingStudyId
patientId
encounterId
modality
bodyPart
studyDate
storageReference
status
```

Examples of modalities:

```text
XRAY
CT
MRI
ULTRASOUND
```

The initial MVP does not require advanced image analysis.

The data model should nevertheless allow imaging to be added later without redesigning the entire patient model.

---

## 11. Entity Relationships

The primary relationship model is:

```text
Patient
  │
  ├── 1:N → Encounter
  │
  ├── 1:N → Condition
  │
  ├── 1:N → Medication
  │
  ├── 1:N → LabResult
  │
  ├── 1:N → ClinicalDocument
  │
  └── 1:N → ImagingStudy
```

An Encounter may also be associated with multiple:

```text
LabResults
ClinicalDocuments
ImagingStudies
```

---

## 12. Identifier Strategy

All synthetic entities should use non-real identifiers.

Examples:

```text
PAT-000001
ENC-000001
LAB-000001
DOC-000001
IMG-000001
COND-000001
MED-000001
```

Identifiers should be unique within their entity type.

The application must not use names as primary identifiers.

---

## 13. Synthetic Patient Scenarios

The seed dataset should contain deliberately designed clinical scenarios.

### Scenario 1 — Healthy Baseline

Patient with:

* No significant active condition
* Normal laboratory results
* Stable history
* Routine encounters

Purpose:

Test that the AI does not invent abnormalities.

---

### Scenario 2 — Gradually Worsening Laboratory Trend

Patient with several laboratory measurements showing gradual deterioration.

Example:

```text
Date       Hemoglobin
01-Jan     13.8
01-Mar     12.9
01-Jun     11.8
01-Sep     10.8
```

Purpose:

Test longitudinal trend detection.

---

### Scenario 3 — Sudden Abnormal Result

Patient has mostly normal historical results followed by a significant abnormal result.

Purpose:

Test detection of sudden changes.

---

### Scenario 4 — Multiple Conditions

Patient has several active conditions and medications.

Purpose:

Test whether the system can prioritize relevant information rather than overwhelming the doctor with every record.

---

### Scenario 5 — Incomplete Information

Patient record intentionally contains missing information.

Examples:

* Missing medication history
* Missing recent laboratory result
* Missing clinical note

Purpose:

Test whether the AI identifies missing information instead of assuming it exists.

---

### Scenario 6 — Conflicting Records

Different records contain inconsistent information.

Example:

```text
Document A:
Medication = Drug A

Document B:
Medication = Drug B
```

Purpose:

Test whether the system identifies conflicting evidence.

The AI should not silently choose one as fact.

---

### Scenario 7 — Potential Red Flag

Patient contains a combination of synthetic findings that should cause the system to highlight information requiring clinician attention.

Purpose:

Test red-flag identification and safety behavior.

The AI should present this as a clinical consideration requiring professional review, not as an autonomous diagnosis.

---

### Scenario 8 — Normal Values, Concerning Trend

Individual laboratory values may remain within reference ranges while changing significantly relative to the patient's previous baseline.

Purpose:

Test patient-specific baseline analysis.

---

## 14. Seed Dataset Size

The initial seed dataset should be intentionally small.

Target:

```text
20–50 synthetic patients
```

This is sufficient for the initial MVP.

The dataset can later be expanded to:

```text
100+
1,000+
10,000+
```

for performance and evaluation testing.

Do not optimize for massive datasets before the basic clinical workflows work correctly.

---

## 15. Distribution of Seed Scenarios

The initial dataset should contain a mixture of scenarios.

Example target:

```text
Healthy baseline                  5
Worsening laboratory trend        5
Sudden abnormality                5
Multiple conditions               5
Incomplete information            5
Conflicting records               5
Potential red flag                5
Normal but concerning trend       5
```

This produces approximately:

```text
40 synthetic patients
```

The exact number can change during implementation.

---

## 16. Data Consistency Rules

Synthetic data must maintain realistic relationships.

Examples:

* Every LabResult must reference an existing Patient.
* Every Encounter must reference an existing Patient.
* Every ClinicalDocument must reference an existing Patient.
* Medication dates should be logically consistent.
* Encounter dates should not occur before patient birth.
* Laboratory observations should have valid units.
* Reference ranges should correspond to the test.
* Historical results should have earlier timestamps than later results.
* Imaging studies must reference an existing patient.
* Foreign-key relationships must remain valid.

---

## 17. Longitudinal Data Requirement

Patients should have multiple historical records rather than only one current snapshot.

For selected patients, include:

```text
Multiple encounters
Multiple lab measurements
Multiple documents
Medication changes
Condition changes
```

This is required to demonstrate the platform's ability to reason over patient history.

---

## 18. Lab Trend Data

For selected laboratory tests, generate repeated measurements.

Example:

```text
Patient: PAT-000010

Creatinine

2026-01-01 → 0.9 mg/dL
2026-03-01 → 1.0 mg/dL
2026-06-01 → 1.2 mg/dL
2026-09-01 → 1.5 mg/dL
```

The platform should eventually calculate:

* Absolute change
* Percentage change
* Direction of trend
* Rate of change
* Reference-range status
* Patient baseline deviation

These calculations should be performed deterministically by application logic.

---

## 19. Missing Data

Some synthetic patients should intentionally contain missing data.

Examples:

```text
No recent labs
No medication history
Missing encounter summary
Missing specialist report
```

The system should treat missing data as:

```text
UNKNOWN
```

not:

```text
NO
```

For example:

```text
No medication record
```

must not automatically become:

```text
Patient takes no medication
```

---

## 20. Conflicting Data

Synthetic data should include controlled conflicts for testing.

Example:

```text
Medication record:
Medication A

Clinical document:
Medication B
```

The platform should surface the conflict and indicate that the information requires verification.

It must not silently overwrite one source with another.

---

## 21. Data Generation

Synthetic data should eventually be generated using a deterministic seed process.

The seed process should support:

* Reproducible datasets
* Fixed random seed
* Scenario selection
* Configurable patient count
* Configurable historical record count

Example conceptual command:

```text
generate synthetic data
--patients 40
--seed 42
```

The exact implementation will be decided later.

---

## 22. Reproducibility

Given the same generation configuration and seed, the system should produce the same dataset.

Example:

```text
Seed = 42
Patients = 40
```

Running the generator again with the same parameters should produce equivalent data.

This is important for:

* Testing
* Debugging
* Evaluation
* Regression testing

---

## 23. Synthetic Data Versioning

Seed datasets should be versioned.

Example:

```text
dataset-v1
dataset-v2
dataset-v3
```

Changes to the synthetic dataset should be documented so that evaluation results remain reproducible.

---

## 24. Data Safety

Synthetic data must remain clearly separated from real clinical data.

The project must never assume:

> "It's only development data, so security doesn't matter."

Development data should still follow the privacy and repository rules defined in:

`docs/data/data-classification-and-privacy.md`

---

## 25. Future Data Types

The platform may eventually support:

* Procedures
* Allergies
* Vital signs
* Pathology
* Microbiology
* Claims information
* FHIR resources
* Genomic data
* Additional imaging metadata

These are intentionally outside the initial seed-model scope unless required by a later MVP task.

---

## 26. S0-T05 Acceptance Criteria

S0-T05 is complete when:

* Core synthetic entities are defined.
* Patient relationships are defined.
* Identifier strategy is defined.
* Laboratory structure is defined.
* Clinical document structure is defined.
* Imaging structure is defined.
* Longitudinal data requirements are defined.
* Synthetic clinical scenarios are defined.
* Missing-data scenarios are defined.
* Conflicting-data scenarios are defined.
* Data consistency rules are defined.
* Seed dataset size is defined.
* Reproducibility requirements are defined.
* Dataset versioning requirements are defined.
* Synthetic-data safety requirements are defined.
* Future data types are documented without expanding MVP scope.

**Status: DONE**
