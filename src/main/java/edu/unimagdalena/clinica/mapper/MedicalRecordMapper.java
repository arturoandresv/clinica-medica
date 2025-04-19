package edu.unimagdalena.clinica.mapper;

import edu.unimagdalena.clinica.dto.MedicalRecordDTO;
import edu.unimagdalena.clinica.model.MedicalRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {

    @Mapping(source = "appointment.id", target = "appointmentId")
    @Mapping(source = "patient.id", target = "patientId")
    MedicalRecordDTO toDTO(MedicalRecord medicalRecord);

    @Mapping(target = "appointment", ignore = true)
    @Mapping(target = "patient", ignore = true)
    MedicalRecord toEntity(MedicalRecordDTO medicalRecordDTO);
}
