package edu.unimagdalena.clinica.controller;

import edu.unimagdalena.clinica.dto.response.AppointmentResponseDTO;
import edu.unimagdalena.clinica.model.AppointmentStatus;
import edu.unimagdalena.clinica.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
@Import(AppointmentControllerTest.MockConfig.class)
class AppointmentControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private AppointmentService appointmentService;

    static class MockConfig {
        @Bean
        public AppointmentService appointmentService() {
            return Mockito.mock(AppointmentService.class);
        }
    }

    private AppointmentResponseDTO createAppointmentResponseDTO() {
        return AppointmentResponseDTO.builder()
                .id(1L)
                .patientId(1L)
                .doctorId(1L)
                .consultRoomId(1L)
                .startTime(LocalDateTime.now().plusHours(7))
                .endTime(LocalDateTime.now().plusDays(7).plusHours(1))
                .status(AppointmentStatus.SCHEDULED)
                .build();
    }

    @Test
    void shouldGetAllAppointments() throws Exception {
        when(appointmentService.getAllAppointments()).thenReturn(List.of(createAppointmentResponseDTO()));

        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientId").value(1L))
                .andExpect(jsonPath("$[0].doctorId").value(1L))
                .andExpect(jsonPath("$[0].consultRoomId").value(1L));
    }

    @Test
    void shouldGetAppointmentById() throws Exception {
        AppointmentResponseDTO appointmentResponseDTO = createAppointmentResponseDTO();

        when(appointmentService.getAppointmentById(1L)).thenReturn(appointmentResponseDTO);

        mockMvc.perform(get("/api/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(1L))
                .andExpect(jsonPath("$.doctorId").value(1L))
                .andExpect(jsonPath("$.consultRoomId").value(1L));
    }

    @Test
    void shouldCreateAppointment() throws Exception {
        AppointmentResponseDTO appointmentResponseDTO = createAppointmentResponseDTO();

        when(appointmentService.createAppointment(any())).thenReturn(appointmentResponseDTO);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentResponseDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientId").value(1L))
                .andExpect(jsonPath("$.doctorId").value(1L))
                .andExpect(jsonPath("$.consultRoomId").value(1L));
    }

    @Test
    void shouldUpdateAppointment() throws Exception {
        AppointmentResponseDTO appointmentResponseDTO = createAppointmentResponseDTO();

        when(appointmentService.updateAppointment(eq(1L), any())).thenReturn(appointmentResponseDTO);

        mockMvc.perform(put("/api/appointments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentResponseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(1L))
                .andExpect(jsonPath("$.doctorId").value(1L))
                .andExpect(jsonPath("$.consultRoomId").value(1L));
    }

    @Test
    void shouldDeleteAppointment() throws Exception {
        mockMvc.perform(delete("/api/appointments/1"))
                .andExpect(status().isNoContent());

        verify(appointmentService).deleteAppointment(1L);
    }

    @Test
    void shouldFindDoctorAppointmentsByDate() throws Exception {
        LocalDateTime start = LocalDateTime.of(2025, 5, 2, 10, 0);
        LocalDateTime end = start.plusMinutes(30);

        AppointmentResponseDTO appointmentResponseDTO = AppointmentResponseDTO.builder()
                .patientId(1L)
                .doctorId(1L)
                .consultRoomId(1L)
                .startTime(start)
                .endTime(end)
                .build();

        when(appointmentService.findDoctorAppointmentsByDate(eq(1L), eq(LocalDate.of(2025, 5, 2))))
                .thenReturn(List.of(appointmentResponseDTO));

        mockMvc.perform(get("/api/appointments?doctorId=1&date=2025-05-02"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientId").value(1L))
                .andExpect(jsonPath("$[0].doctorId").value(1L))
                .andExpect(jsonPath("$[0].consultRoomId").value(1L))
                .andExpect(jsonPath("$[0].startTime").value("2025-05-02T10:00:00"))
                .andExpect(jsonPath("$[0].endTime").value("2025-05-02T10:30:00"));
    }
}