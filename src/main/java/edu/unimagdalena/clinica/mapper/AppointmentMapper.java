package edu.unimagdalena.clinica.mapper;

import edu.unimagdalena.clinica.dto.request.AppointmentRequestCreateDTO;
import edu.unimagdalena.clinica.dto.response.AppointmentResponseDTO;
import edu.unimagdalena.clinica.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.fullName", target = "patientFullName")
    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "doctor.fullName", target = "doctorFullName")
    @Mapping(source = "consultRoom.id", target = "consultRoomId")
    @Mapping(source = "consultRoom.name", target = "consultRoomName")
    AppointmentResponseDTO toDTO(Appointment appointment);

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "consultRoom", ignore = true)
    Appointment toEntity(AppointmentRequestCreateDTO appointmentRequestCreateDTO);
}
