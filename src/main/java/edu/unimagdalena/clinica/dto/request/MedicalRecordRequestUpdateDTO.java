package edu.unimagdalena.clinica.dto.request;

import lombok.Builder;

@Builder
public record MedicalRecordRequestUpdateDTO(String diagnosis,
                                            String notes) {
}
