package com.clinicalai.clinical_api.patient;

import com.clinicalai.clinical_api.audit.AuditEvent;
import com.clinicalai.clinical_api.audit.AuditEventRepository;
import com.clinicalai.clinical_api.patient.dto.Patient360Response;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;
    private final AuditEventRepository auditEventRepository;

    public PatientController(
            PatientService patientService,
            AuditEventRepository auditEventRepository) {
        this.patientService = patientService;
        this.auditEventRepository = auditEventRepository;
    }

    @GetMapping("/search")
    public List<Patient> searchPatients(@RequestParam String q) {
        return patientService.searchPatients(q);
    }

    @GetMapping("/{id}")
    public Patient360Response getPatient360(
            @PathVariable Long id,
            Authentication authentication) {

        Patient360Response response = patientService.getPatient360(id);

        auditEventRepository.save(
                new AuditEvent(
                        authentication.getName(),
                        "PATIENT_VIEW",
                        id
                )
        );

        return response;
    }
}
