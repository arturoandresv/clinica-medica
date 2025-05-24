package edu.unimagdalena.clinica.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentRequestCreateDTO(@NotNull Long patientId,
                                          @NotNull Long doctorId,
                                          @NotNull Long consultRoomId,
                                          @NotNull LocalDateTime startTime,
                                          @NotNull LocalDateTime endTime) {
}
