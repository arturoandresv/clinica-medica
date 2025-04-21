package edu.unimagdalena.clinica.dto.response;

public record PatientResponseDTO(Long id,
                                 String fullName,
                                 String email,
                                 String phone) {
}
