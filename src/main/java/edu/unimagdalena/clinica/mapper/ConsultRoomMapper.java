package edu.unimagdalena.clinica.mapper;

import edu.unimagdalena.clinica.dto.ConsultRoomDTO;
import edu.unimagdalena.clinica.model.ConsultRoom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsultRoomMapper {

    ConsultRoomDTO toDto(ConsultRoom consultRoom);

    ConsultRoom toEntity(ConsultRoomDTO consultRoomDTO);
}
