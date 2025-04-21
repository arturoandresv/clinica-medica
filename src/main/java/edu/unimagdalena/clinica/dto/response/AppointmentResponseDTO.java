package edu.unimagdalena.clinica.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.unimagdalena.clinica.model.AppointmentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentResponseDTO(Long id,
                                     Long patientId,
                                     Long doctorId,
                                     Long consultRoomId,
                                     LocalDateTime startTime,
                                     LocalDateTime endTime,
                                     @JsonFormat(shape = JsonFormat.Shape.STRING)
                             AppointmentStatus status) {
}

