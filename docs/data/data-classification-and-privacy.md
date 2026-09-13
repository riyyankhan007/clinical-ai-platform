# AI Clinical Intelligence Platform

## Data Classification & Privacy Requirements

**Document:** Data Classification & Privacy Requirements
**Version:** 1.0
**Status:** Approved for Development
**Scope:** MVP and future production architecture

---

## 1. Purpose

This document defines how information handled by the AI Clinical Intelligence Platform should be classified, protected, accessed, logged, stored, and processed.

The platform is designed for clinical decision support and may process highly sensitive patient information.

The system therefore follows the principle:

> **Collect the minimum data required, give the minimum access required, and expose sensitive data only when necessary.**

---

## 2. Data Classification Levels

The platform uses four data classification levels.

| Classification   | Description                                                 | Examples                                                          |
| ---------------- | ----------------------------------------------------------- | ----------------------------------------------------------------- |
| PUBLIC           | Information that can be freely shared                       | Public medical knowledge, public documentation                    |
| INTERNAL         | Non-public application information with limited sensitivity | Application configuration, internal documentation                 |
| SENSITIVE        | Information that should not be publicly exposed             | User information, operational data, audit metadata                |
| HIGHLY_SENSITIVE | Clinical or personally identifiable patient information     | Patient records, diagnoses, lab results, medical reports, imaging |

---

## 3. Public Data

### Examples

* Public medical literature
* Public clinical guidelines
* Public documentation
* Public datasets where permitted
* General application documentation

### Requirements

Public data may be:

* Stored in the system
* Used for RAG
* Included in documentation
* Shared publicly when its original license permits it

Licensing and usage restrictions must always be respected.

---

## 4. Internal Data

Internal data includes information required to operate and develop the platform but which is not intended for public disclosure.

### Examples

* Internal architecture documentation
* Non-sensitive configuration
* Development notes
* Internal service metadata

### Requirements

Internal information should:

* Require authenticated access where appropriate
* Not be unnecessarily exposed publicly
* Not contain patient-identifiable information
* Not contain secrets or credentials

---

## 5. Sensitive Data

Sensitive data includes information that could create security or privacy risks if improperly exposed.

### Examples

* User account information
* Authentication metadata
* Authorization information
* Audit metadata
* Internal operational information

### Requirements

Sensitive data must:

* Require authenticated access
* Follow authorization rules
* Be protected during transmission
* Be protected at rest where appropriate
* Be excluded from unnecessary application logs

Secrets such as passwords, API keys, tokens, and private credentials must never be stored in source code.

---

## 6. Highly Sensitive Data

Highly sensitive data includes patient clinical information and personally identifiable information.

### Examples

* Patient name
* Patient identifier
* Date of birth
* Contact information
* Medical history
* Diagnoses
* Medications
* Laboratory results
* Clinical reports
* Doctor notes
* Imaging
* AI analysis associated with a patient
* Other information that can identify or describe a patient

### Requirements

Highly sensitive data must:

* Require authentication
* Require explicit authorization
* Follow least-privilege access
* Be protected in transit
* Be protected at rest in production
* Be audited when accessed through sensitive workflows
* Never be exposed through public endpoints
* Never be unnecessarily written to logs
* Never be committed to Git
* Never be placed in source code

---

## 7. Development Data Policy

The MVP development environment must use:

* Synthetic data
* Properly de-identified data
* Public datasets with appropriate usage permissions

The preferred development approach is synthetic data.

Example:

```text
Patient ID: PAT-000123
Name: Synthetic Patient
Age: 54
```

The project must not use real identifiable patient information for development unless all applicable organizational, legal, security, and compliance requirements have been satisfied.

---

## 8. Git Repository Rules

The Git repository must never contain:

* Real patient records
* Patient names
* Patient contact information
* Medical reports containing identifiable information
* Real medical images containing identifying metadata
* Passwords
* API keys
* Access tokens
* Private certificates
* Database credentials
* Cloud credentials

The project should maintain an appropriate `.gitignore` file as development progresses.

Example:

```text
.env
*.pem
*.key
secrets/
credentials/
```

---

## 9. Logging Requirements

Application logs must be designed to avoid exposing highly sensitive information.

### Logs should NOT contain:

* Patient names
* Patient medical history
* Full clinical reports
* Raw laboratory reports
* Medical images
* Authentication tokens
* Passwords
* API keys
* Full AI prompts containing patient information

### Logs MAY contain:

* Request ID
* Service name
* Timestamp
* Operation name
* Execution duration
* Success/failure status
* Non-sensitive error codes
* Audit event identifiers

Example:

```text
requestId=abc123
operation=CLINICAL_ANALYSIS
patientId=PAT-000123
status=SUCCESS
durationMs=842
```

Even identifiers should be handled carefully in production and should follow the organization's privacy requirements.

---

## 10. AI / LLM Data Boundary

Patient information must not be sent to an external AI provider without an approved data-processing and security model.

The application must explicitly control what information is sent to AI services.

The AI system should receive only the minimum information necessary to perform the requested task.

Example:

```text
Doctor
  ↓
Clinical API
  ↓
Authorization
  ↓
Retrieve required patient information
  ↓
Minimize / prepare context
  ↓
AI Orchestrator
  ↓
LLM / AI Model
```

The AI model must not be treated as an authorization boundary.

Authorization must happen before data reaches the AI layer.

---

## 11. Patient Data Minimization

The system should avoid retrieving or sending unrelated patient information.

For example, if the doctor asks about a patient's recent laboratory trend, the system should prioritize:

* Relevant laboratory results
* Relevant historical results
* Relevant clinical context

It should not automatically send the patient's entire medical record to every AI request.

This reduces:

* Privacy exposure
* Token usage
* Processing cost
* Irrelevant context
* Potential hallucination

---

## 12. Encryption Requirements

### Data in Transit

Production communication involving sensitive data must use encrypted transport such as HTTPS/TLS.

This applies to:

* Browser → API
* API → database where applicable
* API → AI services
* Service → service communication
* External integrations

### Data at Rest

Production storage containing highly sensitive data should use appropriate encryption mechanisms.

This includes:

* Databases
* Object storage
* Backups
* Medical imaging files
* Clinical documents

Exact encryption implementation will be defined during the infrastructure and security stages.

---

## 13. Authentication

All access to protected clinical resources requires authentication.

Unauthenticated requests to protected resources must be rejected.

Authentication and authorization are separate concepts:

```text
Authentication
"Who are you?"

Authorization
"What are you allowed to access?"
```

Both must be enforced.

---

## 14. Authorization

Clinical data access must follow the rules defined in:

`docs/security/roles-and-access.md`

Authorization must be checked before retrieving protected patient information.

The system must follow:

> **Deny by default.**

A valid login alone does not provide access to all patient information.

---

## 15. Audit Requirements

Sensitive operations should generate audit events.

Examples:

* Patient record accessed
* Laboratory information accessed
* Clinical document accessed
* Imaging accessed
* AI analysis requested
* AI analysis viewed
* Patient data exported
* User permissions changed
* Knowledge source changed

Audit events should provide enough information to determine:

* Who performed the action
* What action occurred
* When it occurred
* Which resource was affected
* Whether the action succeeded or failed

Audit logs should not unnecessarily contain the underlying clinical data.

---

## 16. Data Retention

The MVP will not define arbitrary permanent retention of clinical information.

Retention periods should be explicitly defined according to:

* Business requirements
* Security requirements
* Applicable law
* Organizational policy
* Data source requirements

The application should support eventual data lifecycle management rather than assuming that all data should be retained forever.

---

## 17. Data Deletion

Where applicable, the system should support controlled deletion or expiration of data.

Deletion workflows must:

* Require appropriate authorization
* Be auditable
* Avoid accidental deletion
* Consider backups and replicas
* Follow applicable retention requirements

Deletion must not be implemented as an unrestricted user action.

---

## 18. Synthetic Data Strategy

The development environment should contain realistic synthetic patient scenarios.

Examples should include:

* Normal patients
* Patients with abnormal laboratory values
* Patients with changing laboratory trends
* Patients with multiple conditions
* Patients with incomplete records
* Conflicting information
* Missing information
* Potential red-flag scenarios

Synthetic data should be clearly identifiable as synthetic.

Example:

```text
PAT-000001
PAT-000002
PAT-000003
```

No real-world identity should be inferred from synthetic records.

---

## 19. Medical Imaging Data

Medical images require additional protection.

Development imaging data must be:

* Synthetic where possible
* Properly de-identified
* From datasets with permitted usage
* Stored securely
* Excluded from public repositories unless explicitly permitted

Image metadata should be checked for potentially identifying information before use.

---

## 20. Backup & Recovery

Production backups containing highly sensitive information must receive protections equivalent to the primary data.

Backups should:

* Be access-controlled
* Be encrypted where appropriate
* Have defined retention periods
* Be monitored
* Be tested for recovery

Backup access should be auditable.

---

## 21. Third-Party Services

Before sending sensitive information to a third-party service, the project must verify:

* What data is transmitted
* Why it is transmitted
* Where it is processed
* How it is stored
* Whether the service retains the data
* Applicable contractual requirements
* Applicable privacy/security requirements

No third-party AI, analytics, storage, or monitoring service should receive patient data simply because an API is convenient.

---

## 22. Privacy by Design

Privacy must be considered during system design rather than added after implementation.

The system should follow:

1. Data minimization
2. Least privilege
3. Secure defaults
4. Explicit authorization
5. Encryption
6. Auditability
7. Controlled retention
8. Safe logging
9. Controlled AI data exposure
10. Separation of environments

---

## 23. Environment Separation

The project should maintain clear separation between:

```text
Development
Testing
Staging
Production
```

Development and testing environments should use synthetic or appropriately de-identified data.

Production credentials must never be reused in development.

Production patient data must never be copied into development environments without an approved process.

---

## 24. Compliance Boundary

This project is initially a software engineering and AI research/development project.

The MVP must **not claim healthcare regulatory compliance merely because security controls have been implemented**.

Before real-world clinical deployment, applicable requirements must be formally assessed, including relevant:

* Privacy regulations
* Healthcare regulations
* Security requirements
* Data-processing agreements
* Organizational policies
* Medical-device/software requirements where applicable

Compliance will be treated as a dedicated future workstream.

---

## 25. Core Privacy Principle

The platform should follow this principle:

> **The system should know only what it needs, show only what the user is authorized to see, send only what the AI needs, and log only what is necessary for security and operations.**

---

## 26. S0-T03 Acceptance Criteria

S0-T03 is complete when:

* Data classification levels are defined.
* Highly sensitive clinical data is explicitly identified.
* Development data requirements are defined.
* Git repository data restrictions are defined.
* Logging restrictions are defined.
* AI/LLM data boundaries are defined.
* Data minimization principles are defined.
* Encryption requirements are documented.
* Authentication and authorization requirements are documented.
* Audit requirements are documented.
* Data retention and deletion principles are documented.
* Medical imaging data requirements are documented.
* Third-party data-sharing requirements are documented.
* Environment separation requirements are documented.
* Compliance boundaries are explicitly stated.

**Status: DONE**
