package edu.unimagdalena.clinica.mapper;

import edu.unimagdalena.clinica.dto.AppointmentDTO;
import edu.unimagdalena.clinica.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.nio.channels.Pipe;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "consultRoom.id", target = "consultRoomId")
    AppointmentDTO toDTO(Appointment appointment);

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "consultRoom", ignore = true)
    Appointment toEntity(AppointmentDTO appointmentDTO);
}
