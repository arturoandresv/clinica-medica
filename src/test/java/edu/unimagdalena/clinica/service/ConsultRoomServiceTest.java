package edu.unimagdalena.clinica.service;

import edu.unimagdalena.clinica.dto.request.ConsultRoomRequestDTO;
import edu.unimagdalena.clinica.dto.response.ConsultRoomResponseDTO;
import edu.unimagdalena.clinica.exception.notfound.ConsultRoomNotFoundException;
import edu.unimagdalena.clinica.mapper.ConsultRoomMapper;
import edu.unimagdalena.clinica.model.ConsultRoom;
import edu.unimagdalena.clinica.repository.ConsultRoomRepository;
import edu.unimagdalena.clinica.service.impl.ConsultRoomServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ConsultRoomServiceTest {

    @Mock
    private ConsultRoomRepository consultRoomRepository;

    @Mock
    private ConsultRoomMapper consultRoomMapper;

    @InjectMocks
    private ConsultRoomServiceImpl consultRoomService;


    @Test
    void shouldGetAllConsultRooms() {
        ConsultRoom consultRoom = ConsultRoom.builder()
                .id(1L)
                .name("Consult Room A")
                .floor(4)
                .build();

        ConsultRoomResponseDTO dto = ConsultRoomResponseDTO.builder()
                .id(1L)
                .name("Consult Room A")
                .floor(4)
                .build();

        when(consultRoomRepository.findAll()).thenReturn(List.of(consultRoom));
        when(consultRoomMapper.toDto(consultRoom)).thenReturn(dto);

        List<ConsultRoomResponseDTO> result = consultRoomService.getAllConsultRooms();

        assertEquals(1, result.size());
        assertEquals("Consult Room A", result.getFirst().name());
        assertEquals(4, result.getFirst().floor());
    }

    @Test
    void shouldGetConsultRoomById() {
        ConsultRoom consultRoom = ConsultRoom.builder()
                .id(1L)
                .name("Consult Room A")
                .floor(4)
                .build();

        ConsultRoomResponseDTO dto = ConsultRoomResponseDTO.builder()
                .id(1L)
                .name("Consult Room A")
                .floor(4)
                .build();

        when(consultRoomRepository.findById(1L)).thenReturn(Optional.of(consultRoom));
        when(consultRoomMapper.toDto(consultRoom)).thenReturn(dto);

        ConsultRoomResponseDTO result = consultRoomService.getConsultRoomById(1L);

        assertEquals("Consult Room A", result.name());
        assertEquals(4, result.floor());
    }

    @Test
    void shouldCreateConsultRoom() {
        ConsultRoomRequestDTO request = ConsultRoomRequestDTO.builder()
                .name("Consult Room A")
                .floor(4)
                .build();

        ConsultRoom consultRoom = ConsultRoom.builder()
                .id(1L)
                .name("Consult Room A")
                .floor(4)
                .build();

        ConsultRoomResponseDTO dto = ConsultRoomResponseDTO.builder()
                .id(1L)
                .name("Consult Room A")
                .floor(4)
                .build();

        when(consultRoomMapper.toEntity(request)).thenReturn(consultRoom);
        when(consultRoomRepository.save(consultRoom)).thenReturn(consultRoom);
        when(consultRoomMapper.toDto(consultRoom)).thenReturn(dto);

        ConsultRoomResponseDTO result = consultRoomService.createConsultRoom(request);

        assertEquals("Consult Room A", result.name());
        assertEquals(4, result.floor());
        verify(consultRoomRepository).save(consultRoom);
    }

    @Test
    void shouldUpdateConsultRoom() {
        ConsultRoomRequestDTO request = ConsultRoomRequestDTO.builder()
                .name("Consult Room A")
                .floor(4)
                .build();

        ConsultRoom consultRoom = ConsultRoom.builder()
                .id(1L)
                .name("Consult Room A")
                .floor(4)
                .build();

        ConsultRoomResponseDTO dto = ConsultRoomResponseDTO.builder()
                .id(1L)
                .name("Consult Room A")
                .floor(4)
                .build();

        when(consultRoomRepository.findById(1L)).thenReturn(Optional.of(consultRoom));
        when(consultRoomRepository.save(any())).thenReturn(consultRoom);
        when(consultRoomMapper.toDto(consultRoom)).thenReturn(dto);

        ConsultRoomResponseDTO result = consultRoomService.updateConsultRoom(1L, request);

        assertEquals("Consult Room A", result.name());
        assertEquals(4, result.floor());
    }

    @Test
    void shouldDeleteConsultRoom() {
        when(consultRoomRepository.existsById(1L)).thenReturn(true);

        consultRoomService.deleteConsultRoom(1L);

        verify(consultRoomRepository).deleteById(1L);
    }

    @Test
    void shouldThrowIfConsultRoomToDeleteNotFound() {
        when(consultRoomRepository.existsById(99L)).thenReturn(false);

        assertThrows(ConsultRoomNotFoundException.class, () -> consultRoomService.deleteConsultRoom(99L));
    }

    @Test
    void shouldThrowIfConsultRoomToUpdateNotFound() {
        ConsultRoomRequestDTO request = ConsultRoomRequestDTO.builder()
                .name("Consult Room A")
                .floor(4)
                .build();

        when(consultRoomRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ConsultRoomNotFoundException.class, () -> consultRoomService.updateConsultRoom(99L, request));
    }
}