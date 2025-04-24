package edu.unimagdalena.clinica.controller;

import edu.unimagdalena.clinica.dto.request.AppointmentRequestCreateDTO;
import edu.unimagdalena.clinica.dto.request.AppointmentRequestUpdateDTO;
import edu.unimagdalena.clinica.dto.response.AppointmentResponseDTO;
import edu.unimagdalena.clinica.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments(){
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(@PathVariable Long id){
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> createAppointment(@Valid @RequestBody AppointmentRequestCreateDTO appointmentRequestCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appointmentService.createAppointment(appointmentRequestCreateDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentRequestUpdateDTO appointmentRequestUpdateDTO) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, appointmentRequestUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> cancelAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(id));
    }

    @GetMapping(params = {"doctorId", "date"})
    public ResponseEntity<List<AppointmentResponseDTO>> findDoctorAppointmentsByDate(@RequestParam Long doctorId, @RequestParam LocalDate date){
        return ResponseEntity.ok(appointmentService.findDoctorAppointmentsByDate(doctorId, date));
    }
}
