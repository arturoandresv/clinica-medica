package edu.unimagdalena.clinica.service.impl;

import edu.unimagdalena.clinica.dto.request.ConsultRoomRequestCreateDTO;
import edu.unimagdalena.clinica.dto.request.ConsultRoomRequestUpdateDTO;
import edu.unimagdalena.clinica.dto.response.ConsultRoomResponseDTO;
import edu.unimagdalena.clinica.exception.ResourceNotFoundException;
import edu.unimagdalena.clinica.mapper.ConsultRoomMapper;
import edu.unimagdalena.clinica.model.ConsultRoom;
import edu.unimagdalena.clinica.repository.ConsultRoomRepository;
import edu.unimagdalena.clinica.service.ConsultRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultRoomServiceImpl implements ConsultRoomService {
    private final ConsultRoomRepository consultRoomRepository;
    private final ConsultRoomMapper consultRoomMapper;


    @Override
    public List<ConsultRoomResponseDTO> getConsultRooms() {
        return consultRoomRepository.findAll()
                .stream()
                .map(consultRoomMapper::toDto).toList();
    }

    @Override
    public ConsultRoomResponseDTO getConsultRoomById(Long id) {
        return consultRoomRepository.findById(id)
                .map(consultRoomMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("ConsultRoom not found with id: " + id));
    }

    @Override
    public ConsultRoomResponseDTO createConsultRoom(ConsultRoomRequestCreateDTO dto) {
        ConsultRoom room = consultRoomMapper.toEntity(dto);
        return consultRoomMapper.toDto(consultRoomRepository.save(room));
    }

    @Override
    public ConsultRoomResponseDTO updateConsultRoom(Long id, ConsultRoomRequestUpdateDTO dto) {
        ConsultRoom existing = consultRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ConsultRoom not found with id: " + id));
        existing.setName(dto.name());
        existing.setDescription(dto.description());
        existing.setFloor(dto.floor());
        return consultRoomMapper.toDto(consultRoomRepository.save(existing));
    }

    @Override
    public void deleteConsultRoom(Long id) {
        if (!consultRoomRepository.existsById(id)) {
            throw new ResourceNotFoundException("ConsultRoom not found with id: " + id);
        }
        consultRoomRepository.deleteById(id);
    }
}
