package edu.unimagdalena.clinica.dto.request;

import java.time.LocalDateTime;

public record MedicalRecordRequestUpdateDTO(String diagnosis,
                                            String notes,
                                            LocalDateTime createdAt) {
}
