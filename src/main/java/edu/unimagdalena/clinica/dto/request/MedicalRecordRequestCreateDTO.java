package edu.unimagdalena.clinica.dto.request;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MedicalRecordRequestCreateDTO(Long appointmentId,
                                            Long patientId,
                                            String diagnosis,
                                            String notes,
                                            LocalDateTime createdAt) {
}
