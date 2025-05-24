package edu.unimagdalena.clinica.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MedicalRecordRequestDTO(@NotNull Long appointmentId,
                                      @NotNull Long patientId,
                                      @NotBlank String diagnosis,
                                      @NotBlank String notes,
                                      @NotNull LocalDateTime createdAt) {
}
