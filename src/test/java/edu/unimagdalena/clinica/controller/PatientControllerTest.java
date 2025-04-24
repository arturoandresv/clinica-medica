package edu.unimagdalena.clinica.controller;

import edu.unimagdalena.clinica.dto.response.PatientResponseDTO;
import edu.unimagdalena.clinica.service.PatientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
@Import(PatientControllerTest.MockConfig.class)
class PatientControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private PatientService patientService;

    static class MockConfig {
        @Bean
        public PatientService patientService() {
            return Mockito.mock(PatientService.class);
        }
    }

    private PatientResponseDTO createPatientResponseDTO() {
        return  PatientResponseDTO.builder()
                .id(1L)
                .fullName("Juan Rodriguez")
                .email("jrodriguez@gmail.com")
                .phone("3014598321")
                .build();
    }

    @Test
    void shouldGetAllPatients() throws Exception {
        when(patientService.getAllPatients()).thenReturn(List.of(createPatientResponseDTO()));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Juan Rodriguez"))
                .andExpect(jsonPath("$[0].email").value("jrodriguez@gmail.com"))
                .andExpect(jsonPath("$[0].phone").value("3014598321"));
    }

    @Test
    void shouldGetPatientById() throws Exception {
        PatientResponseDTO patientResponseDTO = createPatientResponseDTO();

        when(patientService.getPatientById(1L)).thenReturn(patientResponseDTO);

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fullName").value("Juan Rodriguez"))
                .andExpect(jsonPath("$.email").value("jrodriguez@gmail.com"))
                .andExpect(jsonPath("$.phone").value("3014598321"));
    }

    @Test
    void shouldCreatePatient() throws Exception {
        PatientResponseDTO patientResponseDTO = createPatientResponseDTO();

        when(patientService.createPatient(any())).thenReturn(patientResponseDTO);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientResponseDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("Juan Rodriguez"))
                .andExpect(jsonPath("$.email").value("jrodriguez@gmail.com"))
                .andExpect(jsonPath("$.phone").value("3014598321"));
    }

    @Test
    void shouldUpdatePatient() throws Exception {
        PatientResponseDTO patientResponseDTO = createPatientResponseDTO();

        when(patientService.updatePatient(eq(1L), any())).thenReturn(patientResponseDTO);

        mockMvc.perform(put("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientResponseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Juan Rodriguez"))
                .andExpect(jsonPath("$.email").value("jrodriguez@gmail.com"))
                .andExpect(jsonPath("$.phone").value("3014598321"));
    }

    @Test
    void shouldDeletePatient() throws Exception {
        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isNoContent());

        verify(patientService).deletePatient(1L);
    }
}