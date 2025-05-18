package edu.unimagdalena.clinica.controller;

import edu.unimagdalena.clinica.dto.request.ConsultRoomRequestDTO;
import edu.unimagdalena.clinica.dto.request.PatientRequestDTO;
import edu.unimagdalena.clinica.dto.response.ConsultRoomResponseDTO;
import edu.unimagdalena.clinica.dto.response.PatientResponseDTO;
import edu.unimagdalena.clinica.service.ConsultRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class ConsultRoomController {
    private final ConsultRoomService consultRoomService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ConsultRoomResponseDTO>> getAllConsultRooms() {
        return ResponseEntity.ok(consultRoomService.getAllConsultRooms());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ConsultRoomResponseDTO> getConsultRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(consultRoomService.getConsultRoomById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConsultRoomResponseDTO> createConsultRoom(@Valid @RequestBody ConsultRoomRequestDTO ConsultRoomRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultRoomService.createConsultRoom(ConsultRoomRequestDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ConsultRoomResponseDTO> updateConsultRoom(@PathVariable Long id, @Valid @RequestBody ConsultRoomRequestDTO consultRoomRequestDTO) {
        return ResponseEntity.ok(consultRoomService.updateConsultRoom(id, consultRoomRequestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteConsultRoom(@PathVariable Long id) {
        consultRoomService.deleteConsultRoom(id);
        return ResponseEntity.noContent().build();
    }
}
