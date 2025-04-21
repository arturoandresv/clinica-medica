package edu.unimagdalena.clinica.dto.response;

public record ConsultRoomResponseDTO(Long id,
                                     String name,
                                     Integer floor,
                                     String description) {
}
