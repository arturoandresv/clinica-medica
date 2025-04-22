package edu.unimagdalena.clinica.dto.response;

import lombok.Builder;

@Builder
public record PatientResponseDTO(Long id,
                                 String fullName,
                                 String email,
                                 String phone) {
}
