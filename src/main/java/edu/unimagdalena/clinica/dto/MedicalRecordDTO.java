package edu.unimagdalena.clinica.dto;

import java.time.LocalDateTime;

public record MedicalRecordDTO(Long id,
                               Long appointmentId,
                               Long patientId,
                               String diagnosis,
                               String notes,
                               LocalDateTime createdAt) {
}
