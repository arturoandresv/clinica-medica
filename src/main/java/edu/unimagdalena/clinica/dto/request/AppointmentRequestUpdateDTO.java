package edu.unimagdalena.clinica.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.unimagdalena.clinica.model.AppointmentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentRequestUpdateDTO(LocalDateTime startTime,
                                          LocalDateTime endTime,
                                          @JsonFormat(shape = JsonFormat.Shape.STRING)
                                          AppointmentStatus status) {
}
