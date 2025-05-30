package edu.unimagdalena.clinica.mapper;

import edu.unimagdalena.clinica.dto.request.MedicalRecordRequestDTO;
import edu.unimagdalena.clinica.dto.response.MedicalRecordResponseDTO;
import edu.unimagdalena.clinica.model.MedicalRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {

    @Mapping(source = "appointment.id", target = "appointmentId")
    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "appointment.doctor.fullName", target = "doctorFullName")
    @Mapping(source = "appointment.doctor.id", target = "doctorId")
    @Mapping(source = "patient.fullName", target = "patientFullName")
    MedicalRecordResponseDTO toDTO(MedicalRecord medicalRecord);

    @Mapping(target = "appointment", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    MedicalRecord toEntity(MedicalRecordRequestDTO medicalRecordRequestDTO);
}
