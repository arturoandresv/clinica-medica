package edu.unimagdalena.clinica.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ConsultRoomRequestDTO(@NotBlank String name,
                                    @NotNull Integer floor,
                                    @NotBlank String description) {
}
