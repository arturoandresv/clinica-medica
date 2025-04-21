package edu.unimagdalena.clinica.mapper;

import edu.unimagdalena.clinica.dto.request.PatientRequestDTO;
import edu.unimagdalena.clinica.dto.response.PatientResponseDTO;
import edu.unimagdalena.clinica.model.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientResponseDTO toDto(Patient patient);

    Patient toEntity(PatientRequestDTO patientRequestDTO);
}
