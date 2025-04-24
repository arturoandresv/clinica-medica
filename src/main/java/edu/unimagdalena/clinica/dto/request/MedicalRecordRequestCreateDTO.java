package edu.unimagdalena.clinica.dto.request;

import java.time.LocalDateTime;

public record MedicalRecordRequestCreateDTO(Long id,
                                            Long appointmentId,
                                            Long patientId,
                                            String diagnosis,
                                            String notes,
                                            LocalDateTime createdAt) {
}
