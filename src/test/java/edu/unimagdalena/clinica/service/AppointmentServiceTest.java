package edu.unimagdalena.clinica.service;

import edu.unimagdalena.clinica.dto.AppointmentDTO;
import edu.unimagdalena.clinica.exception.AppointmentConflictException;
import edu.unimagdalena.clinica.exception.DoctorScheduleConflictException;
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
        AppointmentDTO appointmentDTO = AppointmentDTO.builder().id(1L).build();

        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));
        when(appointmentMapper.toDTO(appointment)).thenReturn(appointmentDTO);

        List<AppointmentDTO> result = appointmentService.getAllAppointments();

        assertEquals(1, result.size());
    }

    @Test
    void shouldGetAppointmentById() {
        Appointment appointment = Appointment.builder().id(1L).build();
        AppointmentDTO appointmentDTO = AppointmentDTO.builder().id(1L).build();

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentMapper.toDTO(appointment)).thenReturn(appointmentDTO);

        AppointmentDTO result = appointmentService.getAppointmentById(1L);

        assertEquals(appointmentDTO, result);
    }

    @Test
    void shouldCreateAppointment() {
        AppointmentDTO appointmentDTO = AppointmentDTO.builder()
                .patientId(1L).doctorId(1L).consultRoomId(1L)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
                .status(AppointmentStatus.SCHEDULED)
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
        when(appointmentRepository.findConflictDoctor(eq(1L), any(), any())).thenReturn(List.of());
        when(appointmentRepository.findConflictConsultRoom(eq(1L), any(), any())).thenReturn(List.of());
        when(appointmentMapper.toEntity(any())).thenReturn(appointment);
        when(appointmentRepository.save(any())).thenReturn(appointment);
        when(appointmentMapper.toDTO(any())).thenReturn(appointmentDTO);

        AppointmentDTO result = appointmentService.createAppointment(appointmentDTO);

        assertEquals(1L, result.patientId());
        assertEquals(1L, result.doctorId());
        assertEquals(1L, result.consultRoomId());
    }

    @Test
    void shouldUpdateAppointment() {
        Appointment existing = Appointment.builder().id(1L)
                .patient(Patient.builder().id(1L).build())
                .doctor(Doctor.builder().id(1L)
                        .availableFrom(LocalTime.now().minusHours(2))
                        .availableTo(LocalTime.now().plusHours(2)).build())
                .consultRoom(ConsultRoom.builder().id(1L).build())
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
                .status(AppointmentStatus.SCHEDULED)
                .build();

        AppointmentDTO updateDTO = AppointmentDTO.builder()
                .patientId(1L).doctorId(2L).consultRoomId(2L)
                .startTime(LocalDateTime.now().plusDays(2).plusHours(3))
                .endTime(LocalDateTime.now().plusDays(2).plusHours(4))
                .status(AppointmentStatus.SCHEDULED)
                .build();

        Patient patient = Patient.builder().id(1L).build();
        Doctor doctor = Doctor.builder().id(2L)
                .availableFrom(LocalTime.now())
                .availableTo(LocalTime.now().plusHours(5)).build();
        ConsultRoom consultRoom = ConsultRoom.builder().id(2L).build();

        Appointment update = Appointment.builder().id(1L)
                .patient(patient)
                .doctor(doctor)
                .consultRoom(consultRoom)
                .startTime(LocalDateTime.now().plusDays(2).plusHours(3))
                .endTime(LocalDateTime.now().plusDays(2).plusHours(4))
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(consultRoomRepository.findById(2L)).thenReturn(Optional.of(consultRoom));
        when(appointmentRepository.findConflictDoctor(eq(2L), any(), any())).thenReturn(List.of());
        when(appointmentRepository.findConflictConsultRoom(eq(2L), any(), any())).thenReturn(List.of());
        when(appointmentRepository.save(existing)).thenReturn(update);
        when(appointmentMapper.toDTO(update)).thenReturn(updateDTO);

        AppointmentDTO result = appointmentService.updateAppointment(1L, updateDTO);

        assertEquals(1L, result.patientId());
        assertEquals(2L, result.doctorId());
        assertEquals(2L, result.consultRoomId());

    }

    @Test
    void shouldDeleteAppointment() {
        when(appointmentRepository.existsById(1L)).thenReturn(true);

        appointmentService.deleteAppointment(1L);

        verify(appointmentRepository).deleteById(1L);
    }

    @Test
    void shouldThrowIfAppointmentConflictExists() {
        AppointmentDTO appointmentDTO = AppointmentDTO.builder()
                .patientId(1L).doctorId(1L).consultRoomId(1L)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
                .status(AppointmentStatus.SCHEDULED)
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

        assertThrows(AppointmentConflictException.class, () -> appointmentService.createAppointment(appointmentDTO));
    }
    @Test
    void shouldThrowIfDoctorScheduleConflict() {
        LocalDateTime start = LocalDateTime.of(2025, 4, 21, 19, 0);
        LocalDateTime end = start.plusHours(1);

        AppointmentDTO appointmentDTO = AppointmentDTO.builder()
                .patientId(1L).doctorId(1L).consultRoomId(1L)
                .startTime(start)
                .endTime(end)
                .status(AppointmentStatus.SCHEDULED)
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
                appointmentService.createAppointment(appointmentDTO));
    }
}