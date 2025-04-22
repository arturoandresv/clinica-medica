package edu.unimagdalena.clinica.service;

import edu.unimagdalena.clinica.dto.request.PatientRequestDTO;
import edu.unimagdalena.clinica.dto.response.PatientResponseDTO;
import edu.unimagdalena.clinica.exception.notfound.PatientNotFoundException;
import edu.unimagdalena.clinica.mapper.PatientMapper;
import edu.unimagdalena.clinica.model.Patient;
import edu.unimagdalena.clinica.repository.PatientRepository;
import edu.unimagdalena.clinica.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    void shouldGetAllPatients() {
        Patient patient = Patient.builder().id(1L)
                .fullName("Juan Rodriguez")
                .email("jrodriguez@gmail.com")
                .phone("3014598321")
                .build();

        PatientResponseDTO patientResponseDTO = PatientResponseDTO.builder().id(1L)
                .fullName("Juan Rodriguez")
                .email("jrodriguez@gmail.com")
                .phone("3014598321")
                .build();

        when(patientRepository.findAll()).thenReturn(List.of(patient));
        when(patientMapper.toDto(patient)).thenReturn(patientResponseDTO);

        List<PatientResponseDTO> result = patientService.getAllPatients();

        assertEquals(1, result.size());
        assertEquals("Juan Rodriguez", result.getFirst().fullName());
        assertEquals("jrodriguez@gmail.com", result.getFirst().email());
        assertEquals("3014598321", result.getFirst().phone());
    }

    @Test
    void shouldGetPatientById() {
        Patient existing = Patient.builder().id(1L)
                .fullName("Juan Rodriguez")
                .email("jrodriguez@gmail.com")
                .phone("3014598321")
                .build();

        PatientResponseDTO responseDTO = PatientResponseDTO.builder().id(1L)
                .fullName("Juan Rodriguez")
                .email("jrodriguez@gmail.com")
                .phone("3014598321")
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(patientMapper.toDto(existing)).thenReturn(responseDTO);

        PatientResponseDTO result = patientService.getPatientById(1L);

        assertEquals("Juan Rodriguez", result.fullName());
        assertEquals("jrodriguez@gmail.com", result.email());
        assertEquals("3014598321", result.phone());
    }

    @Test
    void shouldCreatePatient() {
        PatientRequestDTO patientRequestDTO = PatientRequestDTO.builder()
                .fullName("Juan Rodriguez")
                .email("jrodriguez@gmail.com")
                .phone("3014598321")
                .build();

        Patient patient = Patient.builder().id(1L)
                .fullName("Juan Rodriguez")
                .email("jrodriguez@gmail.com")
                .phone("3014598321")
                .build();

        PatientResponseDTO patientResponseDTO = PatientResponseDTO.builder().id(1L)
                .fullName("Juan Rodriguez")
                .email("jrodriguez@gmail.com")
                .phone("3014598321")
                .build();

        when(patientMapper.toEntity(patientRequestDTO)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.toDto(patient)).thenReturn(patientResponseDTO);

        PatientResponseDTO result = patientService.createPatient(patientRequestDTO);

        assertEquals("Juan Rodriguez", result.fullName());
        assertEquals("jrodriguez@gmail.com", result.email());
        assertEquals("3014598321", result.phone());
        verify(patientRepository).save(patient);
    }

    @Test
    void shouldUpdatePatient() {
        PatientRequestDTO requestUpdate = PatientRequestDTO.builder()
                .fullName("Juan Rodriguez")
                .email("jrodriguez@gmail.com")
                .phone("3014598321")
                .build();

        Patient existing = Patient.builder().id(1L)
                .fullName("Jose Hernandez")
                .email("jhernandez@gmail.com")
                .phone("3006671543")
                .build();

        PatientResponseDTO update = PatientResponseDTO.builder()
                .fullName("Jose Hernandez")
                .email("jhernandez@gmail.com")
                .phone("3006671543")
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(patientRepository.save(any())).thenReturn(existing);
        when(patientMapper.toDto(any())).thenReturn(update);

        PatientResponseDTO result = patientService.updatePatient(1L, requestUpdate);

        assertEquals("Jose Hernandez", result.fullName());
        assertEquals("jhernandez@gmail.com", result.email());
        assertEquals("3006671543", result.phone());
    }

    @Test
    void shouldDeletePatient() {
        when(patientRepository.existsById(1L)).thenReturn(true);

        patientService.deletePatient(1L);

        verify(patientRepository).deleteById(1L);
    }

    @Test
    void shouldThrowIfPatientToDeleteNotFound() {
        when(patientRepository.existsById(10L)).thenReturn(false);

        assertThrows(PatientNotFoundException.class, () -> patientService.deletePatient(10L));
    }

    @Test
    void shouldThrowIfPatientToUpdateNotFound() {
        PatientRequestDTO patientRequestDTO = PatientRequestDTO.builder().build();
        when(patientRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.updatePatient(10L, patientRequestDTO));
    }
}