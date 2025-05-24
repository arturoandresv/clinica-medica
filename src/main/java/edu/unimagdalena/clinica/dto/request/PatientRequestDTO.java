package edu.unimagdalena.clinica.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PatientRequestDTO(@NotBlank String fullName,
                                @NotBlank String email,
                                @NotBlank String phone) {
}
