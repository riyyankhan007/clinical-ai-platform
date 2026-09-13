# AI Clinical Intelligence Platform

## Roles & Access Control Specification

**Document:** Roles & Access Control
**Version:** 1.0
**Status:** Approved for Development
**MVP Roles:** Doctor, Admin

---

## 1. Purpose

This document defines the roles, permissions, and access boundaries for the AI Clinical Intelligence Platform.

The authorization model follows the principle of:

> **Least privilege and deny by default.**

A user must only be able to access resources and perform actions explicitly permitted for their role and assigned scope.

---

## 2. MVP Roles

The MVP contains two application roles:

* `DOCTOR`
* `ADMIN`

Additional specialized roles may be introduced in future versions.

---

## 3. Doctor Role

### Purpose

The Doctor role is intended for clinicians who need access to authorized patient information and AI-powered clinical decision-support capabilities.

### Allowed Actions

A Doctor can:

* Authenticate into the platform
* Search for authorized patients
* View authorized patient profiles
* View patient medical history
* View laboratory results
* View clinical reports and documents
* View relevant patient records
* Request AI clinical analysis
* View AI-generated clinical insights
* View supporting evidence
* View uncertainty and safety warnings
* Provide feedback on AI-generated results

### Restricted Actions

A Doctor cannot:

* Create users
* Delete users
* Deactivate users
* Change user roles
* Modify security configuration
* Modify system-wide permissions
* Modify the approved medical knowledge corpus
* View unrestricted administrative audit logs
* Directly access the database
* Bypass authorization checks
* Use AI to directly modify source patient records
* Execute autonomous clinical actions through the AI

---

## 4. Admin Role

### Purpose

The Admin role is intended for operational and administrative management of the platform.

### Allowed Actions

An Admin can:

* Authenticate into the platform
* Create users
* Deactivate users
* Assign application roles
* Manage user access
* Manage approved knowledge sources
* Manage selected system configuration
* View audit logs
* View system health information
* View system usage information

### Clinical Data Boundary

Being an Admin does **not automatically grant unrestricted access to patient clinical information**.

Administrative privileges and clinical-data privileges must remain separate.

If an administrative workflow requires access to clinical information, that access must have an explicit authorization rule and must be audited.

---

## 5. Permission Model

The initial permission set is:

| Permission                         | Doctor | Admin |
| ---------------------------------- | -----: | ----: |
| Login                              |    YES |   YES |
| Search authorized patients         |    YES |   NO* |
| View patient records               |   YES* |   NO* |
| View laboratory results            |   YES* |   NO* |
| View clinical documents            |   YES* |   NO* |
| Request AI analysis                |    YES |    NO |
| View AI analysis                   |    YES |   NO* |
| Submit AI feedback                 |    YES |    NO |
| Manage users                       |     NO |   YES |
| Assign roles                       |     NO |   YES |
| Manage knowledge sources           |     NO |   YES |
| View audit logs                    |     NO |   YES |
| View system health                 |     NO |   YES |
| Manage system configuration        |     NO |   YES |
| Direct database access             |     NO |    NO |
| Bypass authorization               |     NO |    NO |
| Execute autonomous clinical action |     NO |    NO |

`*` Access is subject to resource-level authorization and assigned scope.

---

## 6. Patient Access Rules

Patient access must never be determined solely by the existence of a valid login.

The authorization flow should be:

```text
User
  ↓
Authentication
  ↓
Identify Role
  ↓
Check Permission
  ↓
Check Resource Access
  ↓
ALLOW / DENY
```

For example:

```text
Doctor → Patient 123
        ↓
Is user authenticated?
        ↓ YES
Does user have clinical-read permission?
        ↓ YES
Is Patient 123 within the user's authorized scope?
        ↓ YES
ALLOW
```

If any required authorization check fails:

```text
DENY
```

---

## 7. Resource-Level Authorization

RBAC alone is not sufficient for patient data.

The system should eventually support authorization based on factors such as:

* User role
* Department
* Hospital/organization
* Assigned patient
* Care team relationship
* Encounter relationship
* Other organizational access policies

Example:

```text
User:
  Role = DOCTOR
  Department = CARDIOLOGY

Patient:
  Department = CARDIOLOGY

Access:
  ALLOWED if policy permits
```

A Doctor role by itself must not imply unrestricted access to every patient.

---

## 8. Admin Access Model

The Admin role controls the platform but does not automatically control clinical data.

Separate these concepts:

```text
Administrative Permission
        ≠
Clinical Data Permission
```

Examples:

```text
MANAGE_USERS
VIEW_AUDIT_LOGS
MANAGE_KNOWLEDGE_SOURCES
```

are administrative permissions.

Whereas:

```text
VIEW_PATIENT
VIEW_LABS
VIEW_DOCUMENTS
REQUEST_CLINICAL_ANALYSIS
```

are clinical permissions.

This separation reduces the risk of excessive access.

---

## 9. AI Authorization

The AI system must never independently decide whether a user is allowed to access patient data.

Authorization must happen before patient information is provided to the AI services.

Expected flow:

```text
Doctor
  ↓
Frontend
  ↓
Clinical API
  ↓
Authentication + Authorization
  ↓
Retrieve authorized patient data
  ↓
AI Orchestrator
  ↓
Generate analysis
  ↓
Clinical API
  ↓
Doctor
```

The AI Orchestrator should receive only the information necessary for the authorized request.

The AI layer must not be used as a mechanism to bypass access control.

---

## 10. Service-to-Service Access

Internal services must also follow authorization rules.

Examples:

```text
Clinical API
      ↓
AI Orchestrator
```

```text
AI Orchestrator
      ↓
RAG Service
```

```text
AI Orchestrator
      ↓
Imaging Service
```

Services should authenticate with each other using appropriate service credentials or tokens.

Internal network access alone must not be treated as sufficient authorization.

---

## 11. Audit Requirements

The following security-sensitive events should be auditable:

### Patient Data

* Patient record access
* Laboratory result access
* Clinical document access
* Imaging access
* Patient search where appropriate

### AI

* AI analysis requested
* AI analysis completed
* AI analysis failed
* AI result viewed
* AI feedback submitted

### Administration

* User created
* User deactivated
* Role changed
* Permission changed
* Knowledge source added/removed
* System configuration changed

Audit records should contain enough information to answer:

> Who did what, when, and to which resource?

Sensitive clinical content should not be unnecessarily duplicated inside audit logs.

---

## 12. Deny-by-Default

Every protected resource should default to:

```text
DENY
```

Access is granted only when an explicit authorization rule allows it.

Examples:

```text
No permission
→ DENY

Invalid role
→ DENY

Unauthorized patient
→ DENY

Inactive account
→ DENY

Missing authentication
→ DENY
```

---

## 13. No Direct Database Access

Application users must never directly access the production database.

Expected architecture:

```text
Frontend
   ↓
Clinical API
   ↓
Authorization
   ↓
Data Access Layer
   ↓
PostgreSQL
```

Not:

```text
Frontend
   ↓
PostgreSQL
```

and not:

```text
Doctor
   ↓
PostgreSQL
```

---

## 14. Future Roles

The MVP intentionally keeps the role model small.

Future versions may introduce specialized roles such as:

* `RADIOLOGIST`
* `LAB_TECHNICIAN`
* `NURSE`
* `RESEARCHER`
* `SUPER_ADMIN`

These roles should only be introduced when a concrete workflow requires different permissions.

---

## 15. Security Principles

The authorization system should follow:

1. Least privilege
2. Deny by default
3. Explicit authorization
4. Separation of administrative and clinical access
5. Resource-level authorization
6. Authentication before authorization
7. Authorization before data retrieval
8. No authorization decisions by the LLM
9. No direct user database access
10. Auditable sensitive actions

---

## 16. Acceptance Criteria

S0-T02 is complete when:

* MVP roles are defined.
* Doctor permissions are defined.
* Admin permissions are defined.
* Administrative and clinical permissions are separated.
* Patient-level/resource-level authorization is defined.
* Deny-by-default is established.
* AI authorization boundaries are defined.
* Service-to-service authorization requirements are defined.
* Audit requirements are defined.
* Direct database access is prohibited.
* Future roles are documented without expanding MVP scope.

**Status: DONE**
