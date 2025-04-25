package edu.unimagdalena.clinica.service;

import edu.unimagdalena.clinica.dto.request.AppointmentRequestCreateDTO;
import edu.unimagdalena.clinica.dto.request.AppointmentRequestUpdateDTO;
import edu.unimagdalena.clinica.dto.request.PatientRequestDTO;
import edu.unimagdalena.clinica.dto.response.AppointmentResponseDTO;
import edu.unimagdalena.clinica.exception.AppointmentConflictException;
import edu.unimagdalena.clinica.exception.DoctorScheduleConflictException;
import edu.unimagdalena.clinica.exception.notfound.AppointmentNotFoundException;
import edu.unimagdalena.clinica.exception.notfound.PatientNotFoundException;
import edu.unimagdalena.clinica.mapper.AppointmentMapper;
import edu.unimagdalena.clinica.model.*;
import edu.unimagdalena.clinica.repository.AppointmentRepository;
import edu.unimagdalena.clinica.repository.ConsultRoomRepository;
import edu.unimagdalena.clinica.repository.DoctorRepository;
import edu.unimagdalena.clinica.repository.PatientRepository;
import edu.unimagdalena.clinica.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private ConsultRoomRepository consultRoomRepository;
    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    void shouldGetAllAppointments() {
        Appointment appointment = Appointment.builder().id(1L).build();
        AppointmentResponseDTO appointmentResponseDTO = AppointmentResponseDTO.builder().id(1L).build();

        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));
        when(appointmentMapper.toDTO(appointment)).thenReturn(appointmentResponseDTO);

        List<AppointmentResponseDTO> result = appointmentService.getAllAppointments();

        assertEquals(1, result.size());
    }

    @Test
    void shouldGetAppointmentById() {
        Appointment appointment = Appointment.builder().id(1L).build();
        AppointmentResponseDTO appointmentResponseDTO = AppointmentResponseDTO.builder().id(1L).build();

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentMapper.toDTO(appointment)).thenReturn(appointmentResponseDTO);

        AppointmentResponseDTO result = appointmentService.getAppointmentById(1L);

        assertEquals(appointmentResponseDTO, result);
    }

    @Test
    void shouldCreateAppointment() {
        AppointmentRequestCreateDTO appointmentRequestCreateDTO = AppointmentRequestCreateDTO.builder()
                .patientId(1L).doctorId(1L).consultRoomId(1L)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
                .build();

        Patient patient = Patient.builder().id(1L).build();
        Doctor doctor = Doctor.builder().id(1L)
                .availableFrom(LocalTime.now().minusHours(2))
                .availableTo(LocalTime.now().plusHours(2))
                .build();
        ConsultRoom consultRoom = ConsultRoom.builder().id(1L).build();
        Appointment appointment = Appointment.builder()
                .patient(patient).doctor(doctor).consultRoom(consultRoom)
                .build();

        AppointmentResponseDTO appointmentResponseDTO = AppointmentResponseDTO.builder().id(1L)
                .patientId(1L).doctorId(1L).consultRoomId(1L)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1))
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(consultRoomRepository.findById(1L)).thenReturn(Optional.of(consultRoom));
        when(appointmentRepository.findConflictDoctor(eq(1L), any(), any())).thenReturn(List.of());
        when(appointmentRepository.findConflictConsultRoom(eq(1L), any(), any())).thenReturn(List.of());
        when(appointmentMapper.toEntity(any())).thenReturn(appointment);
        when(appointmentRepository.save(any())).thenReturn(appointment);
        when(appointmentMapper.toDTO(any())).thenReturn(appointmentResponseDTO);

        AppointmentResponseDTO result = appointmentService.createAppointment(appointmentRequestCreateDTO);

        assertEquals(1L, result.patientId());
        assertEquals(1L, result.doctorId());
        assertEquals(1L, result.consultRoomId());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void shouldUpdateAppointment() {
        LocalDateTime now = LocalDateTime.of(2025, 4, 20, 10, 0);
        LocalDateTime existingStart = now.plusDays(1);
        LocalDateTime existingEnd = existingStart.plusHours(1);
        LocalDateTime newStart = now.plusDays(2).minusHours(1);
        LocalDateTime newEnd = now.plusDays(2);

        LocalDateTime updatedDTOStart = now.plusDays(2).plusHours(3);
        LocalDateTime updatedDTOEnd = now.plusDays(2).plusHours(4);

        Appointment existing = Appointment.builder().id(1L)
                .patient(Patient.builder().id(1L).build())
                .doctor(Doctor.builder().id(1L)
                        .availableFrom(LocalTime.of(8, 0))
                        .availableTo(LocalTime.of(18, 0)).build())
                .consultRoom(ConsultRoom.builder().id(1L).build())
                .startTime(existingStart)
                .endTime(existingEnd)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        AppointmentRequestUpdateDTO requestUpdate = AppointmentRequestUpdateDTO.builder()
                .startTime(newStart)
                .endTime(newEnd)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        AppointmentResponseDTO update = AppointmentResponseDTO.builder()
                .patientId(1L)
                .doctorId(1L)
                .consultRoomId(1L)
                .startTime(updatedDTOStart)
                .endTime(updatedDTOEnd)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(appointmentRepository.findConflictDoctor(eq(1L), any(), any())).thenReturn(List.of());
        when(appointmentRepository.findConflictConsultRoom(eq(1L), any(), any())).thenReturn(List.of());
        when(appointmentRepository.save(any())).thenReturn(existing);
        when(appointmentMapper.toDTO(any())).thenReturn(update);

        AppointmentResponseDTO result = appointmentService.updateAppointment(1L, requestUpdate);

        assertEquals(updatedDTOStart, result.startTime());
        assertEquals(updatedDTOEnd, result.endTime());
        assertEquals(AppointmentStatus.SCHEDULED, result.status());
        assertEquals(1L, result.doctorId());
        assertEquals(1L, result.patientId());
        assertEquals(1L, result.consultRoomId());
    }

    @Test
    void shouldCancelAppointment() {
        Appointment appointment = Appointment.builder()
                .id(1L)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        Appointment updatedAppointment = Appointment.builder()
                .id(1L)
                .status(AppointmentStatus.CANCELED)
                .build();

        AppointmentResponseDTO appointmentResponseDTO = AppointmentResponseDTO.builder()
                .status(AppointmentStatus.CANCELED)
                .build();

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(updatedAppointment);
        when(appointmentMapper.toDTO(updatedAppointment)).thenReturn(appointmentResponseDTO);

        AppointmentResponseDTO result = appointmentService.cancelAppointment(1L);

        assertNotNull(result);
        assertEquals(AppointmentStatus.CANCELED, result.status());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void shouldThrowIfAppointmentConflictExists() {
        AppointmentRequestCreateDTO appointmentRequestCreateDTO = AppointmentRequestCreateDTO.builder()
                .patientId(1L).doctorId(1L).consultRoomId(1L)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
                .build();

        Patient patient = Patient.builder().id(1L).build();
        Doctor doctor = Doctor.builder().id(1L)
                .availableFrom(LocalTime.now().minusHours(2))
                .availableTo(LocalTime.now().plusHours(2))
                .build();
        ConsultRoom consultRoom = ConsultRoom.builder().id(1L).build();
        Appointment appointment = Appointment.builder()
                .patient(patient).doctor(doctor).consultRoom(consultRoom)
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(consultRoomRepository.findById(1L)).thenReturn(Optional.of(consultRoom));
        when(appointmentRepository.findConflictDoctor(eq(1L), any(), any())).thenReturn(List.of(appointment));
        when(appointmentRepository.findConflictConsultRoom(eq(1L), any(), any())).thenReturn(List.of(appointment));

        assertThrows(AppointmentConflictException.class, () -> appointmentService.createAppointment(appointmentRequestCreateDTO));
    }
    @Test
    void shouldThrowIfDoctorScheduleConflict() {
        LocalDateTime start = LocalDateTime.of(2025, 4, 21, 19, 0);
        LocalDateTime end = start.plusHours(1);

        AppointmentRequestCreateDTO appointmentRequestCreateDTO = AppointmentRequestCreateDTO.builder()
                .patientId(1L).doctorId(1L).consultRoomId(1L)
                .startTime(start)
                .endTime(end)
                .build();

        Patient patient = Patient.builder().id(1L).build();
        Doctor doctor = Doctor.builder()
                .id(1L)
                .availableFrom(LocalTime.of(8, 0))
                .availableTo(LocalTime.of(17, 0))
                .build();
        ConsultRoom consultRoom = ConsultRoom.builder().id(1L).build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(consultRoomRepository.findById(1L)).thenReturn(Optional.of(consultRoom));

        assertThrows(DoctorScheduleConflictException.class, () ->
                appointmentService.createAppointment(appointmentRequestCreateDTO));
    }

    @Test
    void shouldThrowIfAppointmentToUpdateNotFound() {
        AppointmentRequestUpdateDTO appointmentRequestUpdateDTO = AppointmentRequestUpdateDTO.builder().build();
        when(appointmentRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.updateAppointment(10L, appointmentRequestUpdateDTO));
    }


}