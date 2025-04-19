package edu.unimagdalena.clinica.mapper;

import edu.unimagdalena.clinica.dto.DoctorDTO;
import edu.unimagdalena.clinica.model.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    DoctorDTO toDto(Doctor doctor);

    Doctor toEntity(DoctorDTO doctorDTO);
}
