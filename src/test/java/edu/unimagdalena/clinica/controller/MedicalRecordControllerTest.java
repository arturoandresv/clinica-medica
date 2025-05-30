package edu.unimagdalena.clinica.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.unimagdalena.clinica.dto.response.MedicalRecordResponseDTO;
import edu.unimagdalena.clinica.security.jwt.JwtFilter;
import edu.unimagdalena.clinica.service.MedicalRecordService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicalRecordController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(MedicalRecordControllerTest.MockConfig.class)
class MedicalRecordControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MedicalRecordService medicalRecordService;

    static class MockConfig {
        @Bean
        public MedicalRecordService medicalRecordService() {
            return Mockito.mock(MedicalRecordService.class);
        }
        @Bean
        public JwtFilter jwtFilter() {
            return Mockito.mock(JwtFilter.class);
        }
    }

    private MedicalRecordResponseDTO createMedicalRecordResponseDTO() {
        return MedicalRecordResponseDTO.builder()
                .id(1L)
                .appointmentId(1L)
                .patientId(1L)
                .diagnosis("diabetes")
                .notes("none")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldGetAllMedicalRecords() throws Exception {
        when(medicalRecordService.getAllMedicalRecords()).thenReturn(List.of(createMedicalRecordResponseDTO()));

        mockMvc.perform(get("/api/records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].diagnosis").value("diabetes"))
                .andExpect(jsonPath("$[0].patientId").value(1L));
    }

    @Test
    void shouldGetMedicalRecordById() throws Exception {
        when(medicalRecordService.getMedicalRecordById(1L)).thenReturn(createMedicalRecordResponseDTO());

        mockMvc.perform(get("/api/records/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diagnosis").value("diabetes"))
                .andExpect(jsonPath("$.notes").value("none"))
                .andExpect(jsonPath("$.patientId").value(1L));
    }

    @Test
    void shouldGetMedicalRecordByPatientId() throws Exception {
        when(medicalRecordService.getMedicalRecordsByPatientId(1L))
                .thenReturn(List.of(createMedicalRecordResponseDTO()));

        mockMvc.perform(get("/api/records/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientId").value(1L))
                .andExpect(jsonPath("$[0].diagnosis").value("diabetes"));
    }

    @Test
    void shouldCreateMedicalRecord() throws Exception {
        MedicalRecordResponseDTO record = createMedicalRecordResponseDTO();
        when(medicalRecordService.createMedicalRecord(any())).thenReturn(record);

        mockMvc.perform(post("/api/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.diagnosis").value("diabetes"))
                .andExpect(jsonPath("$.notes").value("none"));
    }

    @Test
    void shouldDeleteMedicalRecord() throws Exception {
        mockMvc.perform(delete("/api/records/1"))
                .andExpect(status().isNoContent());

        verify(medicalRecordService).deleteMedicalRecord(1L);
    }
}