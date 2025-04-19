package edu.unimagdalena.clinica.dto;

public record PatientDTO(Long id,
                         String fullName,
                         String email,
                         String phone) {
}
