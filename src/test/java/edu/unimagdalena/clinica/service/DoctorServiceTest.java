package edu.unimagdalena.clinica.service;

import edu.unimagdalena.clinica.dto.request.DoctorRequestDTO;
import edu.unimagdalena.clinica.dto.request.PatientRequestDTO;
import edu.unimagdalena.clinica.dto.response.DoctorResponseDTO;
import edu.unimagdalena.clinica.exception.notfound.DoctorNotFoundException;
import edu.unimagdalena.clinica.exception.notfound.PatientNotFoundException;
import edu.unimagdalena.clinica.mapper.DoctorMapper;
import edu.unimagdalena.clinica.model.Doctor;
import edu.unimagdalena.clinica.repository.DoctorRepository;
import edu.unimagdalena.clinica.service.impl.DoctorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    @Test
    void shouldGetAllDoctors() {
        Doctor doctor = Doctor.builder().id(1L)
                .fullName("Pedro Gonzalez")
                .email("pgonzalez@gmail.com")
                .specialty("Cardiology")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(16, 0))
                .build();

        DoctorResponseDTO doctorResponseDTO = DoctorResponseDTO.builder()
                .id(1L)
                .fullName("Pedro Gonzalez")
                .email("pgonzalez@gmail.com")
                .specialty("Cardiology")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(16, 0))
                .build();

        when(doctorRepository.findAll()).thenReturn(List.of(doctor));
        when(doctorMapper.toDto(doctor)).thenReturn(doctorResponseDTO);

        List<DoctorResponseDTO> result = doctorService.getAllDoctors();

        assertEquals(1, result.size());
        assertEquals("Pedro Gonzalez", result.getFirst().fullName());
        assertEquals("pgonzalez@gmail.com", result.getFirst().email());
        assertEquals("Cardiology", result.getFirst().specialty());
    }

    @Test
    void shouldGetDoctorById() {
        Doctor doctor = Doctor.builder().id(1L)
                .fullName("Pedro Gonzalez")
                .email("pgonzalez@gmail.com")
                .specialty("Cardiology")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(16, 0))
                .build();
        DoctorResponseDTO doctorResponseDTO = DoctorResponseDTO.builder().id(1L)
                .fullName("Pedro Gonzalez")
                .email("pgonzalez@gmail.com")
                .specialty("Cardiology")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(16, 0))
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDto(doctor)).thenReturn(doctorResponseDTO);

        DoctorResponseDTO result = doctorService.getDoctorById(1L);

        assertEquals("Pedro Gonzalez", result.fullName());
        assertEquals("pgonzalez@gmail.com", result.email());
        assertEquals("Cardiology", result.specialty());
    }

    @Test
    void shouldCreateDoctor() {
        DoctorRequestDTO requestDTO = DoctorRequestDTO.builder()
                .fullName("Pedro Gonzalez")
                .email("pgonzalez@gmail.com")
                .specialty("Cardiology")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(16, 0))
                .build();

        Doctor doctor = Doctor.builder().id(1L)
                .fullName("Pedro Gonzalez")
                .email("pgonzalez@gmail.com")
                .specialty("Cardiology")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(16, 0))
                .build();

        DoctorResponseDTO responseDTO = DoctorResponseDTO.builder()
                .id(1L)
                .fullName("Pedro Gonzalez")
                .email("pgonzalez@gmail.com")
                .specialty("Cardiology")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(16, 0))
                .build();

        when(doctorMapper.toEntity(requestDTO)).thenReturn(doctor);
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(doctorMapper.toDto(doctor)).thenReturn(responseDTO);

        DoctorResponseDTO result = doctorService.createDoctor(requestDTO);

        assertEquals("Pedro Gonzalez", result.fullName());
        assertEquals("pgonzalez@gmail.com", result.email());
        assertEquals("Cardiology", result.specialty());
        verify(doctorRepository).save(doctor);
    }

    @Test
    void shouldUpdateDoctor() {
        Doctor existing = Doctor.builder().id(1L)
                .fullName("Pedro Gonzalez")
                .email("pgonzalez@gmail.com")
                .specialty("Cardiology")
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(16, 0))
                .build();

        DoctorRequestDTO updateDTO = DoctorRequestDTO.builder()
                .fullName("Pedro G.")
                .email("pedrog@gmail.com")
                .specialty("Neurology")
                .availableFrom(LocalTime.of(9, 0))
                .availableTo(LocalTime.of(17, 0))
                .build();

        Doctor updated = Doctor.builder().id(1L)
                .fullName("Pedro G.")
                .email("pedrog@gmail.com")
                .specialty("Neurology")
                .availableFrom(LocalTime.of(9, 0))
                .availableTo(LocalTime.of(17, 0))
                .build();

        DoctorResponseDTO responseDTO = DoctorResponseDTO.builder()
                .id(1L)
                .fullName("Pedro G.")
                .email("pedrog@gmail.com")
                .specialty("Neurology")
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(doctorRepository.save(any())).thenReturn(updated);
        when(doctorMapper.toDto(updated)).thenReturn(responseDTO);

        DoctorResponseDTO result = doctorService.updateDoctor(1L, updateDTO);

        assertEquals("Pedro G.", result.fullName());
        assertEquals("pedrog@gmail.com", result.email());
        assertEquals("Neurology", result.specialty());
    }

    @Test
    void shouldDeleteDoctor() {
        when(doctorRepository.existsById(1L)).thenReturn(true);

        doctorService.deleteDoctor(1L);

        verify(doctorRepository).deleteById(1L);
    }

    @Test
    void shouldFindBySpecialty() {
        Doctor doctor = Doctor.builder().id(1L)
                .fullName("Pedro Gonzalez")
                .email("pgonzalez@gmail.com")
                .specialty("Cardiology")
                .build();

        DoctorResponseDTO dto = DoctorResponseDTO.builder().id(1L).specialty("Cardiology").build();

        when(doctorRepository.findBySpecialty("Cardiology")).thenReturn(List.of(doctor));
        when(doctorMapper.toDto(doctor)).thenReturn(dto);

        List<DoctorResponseDTO> result = doctorService.findBySpecialty("Cardiology");

        assertEquals(1, result.size());
        assertEquals("Cardiology", result.getFirst().specialty());
    }

    @Test
    void shouldThrowIfDoctorToDeleteNotFound() {
        when(doctorRepository.existsById(10L)).thenReturn(false);

        assertThrows(DoctorNotFoundException.class, () -> doctorService.deleteDoctor(10L));
    }

    @Test
    void shouldThrowIfDoctorToUpdateNotFound() {
        DoctorRequestDTO doctorRequestDTO = DoctorRequestDTO.builder().build();
        when(doctorRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> doctorService.updateDoctor(10L, doctorRequestDTO));
    }
}