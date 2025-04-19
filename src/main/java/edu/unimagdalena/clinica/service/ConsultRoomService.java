package edu.unimagdalena.clinica.service;

import edu.unimagdalena.clinica.dto.ConsultRoomDTO;

import java.util.List;

public interface ConsultRoomService {
    List<ConsultRoomDTO> getConsultRooms();
    ConsultRoomDTO getConsultRoomById(Long id);
    ConsultRoomDTO createConsultRoom(ConsultRoomDTO consultRoomDTO);
    ConsultRoomDTO updateConsultRoom(Long id, ConsultRoomDTO consultRoomDTO);
    void deleteConsultRoom(Long id);
}
