package com.clinicalai.clinical_api.audit;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "audit_events")
public class AuditEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "actor_id", nullable = false)
    private String actorId;

    @Column(nullable = false)
    private String action;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "occurred_at", nullable = false)
    private OffsetDateTime occurredAt;

    protected AuditEvent() {
    }

    public AuditEvent(String actorId, String action, Long patientId) {
        this.actorId = actorId;
        this.action = action;
        this.patientId = patientId;
        this.occurredAt = OffsetDateTime.now();
    }
}
