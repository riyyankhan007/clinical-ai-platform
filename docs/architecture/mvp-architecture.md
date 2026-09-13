# AI Clinical Intelligence Platform

## MVP Architecture & Service Boundaries

**Document:** MVP Architecture & Service Boundaries
**Version:** 1.0
**Status:** Approved for Development
**MVP Boundary:** S4 — Clinical Agent

---

## 1. Purpose

This document defines the technical architecture and service boundaries for the AI Clinical Intelligence Platform MVP.

The architecture is designed to support:

* Patient 360
* Laboratory intelligence
* Clinical RAG
* AI-powered clinical analysis
* Authentication and authorization
* Auditability
* Observability
* Future medical imaging capabilities

The architecture should remain simple enough for an MVP while allowing individual components to evolve independently.

---

## 2. MVP Technology Stack

### Frontend

```text
React
TypeScript
Tailwind CSS
```

Responsibilities:

* Doctor/admin user interface
* Authentication UI
* Patient search
* Patient 360 view
* Laboratory visualization
* Clinical AI interaction
* Evidence display
* Safety warnings
* Admin interface

The frontend must not directly access the database.

---

### Clinical API

```text
Java
Spring Boot
Spring Security
REST API
```

Responsibilities:

* Authentication integration
* Authorization
* Patient APIs
* Clinical data APIs
* AI analysis APIs
* Business logic
* Request validation
* Audit event generation
* Communication with internal services

The Clinical API is the primary backend entry point for the frontend.

---

### Database

```text
PostgreSQL
```

Responsibilities:

* Patient data
* Encounters
* Conditions
* Medications
* Laboratory results
* Clinical documents metadata
* Imaging metadata
* User/application metadata where appropriate

The database is not directly exposed to the frontend.

---

### Vector Search

Initial MVP:

```text
PostgreSQL + pgvector
```

Responsibilities:

* Store document embeddings
* Similarity search
* Retrieve relevant medical knowledge
* Support clinical RAG

A dedicated vector database is not required for the initial MVP.

---

### AI Orchestrator

```text
Python
FastAPI
```

Responsibilities:

* Coordinate AI workflows
* Prepare AI context
* Call retrieval systems
* Call LLM/model services
* Validate structured AI output
* Apply AI safety checks
* Return structured clinical-support results

The AI Orchestrator must not perform authorization decisions.

---

### RAG Ingestion

```text
Python
```

Responsibilities:

* Ingest approved medical documents
* Clean and normalize documents
* Split documents into chunks
* Generate embeddings
* Store embeddings
* Track source metadata
* Support re-indexing

The ingestion pipeline should operate separately from runtime clinical requests.

---

### Imaging Service

```text
Python
FastAPI
PyTorch / specialized medical imaging models
```

Responsibilities:

* Receive authorized imaging requests
* Run specialized medical imaging models
* Return structured model findings
* Provide model/version metadata

Advanced imaging functionality is outside the initial MVP.

The service boundary exists so imaging can be introduced later without redesigning the core platform.

---

### Evaluation

The evaluation component is responsible for:

* AI quality evaluation
* RAG evaluation
* Safety testing
* Regression testing
* Synthetic scenario testing
* Model comparison

Evaluation is separate from runtime clinical services.

---

## 3. High-Level Architecture

```text
                         ┌─────────────────────┐
                         │      Doctor/Admin    │
                         └──────────┬──────────┘
                                    │
                                    ↓
                         ┌─────────────────────┐
                         │ React + TypeScript  │
                         │      Frontend       │
                         └──────────┬──────────┘
                                    │ HTTPS
                                    ↓
                         ┌─────────────────────┐
                         │   Clinical API      │
                         │ Java + Spring Boot  │
                         └──────┬──────┬───────┘
                                │      │
                   ┌────────────┘      └─────────────┐
                   ↓                                 ↓
          ┌─────────────────┐              ┌──────────────────┐
          │   PostgreSQL    │              │ AI Orchestrator  │
          │    + pgvector   │              │ Python + FastAPI │
          └─────────────────┘              └───────┬──────────┘
                                                    │
                                      ┌─────────────┼─────────────┐
                                      ↓             ↓             ↓
                               ┌────────────┐ ┌────────────┐ ┌───────────┐
                               │    RAG     │ │    LLM     │ │  Future   │
                               │ Retrieval  │ │ / AI Model │ │  Imaging  │
                               └────────────┘ └────────────┘ └───────────┘
```

---

## 4. Frontend Boundary

The frontend is responsible only for presentation and user interaction.

The frontend may:

* Display patient information
* Send API requests
* Display AI responses
* Display evidence
* Display warnings
* Display loading/error states
* Collect user feedback

The frontend must not:

* Connect directly to PostgreSQL
* Contain database credentials
* Make authorization decisions
* Call the LLM directly
* Bypass the Clinical API
* Store sensitive clinical data unnecessarily

---

## 5. Clinical API Boundary

The Clinical API is the primary application backend.

It is responsible for:

```text
Authentication
Authorization
Validation
Business Logic
Patient Access
Clinical Data Access
AI Request Coordination
Audit
```

Example API structure:

```text
/api/auth/*
/api/patients/*
/api/encounters/*
/api/labs/*
/api/documents/*
/api/clinical-analysis/*
/api/admin/*
```

Exact endpoints will be defined during implementation.

---

## 6. Clinical Data Ownership

PostgreSQL is the authoritative store for structured clinical data.

The Clinical API should own access to:

* Patient records
* Encounters
* Conditions
* Medications
* Laboratory results
* Clinical documents metadata
* Imaging metadata

Other services should not directly modify authoritative clinical data.

---

## 7. AI Orchestrator Boundary

The AI Orchestrator coordinates AI reasoning.

It may:

* Receive authorized clinical context
* Retrieve relevant knowledge
* Prepare prompts/context
* Call AI models
* Validate model responses
* Apply safety checks
* Return structured results

It must not:

* Decide whether a doctor can access a patient
* Retrieve unauthorized patient information
* Modify source patient records
* Execute clinical actions
* Override application security rules

---

## 8. AI Request Flow

The MVP clinical-analysis flow is:

```text
Doctor
  ↓
React Frontend
  ↓
Clinical API
  ↓
Authenticate User
  ↓
Authorize Patient Access
  ↓
Retrieve Relevant Patient Data
  ↓
Run Deterministic Lab Analytics
  ↓
Send Required Context
  ↓
AI Orchestrator
  ↓
Retrieve Relevant Medical Knowledge
  ↓
Generate Structured AI Response
  ↓
Validate Response
  ↓
Safety Checks
  ↓
Clinical API
  ↓
Audit Event
  ↓
Frontend
  ↓
Doctor
```

---

## 9. RAG Architecture

The RAG system has two distinct workflows.

### Ingestion

```text
Medical Source
     ↓
Document Ingestion
     ↓
Cleaning
     ↓
Chunking
     ↓
Embedding Generation
     ↓
PostgreSQL + pgvector
```

### Runtime Retrieval

```text
Clinical Question
     ↓
Query Processing
     ↓
Embedding
     ↓
Vector Search
     ↓
Relevant Chunks
     ↓
Source Metadata
     ↓
AI Orchestrator
```

---

## 10. RAG Source Requirements

Only approved medical knowledge sources should be included in the clinical knowledge corpus.

Each indexed document should maintain metadata such as:

```text
sourceId
title
sourceType
publisher
publicationDate
version
url/reference
documentVersion
ingestedAt
```

Where licensing or access restrictions apply, they must be respected.

---

## 11. Evidence Flow

AI responses should maintain traceability.

Example:

```text
AI Claim
   ↓
Evidence Reference
   ↓
Patient Record / Medical Source
```

Patient-specific evidence and external medical knowledge should remain distinguishable.

Example:

```text
PATIENT EVIDENCE
- Lab result LAB-000123

MEDICAL EVIDENCE
- Knowledge source SRC-000045
```

---

## 12. Laboratory Intelligence Boundary

Laboratory analytics should be performed using deterministic application logic wherever possible.

The system may calculate:

* Reference-range status
* Absolute change
* Percentage change
* Trend direction
* Rate of change
* Baseline deviation

Example:

```text
Lab Service / Application Logic
        ↓
Calculated Finding
        ↓
AI Orchestrator
        ↓
Clinical Interpretation
```

The LLM should explain or contextualize calculations rather than being the sole calculator.

---

## 13. Authentication Architecture

Authentication should use an established identity mechanism.

Target architecture:

```text
User
  ↓
Identity Provider
  ↓
Authentication
  ↓
Access Token
  ↓
Clinical API
  ↓
Authorization
```

The exact identity provider can be selected during implementation.

The system should support standards-based authentication such as OAuth 2.0 / OpenID Connect.

---

## 14. Authorization Architecture

Authorization occurs inside the application backend.

```text
Request
  ↓
Authentication
  ↓
Role Check
  ↓
Resource Check
  ↓
Scope Check
  ↓
Allow / Deny
```

Authorization must follow the rules defined in:

`docs/security/roles-and-access.md`

The AI layer must not replace backend authorization.

---

## 15. Service-to-Service Communication

Internal services should communicate through authenticated interfaces.

Example:

```text
Clinical API
     ↓
AI Orchestrator
```

```text
AI Orchestrator
     ↓
RAG
```

```text
AI Orchestrator
     ↓
Imaging Service
```

Internal services should not assume that network location alone provides trust.

Service authentication and authorization should be introduced during implementation.

---

## 16. Database Access

Only authorized backend services should access PostgreSQL.

Expected model:

```text
Frontend
   ↓
Clinical API
   ↓
Data Access Layer
   ↓
PostgreSQL
```

The frontend must never receive database credentials.

---

## 17. File and Object Storage

Large clinical files such as:

* Medical documents
* Medical images
* Other binary assets

should eventually use object storage rather than storing large binary objects directly inside normal relational tables.

Example:

```text
Clinical API
     ↓
Object Storage
     ↓
File Reference
     ↓
PostgreSQL Metadata
```

The MVP may initially use local/object-compatible storage for development.

---

## 18. Audit Architecture

Sensitive operations generate audit events.

Example:

```text
Doctor
  ↓
Clinical API
  ↓
Patient Access
  ↓
Audit Event
```

AI operations should also be auditable:

```text
AI Request
  ↓
AI Analysis
  ↓
Safety Status
  ↓
Audit Event
```

Audit requirements are defined in:

`docs/observability/audit-and-observability.md`

---

## 19. Observability Architecture

The MVP should support:

* Structured logs
* Request IDs
* Health checks
* Basic metrics
* Error tracking

Future production architecture should support distributed tracing.

Example:

```text
Request ID: req-123

Frontend
   ↓
Clinical API
   ↓
AI Orchestrator
   ↓
RAG
   ↓
LLM
```

The same request identifier should be propagated where practical.

---

## 20. Error Boundaries

Each service should handle failures within its own boundary and return safe errors.

Example:

```text
LLM unavailable
      ↓
AI Orchestrator detects failure
      ↓
Clinical API receives safe error
      ↓
Frontend displays:
"AI analysis is temporarily unavailable."
```

Internal stack traces and sensitive implementation details must not be exposed to users.

---

## 21. AI Safety Boundary

The AI pipeline must follow:

```text
Retrieve
  ↓
Generate
  ↓
Validate
  ↓
Safety Check
  ↓
Return
```

The system must not display AI output blindly.

Possible outcomes:

```text
PASS
REVIEW_REQUIRED
INSUFFICIENT_EVIDENCE
```

Safety requirements are defined in:

`docs/safety/ai-safety-and-failure-behavior.md`

---

## 22. MVP Service List

The MVP runtime should begin with the smallest practical number of services.

### Required

```text
Frontend
Clinical API
PostgreSQL
AI Orchestrator
RAG Ingestion
Evaluation
```

### Future / Expansion

```text
Dedicated Imaging Service
Object Storage
Redis
Dedicated Vector Database
External EHR/FHIR Integration
Advanced Observability Stack
```

The architecture should avoid premature microservice complexity.

---

## 23. Monolith vs Microservices Strategy

The MVP should maintain clear logical service boundaries without requiring every component to become an independently deployed microservice immediately.

For example:

```text
Clinical API
├── Patient Module
├── Encounter Module
├── Lab Module
├── Document Module
├── Authorization Module
└── Audit Module
```

This keeps the Java backend manageable while preserving logical boundaries.

AI workloads remain separate because they have different:

* Runtime requirements
* Programming language
* Model dependencies
* Scaling characteristics

---

## 24. Initial Deployment Model

The initial development environment should support local execution.

Target:

```text
React
   ↓
Spring Boot
   ↓
PostgreSQL
   ↓
Python AI Orchestrator
```

Docker Compose may eventually be used to simplify local infrastructure.

Production deployment architecture will be defined later.

---

## 25. Repository Architecture

The repository follows:

```text
clinical-ai-platform/
│
├── frontend/
│
├── clinical-api/
│
├── clinical-data/
│
├── ai-orchestrator/
│
├── imaging-service/
│
├── rag-ingestion/
│
├── evaluation/
│
├── infra/
│
└── docs/
    ├── product/
    ├── security/
    ├── data/
    ├── safety/
    ├── observability/
    └── architecture/
```

Each top-level component should have a clearly defined responsibility.

---

## 26. Dependency Direction

The preferred dependency direction is:

```text
Frontend
   ↓
Clinical API
   ↓
Clinical Data

Clinical API
   ↓
AI Orchestrator
   ↓
RAG / Models
```

The AI layer should not become tightly coupled to the frontend.

The frontend should not depend directly on AI implementation details.

---

## 27. Data Flow Security

Sensitive data should move through controlled boundaries.

Example:

```text
Doctor
  ↓
Authenticated Request
  ↓
Authorized Clinical API
  ↓
Minimum Required Patient Context
  ↓
AI Orchestrator
  ↓
AI Model
```

The system should minimize the amount of sensitive information transferred between services.

---

## 28. Architecture Evolution

The architecture should allow future additions without major redesign.

Potential future components:

```text
FHIR Integration
        ↓
Hospital EHR
        ↓
Clinical API

Imaging
        ↓
Imaging Service

Advanced Vector Search
        ↓
Dedicated Vector Database

Observability
        ↓
Metrics + Logs + Traces
```

These should be introduced only when required.

---

## 29. MVP Non-Goals

The architecture does not initially require:

* Multi-region deployment
* Kubernetes
* Complex service mesh
* Dedicated vector database
* Full hospital EHR integration
* Multi-modal clinical AI
* Real patient production data
* Autonomous clinical workflows

The MVP should prioritize correctness, security, traceability, and maintainability over infrastructure complexity.

---

## 30. Architecture Principles

The system follows these principles:

1. Frontend and backend are separated.
2. Backend controls access to clinical data.
3. Database is never directly exposed to users.
4. Authorization happens before data retrieval.
5. AI is not an authorization boundary.
6. AI does not modify source clinical records.
7. Patient data is minimized before AI processing.
8. Important AI claims must be evidence-backed.
9. Services communicate through explicit interfaces.
10. Sensitive operations are auditable.
11. Observability must not expose unnecessary patient information.
12. Deterministic calculations should remain deterministic.
13. MVP infrastructure should remain simple.
14. Future capabilities should have clear service boundaries.
15. Human clinical judgment remains the final decision point.

---

## 31. S0-T07 Acceptance Criteria

S0-T07 is complete when:

* MVP technology stack is defined.
* Frontend boundary is defined.
* Clinical API boundary is defined.
* Database responsibility is defined.
* AI Orchestrator boundary is defined.
* RAG architecture is defined.
* Laboratory intelligence boundary is defined.
* Imaging service boundary is defined.
* Authentication flow is defined.
* Authorization flow is defined.
* Service-to-service communication requirements are defined.
* Database access boundaries are defined.
* Object storage strategy is defined.
* Audit architecture is defined.
* Observability architecture is defined.
* AI safety boundary is defined.
* MVP service list is defined.
* Monolith vs microservice strategy is defined.
* Repository architecture is defined.
* Dependency direction is defined.
* MVP non-goals are defined.
* Architecture principles are documented.

**Status: DONE**
