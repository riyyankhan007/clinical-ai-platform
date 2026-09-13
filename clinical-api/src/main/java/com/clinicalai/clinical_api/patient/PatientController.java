package com.clinicalai.clinical_api.patient;

import com.clinicalai.clinical_api.patient.dto.Patient360Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/search")
    public List<Patient> searchPatients(@RequestParam String q) {
        return patientService.searchPatients(q);
    }

    @GetMapping("/{id}")
    public Patient360Response getPatient360(@PathVariable Long id) {
        return patientService.getPatient360(id);
    }
}
