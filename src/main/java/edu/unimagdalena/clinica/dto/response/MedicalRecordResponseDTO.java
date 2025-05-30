package edu.unimagdalena.clinica.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MedicalRecordResponseDTO(Long id,
                                       Long appointmentId,
                                       Long patientId,
                                       String patientFullName,
                                       Long doctorId,
                                       String doctorFullName,
                                       String diagnosis,
                                       String notes,
                                       LocalDateTime createdAt) {
}
