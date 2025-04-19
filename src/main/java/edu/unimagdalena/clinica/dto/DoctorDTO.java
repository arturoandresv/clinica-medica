package edu.unimagdalena.clinica.dto;

import java.time.LocalTime;

public record DoctorDTO(Long id,
                        String fullName,
                        String email,
                        String specialty,
                        LocalTime availableFrom,
                        LocalTime availableTo) {
}
