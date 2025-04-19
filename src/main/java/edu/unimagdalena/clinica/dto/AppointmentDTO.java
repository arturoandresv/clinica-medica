package edu.unimagdalena.clinica.dto;

import edu.unimagdalena.clinica.model.AppointmentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

public record AppointmentDTO(Long id,
                             Long patientId,
                             Long doctorId,
                             Long consultRoomId,
                             LocalDateTime startTime,
                             LocalDateTime endTime,
                             @Enumerated(EnumType.STRING)
                             AppointmentStatus status) {
}

