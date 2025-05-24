package edu.unimagdalena.clinica.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.unimagdalena.clinica.model.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentRequestUpdateDTO(@NotNull LocalDateTime startTime,
                                          @NotNull LocalDateTime endTime,
                                          @NotNull @JsonFormat(shape = JsonFormat.Shape.STRING)
                                          AppointmentStatus status) {
}
