package edu.unimagdalena.clinica.dto.response;

import java.time.LocalDateTime;

public record MedicalRecordResponseDTO(Long id,
                                       Long appointmentId,
                                       Long patientId,
                                       String diagnosis,
                                       String notes,
                                       LocalDateTime createdAt) {
}
