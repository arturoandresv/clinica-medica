package edu.unimagdalena.clinica.dto.response;

import lombok.Builder;

import java.time.LocalTime;

@Builder
public record DoctorResponseDTO(Long id,
                                String fullName,
                                String email,
                                String specialty,
                                LocalTime availableFrom,
                                LocalTime availableTo) {
}
