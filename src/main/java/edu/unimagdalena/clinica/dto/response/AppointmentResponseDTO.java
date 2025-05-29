package edu.unimagdalena.clinica.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.unimagdalena.clinica.model.AppointmentStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentResponseDTO(Long id,
                                     Long patientId,
                                     String patientFullName,
                                     Long doctorId,
                                     String doctorFullName,
                                     Long consultRoomId,
                                     String consultRoomName,
                                     LocalDateTime startTime,
                                     LocalDateTime endTime,
                                     @JsonFormat(shape = JsonFormat.Shape.STRING)
                             AppointmentStatus status) {
}

