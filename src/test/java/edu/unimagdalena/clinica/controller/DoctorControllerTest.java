package edu.unimagdalena.clinica.controller;

import edu.unimagdalena.clinica.dto.response.DoctorResponseDTO;
import edu.unimagdalena.clinica.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
@Import(DoctorControllerTest.MockConfig.class)
class DoctorControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private DoctorService doctorService;

    static class MockConfig {
        @Bean
        public DoctorService doctorService() {
            return Mockito.mock(DoctorService.class);
        }
    }

    private DoctorResponseDTO createDoctorResponseDTO() {
        return DoctorResponseDTO.builder()
                .id(1L)
                .fullName("Pedro Gonzalez")
                .email("pgonzalez@gmail.com")
                .specialty("Cardiology")
                .availableFrom(LocalTime.now())
                .availableTo(LocalTime.now().plusHours(8))
                .build();
    }

    @Test
    void shouldGetAllDoctors() throws Exception {
        DoctorResponseDTO doctorResponseDTO = createDoctorResponseDTO();

        when(doctorService.getAllDoctors()).thenReturn(List.of(doctorResponseDTO));

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Pedro Gonzalez"))
                .andExpect(jsonPath("$[0].email").value("pgonzalez@gmail.com"))
                .andExpect(jsonPath("$[0].specialty").value("Cardiology"));
    }

    @Test
    void shouldGetDoctorById() throws Exception {
        DoctorResponseDTO doctorResponseDTO = createDoctorResponseDTO();

        when(doctorService.getDoctorById(1L)).thenReturn(doctorResponseDTO);

        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fullName").value("Pedro Gonzalez"))
                .andExpect(jsonPath("$.email").value("pgonzalez@gmail.com"))
                .andExpect(jsonPath("$.specialty").value("Cardiology"));
    }

    @Test
    void shouldCreateDoctor() throws Exception {
        DoctorResponseDTO doctorResponseDTO = createDoctorResponseDTO();
        when(doctorService.createDoctor(any())).thenReturn(doctorResponseDTO);

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorResponseDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("Pedro Gonzalez"))
                .andExpect(jsonPath("$.email").value("pgonzalez@gmail.com"))
                .andExpect(jsonPath("$.specialty").value("Cardiology"));
    }

    @Test
    void shouldUpdateDoctor() throws Exception {
        DoctorResponseDTO doctorResponseDTO = createDoctorResponseDTO();
        when(doctorService.updateDoctor(eq(1L), any())).thenReturn(doctorResponseDTO);

        mockMvc.perform(put("/api/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorResponseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Pedro Gonzalez"))
                .andExpect(jsonPath("$.email").value("pgonzalez@gmail.com"))
                .andExpect(jsonPath("$.specialty").value("Cardiology"));
    }

    @Test
    void shouldDeleteDoctor() throws Exception {
        mockMvc.perform(delete("/api/doctors/1"))
                .andExpect(status().isNoContent());

        verify(doctorService).deleteDoctor(1L);
    }

    @Test
    void shouldFindBySpecialty() throws Exception {
        DoctorResponseDTO doctor = createDoctorResponseDTO();
        when(doctorService.findBySpecialty("Cardiology")).thenReturn(List.of(doctor));

        mockMvc.perform(get("/api/doctors?specialty=Cardiology"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].specialty").value("Cardiology"))
                .andExpect(jsonPath("$[0].fullName").value("Pedro Gonzalez"))
                .andExpect(jsonPath("$[0].email").value("pgonzalez@gmail.com"));
    }
}