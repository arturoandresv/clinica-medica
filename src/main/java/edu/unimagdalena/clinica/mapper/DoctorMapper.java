package edu.unimagdalena.clinica.mapper;

import edu.unimagdalena.clinica.dto.request.DoctorRequestDTO;
import edu.unimagdalena.clinica.dto.response.DoctorResponseDTO;
import edu.unimagdalena.clinica.model.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    DoctorResponseDTO toDto(Doctor doctor);

    Doctor toEntity(DoctorRequestDTO doctorRequestDTO);
}
