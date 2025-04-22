package edu.unimagdalena.clinica.dto.response;

import lombok.Builder;

@Builder
public record ConsultRoomResponseDTO(Long id,
                                     String name,
                                     Integer floor,
                                     String description) {
}
