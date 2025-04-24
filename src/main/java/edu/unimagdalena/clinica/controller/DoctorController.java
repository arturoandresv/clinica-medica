package edu.unimagdalena.clinica.controller;

import edu.unimagdalena.clinica.dto.request.DoctorRequestDTO;
import edu.unimagdalena.clinica.dto.response.DoctorResponseDTO;
import edu.unimagdalena.clinica.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @PostMapping
    public ResponseEntity<DoctorResponseDTO> createDoctor(@Valid @RequestBody DoctorRequestDTO doctorRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createDoctor(doctorRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorRequestDTO doctorRequestDTO) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, doctorRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(params = "specialty")
    public ResponseEntity<List<DoctorResponseDTO>> findBySpecialty(@RequestParam String specialty) {
        return ResponseEntity.ok(doctorService.findBySpecialty(specialty));
    }
}
