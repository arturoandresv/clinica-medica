package edu.unimagdalena.clinica.dto.request;

import lombok.Builder;

@Builder
public record ConsultRoomRequestDTO(String name,
                                    Integer floor,
                                    String description) {
}
