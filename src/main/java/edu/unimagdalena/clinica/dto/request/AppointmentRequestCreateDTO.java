package edu.unimagdalena.clinica.dto.request;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AppointmentRequestCreateDTO(Long patientId,
                                          Long doctorId,
                                          Long consultRoomId,
                                          LocalDateTime startTime,
                                          LocalDateTime endTime) {
}
