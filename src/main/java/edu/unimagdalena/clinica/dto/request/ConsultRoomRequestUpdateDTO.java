package edu.unimagdalena.clinica.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.unimagdalena.clinica.model.AppointmentStatus;

import java.time.LocalDateTime;

public record ConsultRoomRequestUpdateDTO(Long id,
                                          String name,
                                          Integer floor,
                                          String description) {
}
