package edu.unimagdalena.clinica.dto;

import edu.unimagdalena.clinica.model.AppointmentStatus;

import java.time.LocalDateTime;

public record AppointmentDTO(Long id,
                             Long patientId,
                             Long doctorId,
                             Long consultRoomId,
                             LocalDateTime startTime,
                             LocalDateTime endTime,
                             AppointmentStatus status) {
}
