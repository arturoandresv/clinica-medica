package edu.unimagdalena.clinica.dto.request;

import lombok.Builder;

@Builder
public record PatientRequestDTO(String fullName,
                                String email,
                                String phone) {
}
