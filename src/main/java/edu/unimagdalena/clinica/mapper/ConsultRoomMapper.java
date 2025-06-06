package edu.unimagdalena.clinica.mapper;

import edu.unimagdalena.clinica.dto.request.ConsultRoomRequestDTO;
import edu.unimagdalena.clinica.dto.response.ConsultRoomResponseDTO;
import edu.unimagdalena.clinica.model.ConsultRoom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsultRoomMapper {

    ConsultRoomResponseDTO toDto(ConsultRoom consultRoom);

    ConsultRoom toEntity(ConsultRoomRequestDTO consultRoomRequestDTO);
}
