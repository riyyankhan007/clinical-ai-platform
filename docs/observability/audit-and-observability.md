# AI Clinical Intelligence Platform

## Audit & Observability Requirements

**Document:** Audit & Observability Requirements
**Version:** 1.0
**Status:** Approved for Development
**Scope:** MVP and future production architecture

---

## 1. Purpose

This document defines how the platform records security-sensitive activity, monitors system behavior, detects failures, and traces requests across services.

The system must provide enough observability to answer:

> **"What happened, who initiated it, where did it fail, and why?"**

Observability must not come at the cost of exposing sensitive patient information.

---

## 2. Observability Model

The platform uses four primary observability mechanisms:

### Audit Logs

Track security-sensitive and business-sensitive actions.

Example:

```text
Doctor accessed Patient PAT-000123
```

### Application Logs

Help developers and operators understand application behavior and failures.

Example:

```text
Clinical analysis request failed
reason=AI_SERVICE_TIMEOUT
```

### Metrics

Measure system behavior quantitatively.

Example:

```text
AI analysis requests: 1,240
AI analysis failures: 17
```

### Distributed Tracing

Tracks a single request across multiple services.

Example:

```text
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

---

## 3. Correlation / Request ID

Every incoming request should receive a unique correlation identifier.

Example:

```text
X-Request-ID: 7f2a9c21
```

The identifier should be propagated across internal services.

Example:

```text
Frontend
requestId=abc123
     ↓
Clinical API
requestId=abc123
     ↓
AI Orchestrator
requestId=abc123
     ↓
RAG Service
requestId=abc123
```

This allows a complete request to be investigated without relying on sensitive patient information.

---

## 4. Audit Events

The following events should be auditable.

### Authentication

* Successful login
* Failed login
* Logout
* Account activation
* Account deactivation
* Authentication failure

### Authorization

* Access granted to protected resource
* Access denied
* Unauthorized patient access attempt
* Permission changes

### Patient Data

* Patient record accessed
* Laboratory results accessed
* Clinical document accessed
* Imaging accessed
* Patient search
* Patient data export, if implemented

### AI

* AI analysis requested
* AI analysis completed
* AI analysis failed
* AI result viewed
* AI feedback submitted
* AI safety check failed
* AI response marked for review

### Administration

* User created
* User deactivated
* Role changed
* Permission changed
* Knowledge source added
* Knowledge source removed
* System configuration changed

---

## 5. Audit Event Structure

Audit events should contain enough information to determine:

* Who performed the action
* What action occurred
* When it occurred
* Which resource was affected
* Whether it succeeded or failed
* Request/correlation ID
* Source/application/service where appropriate

Example conceptual structure:

```json
{
  "eventId": "evt-12345",
  "timestamp": "2026-09-13T10:30:00Z",
  "actorId": "user-123",
  "action": "PATIENT_RECORD_VIEW",
  "resourceType": "PATIENT",
  "resourceId": "PAT-000123",
  "result": "SUCCESS",
  "requestId": "req-456"
}
```

The exact implementation will be decided during the backend and infrastructure stages.

---

## 6. Audit Log Privacy

Audit logs must not unnecessarily duplicate clinical information.

Do not store:

* Full patient history
* Full clinical reports
* Raw laboratory reports
* Medical images
* Complete AI prompts
* Complete AI responses
* Passwords
* Authentication tokens
* API keys

Prefer identifiers and metadata.

For example:

```text
GOOD:
patientId=PAT-000123
action=VIEW_LAB_RESULTS

BAD:
patientName=John Smith
hemoglobin=8.4
fullReport="..."
```

Audit logs should answer **what happened** without becoming another copy of the medical record.

---

## 7. Application Logging

Application logs are intended primarily for debugging and operational investigation.

Recommended fields include:

* Timestamp
* Log level
* Service name
* Request ID
* Operation
* Error code
* Execution duration
* Status
* Exception information where safe

Example:

```text
2026-09-13T10:30:00Z
service=clinical-api
requestId=req-456
operation=GET_PATIENT
status=SUCCESS
durationMs=42
```

---

## 8. Log Levels

The application should support standard log levels:

### ERROR

Unexpected failures requiring investigation.

Example:

```text
AI service unavailable
```

### WARN

Potentially abnormal situations that do not necessarily cause failure.

Example:

```text
RAG returned insufficient evidence
```

### INFO

Normal important application events.

Example:

```text
Clinical analysis request completed
```

### DEBUG

Detailed development information.

Debug logging should be controlled and must not expose sensitive patient information.

---

## 9. Sensitive Information Logging Rules

The following must never be logged:

* Passwords
* Access tokens
* Refresh tokens
* API keys
* Private keys
* Database credentials
* Full patient names
* Patient contact information
* Full medical reports
* Raw medical images
* Complete patient history
* Unnecessary clinical information

Patient identifiers should only be logged where operationally justified and should follow the privacy requirements defined in:

`docs/data/data-classification-and-privacy.md`

---

## 10. Metrics

The platform should collect operational metrics such as:

### API Metrics

* Request count
* Request latency
* Error rate
* HTTP status distribution

### Database Metrics

* Query latency
* Connection pool usage
* Connection failures
* Database errors

### AI Metrics

* AI request count
* AI success rate
* AI failure rate
* AI latency
* AI timeout count
* Token usage where available
* RAG retrieval latency
* RAG retrieval failures

### System Metrics

* CPU usage
* Memory usage
* Disk usage
* Service availability

Metrics should not contain raw patient clinical information.

---

## 11. AI-Specific Observability

AI workflows require additional monitoring because failures may not always appear as application errors.

The system should track:

* AI request status
* Model/service used
* Request latency
* Retrieval latency
* Number of retrieved sources
* Evidence availability
* Safety-check result
* Model errors
* Timeout events
* Structured-output validation failures

Where appropriate, the system should also track evaluation metrics such as:

* Evidence grounding
* Retrieval quality
* Structured-output validity
* Hallucination/error evaluation
* Safety evaluation results

Sensitive prompts and responses should not be automatically stored in ordinary logs.

---

## 12. Distributed Tracing

The system should support distributed tracing across services.

Example:

```text
Trace: req-456

Frontend
   |
   | 120ms
   ↓
Clinical API
   |
   | 40ms
   ↓
Patient Data Service
   |
   | 80ms
   ↓
AI Orchestrator
   |
   | 900ms
   ↓
RAG Service
   |
   | 200ms
   ↓
LLM
```

This allows performance bottlenecks and failures to be identified.

Tracing implementation will be introduced during infrastructure/backend development.

---

## 13. Error Handling

Errors should be classified into meaningful categories.

Examples:

```text
AUTHENTICATION_ERROR
AUTHORIZATION_ERROR
RESOURCE_NOT_FOUND
VALIDATION_ERROR
DATABASE_ERROR
AI_SERVICE_ERROR
AI_TIMEOUT
RAG_ERROR
SAFETY_CHECK_FAILURE
INTERNAL_ERROR
```

The API should return safe error responses to users.

Internal implementation details should not be exposed to clients.

For example, do not return:

```text
PostgreSQL connection string...
```

or:

```text
Stack trace...
```

to the frontend.

---

## 14. Security Monitoring

The system should detect and record suspicious activity such as:

* Repeated failed logins
* Repeated authorization failures
* Attempts to access unauthorized patients
* Unusual administrative activity
* Unexpected service-to-service access
* Excessive API requests
* Repeated AI failures

Alerting thresholds will be defined during the infrastructure/security implementation stage.

---

## 15. Availability Monitoring

Critical services should have health checks.

Initial services include:

```text
Clinical API
Database
AI Orchestrator
RAG Service
Imaging Service
```

Health checks should distinguish between:

### Liveness

Is the service running?

### Readiness

Is the service capable of handling requests?

A service that is running but cannot connect to required dependencies should not necessarily report itself as ready.

---

## 16. AI Safety Monitoring

The platform should monitor safety-related failures.

Examples:

* Insufficient evidence
* Invalid AI output structure
* Unsupported claims detected by validation
* Missing evidence
* Safety check failure
* Model timeout
* Retrieval failure
* Conflicting evidence

A safety failure should result in an appropriate status such as:

```text
REVIEW_REQUIRED
```

or:

```text
INSUFFICIENT_EVIDENCE
```

rather than silently producing a confident-looking response.

---

## 17. Audit vs Application Logs

These systems serve different purposes.

| Feature                    | Audit Log          | Application Log |
| -------------------------- | ------------------ | --------------- |
| Security actions           | YES                | MAYBE           |
| Patient access             | YES                | MAYBE           |
| Debugging                  | NO                 | YES             |
| Errors                     | YES where relevant | YES             |
| Performance details        | LIMITED            | YES             |
| User actions               | YES                | MAYBE           |
| Sensitive clinical content | NO                 | NO              |
| Long-term accountability   | YES                | NOT PRIMARY     |

Audit logs are primarily for **accountability and security**.

Application logs are primarily for **operations and debugging**.

---

## 18. Monitoring Dashboard

The future platform should provide operational visibility into:

* API health
* Error rates
* Request latency
* Database health
* AI service health
* RAG health
* Service availability
* Authentication failures
* Authorization failures
* AI safety failures

The dashboard should avoid exposing patient clinical information.

---

## 19. Alerting

Alerts should be generated for meaningful operational or security events.

Potential alerts include:

```text
High API error rate
AI service unavailable
Database unavailable
Repeated authorization failures
Repeated login failures
High request latency
High AI timeout rate
RAG service unavailable
Critical safety-check failures
```

Alert thresholds should be configured based on system behavior during later testing.

---

## 20. Observability Data Retention

Retention periods for logs, metrics, traces, and audit records should be explicitly defined before production deployment.

Retention should consider:

* Security requirements
* Operational requirements
* Storage cost
* Privacy requirements
* Applicable regulations
* Organizational policies

Audit records may require different retention rules from temporary debugging logs.

---

## 21. Development Environment

During development, observability should still be implemented in a simplified form.

At minimum:

* Structured application logs
* Request IDs
* Error logging
* Basic API metrics
* Basic health checks

Production-grade monitoring infrastructure can be introduced incrementally.

---

## 22. Core Observability Principle

The platform should make it possible to understand system behavior without exposing unnecessary clinical information.

The principle is:

> **Observe everything necessary to operate and secure the system, but never log sensitive data merely because it is available.**

---

## 23. S0-T04 Acceptance Criteria

S0-T04 is complete when:

* Audit events are defined.
* Audit event structure is defined.
* Patient-access auditing is defined.
* AI activity auditing is defined.
* Administrative auditing is defined.
* Application logging requirements are defined.
* Sensitive logging restrictions are defined.
* Request/correlation IDs are defined.
* Metrics requirements are defined.
* AI-specific observability requirements are defined.
* Distributed tracing requirements are defined.
* Error categories are defined.
* Security monitoring requirements are defined.
* Health-check requirements are defined.
* AI safety monitoring requirements are defined.
* Audit logs are distinguished from application logs.
* Retention principles are documented.

**Status: DONE**
