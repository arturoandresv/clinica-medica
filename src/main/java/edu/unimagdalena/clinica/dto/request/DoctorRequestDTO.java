package edu.unimagdalena.clinica.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record DoctorRequestDTO(@NotBlank String fullName,
                               @NotBlank String email,
                               @NotBlank String specialty,
                               @NotNull LocalTime availableFrom,
                               @NotNull LocalTime availableTo) {
}
