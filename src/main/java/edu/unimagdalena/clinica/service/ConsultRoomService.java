package edu.unimagdalena.clinica.service;

import edu.unimagdalena.clinica.dto.request.ConsultRoomRequestCreateDTO;
import edu.unimagdalena.clinica.dto.request.ConsultRoomRequestUpdateDTO;
import edu.unimagdalena.clinica.dto.response.ConsultRoomResponseDTO;

import java.util.List;

public interface ConsultRoomService {
    List<ConsultRoomResponseDTO> getConsultRooms();
    ConsultRoomResponseDTO getConsultRoomById(Long id);
    ConsultRoomResponseDTO createConsultRoom(ConsultRoomRequestCreateDTO consultRoomRequestCreateDTO);
    ConsultRoomResponseDTO updateConsultRoom(Long id, ConsultRoomRequestUpdateDTO dto);

    void deleteConsultRoom(Long id);
}
