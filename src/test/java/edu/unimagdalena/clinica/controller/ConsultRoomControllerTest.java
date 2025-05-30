package edu.unimagdalena.clinica.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.unimagdalena.clinica.dto.response.ConsultRoomResponseDTO;
import edu.unimagdalena.clinica.security.jwt.JwtFilter;
import edu.unimagdalena.clinica.service.ConsultRoomService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConsultRoomController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ConsultRoomControllerTest.MockConfig.class)
class ConsultRoomControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ConsultRoomService consultRoomService;

    static class MockConfig {
        @Bean
        public ConsultRoomService consultRoomService() {
            return Mockito.mock(ConsultRoomService.class);
        }
        @Bean
        public JwtFilter jwtFilter() {
            return Mockito.mock(JwtFilter.class);
        }
    }

    private ConsultRoomResponseDTO createConsultRoomResponseDTO() {
        return ConsultRoomResponseDTO.builder()
                .id(1L)
                .name("Consult Room A")
                .floor(3)
                .description("First room for consults")
                .build();
    }

    @Test
    void shouldGetAllConsultRooms() throws Exception {
        ConsultRoomResponseDTO consultRoomResponseDTO = createConsultRoomResponseDTO();

        when(consultRoomService.getAllConsultRooms()).thenReturn(List.of(consultRoomResponseDTO));

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Consult Room A"))
                .andExpect(jsonPath("$[0].floor").value(3))
                .andExpect(jsonPath("$[0].description").value("First room for consults"));
        verify(consultRoomService).getAllConsultRooms();
    }

    @Test
    void shouldGetConsultRoomById() throws Exception {
        ConsultRoomResponseDTO consultRoomResponseDTO = createConsultRoomResponseDTO();

        when(consultRoomService.getConsultRoomById(1L)).thenReturn(consultRoomResponseDTO);

        mockMvc.perform(get("/api/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Consult Room A"))
                .andExpect(jsonPath("$.floor").value(3))
                .andExpect(jsonPath("$.description").value("First room for consults"));
        verify(consultRoomService).getConsultRoomById(1L);
    }

    @Test
    void shouldCreateConsultRoom() throws Exception {
        ConsultRoomResponseDTO room = createConsultRoomResponseDTO();
        when(consultRoomService.createConsultRoom(any())).thenReturn(room);

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Consult Room A"))
                .andExpect(jsonPath("$.floor").value(3))
                .andExpect(jsonPath("$.description").value("First room for consults"));
        verify(consultRoomService).createConsultRoom(any());
    }

    @Test
    void shouldUpdateConsultRoom() throws Exception {
        ConsultRoomResponseDTO room = createConsultRoomResponseDTO();
        when(consultRoomService.updateConsultRoom(eq(1L), any())).thenReturn(room);

        mockMvc.perform(put("/api/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Consult Room A"))
                .andExpect(jsonPath("$.floor").value(3))
                .andExpect(jsonPath("$.description").value("First room for consults"));
        verify(consultRoomService).updateConsultRoom(eq(1L), any());
    }

    @Test
    void shouldDeleteConsultRoom() throws Exception {
        mockMvc.perform(delete("/api/rooms/1"))
                .andExpect(status().isNoContent());

        verify(consultRoomService).deleteConsultRoom(1L);
    }
}