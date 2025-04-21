package edu.unimagdalena.clinica.dto.response;

import java.time.LocalTime;

public record DoctorResponseDTO(Long id,
                                String fullName,
                                String email,
                                String specialty,
                                LocalTime availableFrom,
                                LocalTime availableTo) {
}
