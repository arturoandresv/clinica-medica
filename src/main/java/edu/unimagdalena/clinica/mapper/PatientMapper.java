package edu.unimagdalena.clinica.mapper;

import edu.unimagdalena.clinica.dto.PatientDTO;
import edu.unimagdalena.clinica.model.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientDTO toDto(Patient patient);

    Patient toEntity(PatientDTO patientDTO);
}
