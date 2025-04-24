package edu.unimagdalena.clinica.dto.request;

import lombok.Builder;

import java.time.LocalTime;

@Builder
public record DoctorRequestDTO(String fullName,
                               String email,
                               String specialty,
                               LocalTime availableFrom,
                               LocalTime availableTo) {
}
