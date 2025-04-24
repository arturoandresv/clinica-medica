package edu.unimagdalena.clinica.service;

import edu.unimagdalena.clinica.dto.request.ConsultRoomRequestDTO;
import edu.unimagdalena.clinica.dto.response.ConsultRoomResponseDTO;

import java.util.List;

public interface ConsultRoomService {
    List<ConsultRoomResponseDTO> getConsultRooms();
    ConsultRoomResponseDTO getConsultRoomById(Long id);
    ConsultRoomResponseDTO createConsultRoom(ConsultRoomRequestDTO consultRoomRequestDTO);
    ConsultRoomResponseDTO updateConsultRoom(Long id, ConsultRoomRequestDTO dto);

    void deleteConsultRoom(Long id);
}
