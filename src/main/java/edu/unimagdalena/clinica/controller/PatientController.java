package edu.unimagdalena.clinica.controller;

import edu.unimagdalena.clinica.dto.request.PatientRequestDTO;
import edu.unimagdalena.clinica.dto.response.PatientResponseDTO;
import edu.unimagdalena.clinica.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(@Valid @RequestBody PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(patientRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientRequestDTO patientRequestDTO) {
        return ResponseEntity.ok(patientService.updatePatient(id,patientRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
