package edu.unimagdalena.clinica.dto.request;

import java.time.LocalTime;

public record DoctorWithUserDTO(
        String fullName,
        String email,
        String specialty,
        LocalTime availableFrom,
        LocalTime availableTo,
        String password
) {}
