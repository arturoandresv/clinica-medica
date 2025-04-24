package edu.unimagdalena.clinica.service;

import edu.unimagdalena.clinica.dto.request.MedicalRecordRequestDTO;
import edu.unimagdalena.clinica.dto.response.MedicalRecordResponseDTO;
import edu.unimagdalena.clinica.exception.AppointmentNotModifiableException;
import edu.unimagdalena.clinica.mapper.MedicalRecordMapper;
import edu.unimagdalena.clinica.model.*;
import edu.unimagdalena.clinica.repository.AppointmentRepository;
import edu.unimagdalena.clinica.repository.MedicalRecordRepository;
import edu.unimagdalena.clinica.repository.PatientRepository;
import edu.unimagdalena.clinica.service.impl.MedicalRecordServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private MedicalRecordMapper medicalRecordMapper;

    @InjectMocks
    private MedicalRecordServiceImpl medicalRecordService;

    @Test
    void shouldCreateMedicalRecord() {
        MedicalRecordRequestDTO requestDTO = MedicalRecordRequestDTO.builder()
                .appointmentId(1L)
                .patientId(1L)
                .diagnosis("Paciente con fiebre y dolor muscular")
                .build();

        Patient patient = Patient.builder().id(1L).build();
        Appointment appointment = Appointment.builder()
                .id(1L)
                .status(AppointmentStatus.COMPLETED)
                .build();

        MedicalRecord medicalRecord = MedicalRecord.builder().id(1L).diagnosis("Paciente con fiebre").build();
        MedicalRecordResponseDTO responseDTO = MedicalRecordResponseDTO.builder().id(1L).diagnosis("Paciente con fiebre").build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(medicalRecordMapper.toEntity(requestDTO)).thenReturn(medicalRecord);
        when(medicalRecordRepository.save(medicalRecord)).thenReturn(medicalRecord);
        when(medicalRecordMapper.toDTO(medicalRecord)).thenReturn(responseDTO);

        MedicalRecordResponseDTO result = medicalRecordService.createMedicalRecord(requestDTO);

        assertEquals(1L, result.id());
        verify(medicalRecordRepository).save(medicalRecord);
    }

    @Test
    void shouldThrowIfAppointmentNotCompleted() {
        Appointment appointment = Appointment.builder()
                .id(1L)
                .status(AppointmentStatus.SCHEDULED)
                .build();
        Patient patient = Patient.builder().id(1L).build();

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        MedicalRecordRequestDTO dto = MedicalRecordRequestDTO.builder()
                .appointmentId(1L)
                .patientId(1L)
                .diagnosis("Dolor de cabeza")
                .build();

        assertThrows(AppointmentNotModifiableException.class,
                () -> medicalRecordService.createMedicalRecord(dto));
    }

    @Test
    void shouldGetAllMedicalRecords() {
        MedicalRecord record = MedicalRecord.builder().id(1L).diagnosis("Historial 1").build();
        MedicalRecordResponseDTO dto = MedicalRecordResponseDTO.builder().id(1L).diagnosis("Historial 1").build();

        when(medicalRecordRepository.findAll()).thenReturn(List.of(record));
        when(medicalRecordMapper.toDTO(record)).thenReturn(dto);

        List<MedicalRecordResponseDTO> result = medicalRecordService.getAllMedicalRecords();

        assertEquals(1, result.size());
        assertEquals("Historial 1", result.get(0).diagnosis());
    }

    @Test
    void shouldGetMedicalRecordById() {
        MedicalRecord record = MedicalRecord.builder().id(1L).notes("Registro único").build();
        MedicalRecordResponseDTO dto = MedicalRecordResponseDTO.builder().id(1L).notes("Registro único").build();

        when(medicalRecordRepository.findById(1L)).thenReturn(Optional.of(record));
        when(medicalRecordMapper.toDTO(record)).thenReturn(dto);

        MedicalRecordResponseDTO result = medicalRecordService.getMedicalRecordById(1L);

        assertEquals("Registro único", result.notes());
    }

    @Test
    void shouldGetRecordsByPatientId() {
        MedicalRecord record = MedicalRecord.builder().id(1L).notes("Control general").build();
        MedicalRecordResponseDTO dto = MedicalRecordResponseDTO.builder().id(1L).notes("Control general").build();

        when(medicalRecordRepository.findByPatientId(1L)).thenReturn(List.of(record));
        when(medicalRecordMapper.toDTO(record)).thenReturn(dto);

        List<MedicalRecordResponseDTO> result = medicalRecordService.getMedicalRecordsByPatientId(1L);

        assertEquals(1, result.size());
        assertEquals("Control general", result.get(0).notes());
    }

    @Test
    void shouldDeleteMedicalRecord() {
        when(medicalRecordRepository.existsById(1L)).thenReturn(true);

        medicalRecordService.deleteMedicalRecord(1L);

        verify(medicalRecordRepository).deleteById(1L);
    }
}