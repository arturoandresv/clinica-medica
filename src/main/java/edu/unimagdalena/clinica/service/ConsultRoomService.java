package edu.unimagdalena.clinica.service;

import edu.unimagdalena.clinica.dto.response.ConsultRoomResponseDTO;

import java.util.List;

public interface ConsultRoomService {
    List<ConsultRoomResponseDTO> getConsultRooms();
    ConsultRoomResponseDTO getConsultRoomById(Long id);
    ConsultRoomResponseDTO createConsultRoom(ConsultRoomResponseDTO consultRoomResponseDTO);
    ConsultRoomResponseDTO updateConsultRoom(Long id, ConsultRoomResponseDTO consultRoomResponseDTO);
    void deleteConsultRoom(Long id);
}
