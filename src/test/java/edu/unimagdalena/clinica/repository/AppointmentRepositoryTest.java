package edu.unimagdalena.clinica.repository;

import edu.unimagdalena.clinica.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ConsultRoomRepository consultRoomRepository;

    @Test
    void shouldSaveAndFindAppointment() {

        Patient patient = patientRepository.save(Patient.builder()
                .fullName("Juan Rodriguez")
                .email("jrodriguez@gmail.com")
                .phone("3014598321")
                .build());
        Doctor doctor = doctorRepository.save(Doctor.builder()
                .fullName("Pedro Gonzalez")
                .email("pgonzalez@gmail.com")
                .specialty("Cardiology")
                .build());
        ConsultRoom consultRoom = consultRoomRepository.save(ConsultRoom.builder().name("Consultorio A").build());

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .consultRoom(consultRoom)
                .startTime(LocalDateTime.now().plusDays(1L).plusHours(3))
                .endTime(LocalDateTime.now().plusDays(1L).plusHours(4))
                .status(AppointmentStatus.SCHEDULED)
                .build();

        Appointment saved = appointmentRepository.save(appointment);
        Optional<Appointment> result = appointmentRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("Juan Rodriguez", result.get().getPatient().getFullName());
        assertEquals("Pedro Gonzalez", result.get().getDoctor().getFullName());
        assertEquals("Consultorio A", result.get().getConsultRoom().getName());
    }

    @Test
    void shouldFindAllAppointments() {
        Patient patient1 = patientRepository.save(Patient.builder().fullName("Juan Rodriguez").email("jrodriguez@gmail.com").phone("3014598321").build());
        Doctor doctor1 = doctorRepository.save(Doctor.builder().fullName("Pedro Gonzalez").email("pgonzalez@gmail.com").specialty("Cardiology").build());
        ConsultRoom consultRoom1 = consultRoomRepository.save(ConsultRoom.builder().name("Consultorio A").build());

        Appointment appointment1 = Appointment.builder()
                .patient(patient1)
                .doctor(doctor1)
                .consultRoom(consultRoom1)
                .startTime(LocalDateTime.now().plusDays(1L).withHour(10))
                .endTime(LocalDateTime.now().plusDays(1L).withHour(11))
                .status(AppointmentStatus.SCHEDULED)
                .build();

        Appointment appointment2 = Appointment.builder()
                .patient(patient1)
                .doctor(doctor1)
                .consultRoom(consultRoom1)
                .startTime(LocalDateTime.now().plusDays(2L).withHour(12))
                .endTime(LocalDateTime.now().plusDays(2L).withHour(13))
                .status(AppointmentStatus.SCHEDULED)
                .build();

        appointmentRepository.save(appointment1);
        appointmentRepository.save(appointment2);

        List<Appointment> appointments = appointmentRepository.findAll();

        assertEquals(2, appointments.size());
    }

    @Test
    void shouldUpdateAppointment() {
        Patient patient = patientRepository.save(Patient.builder().fullName("Juan Rodriguez").email("jrodriguez@gmail.com").phone("3014598321").build());
        Doctor doctor = doctorRepository.save(Doctor.builder().fullName("Pedro Gonzalez").email("pgonzalez@gmail.com").specialty("Cardiology").build());
        ConsultRoom consultRoom = consultRoomRepository.save(ConsultRoom.builder().name("Consultorio A").build());

        Appointment appointment = appointmentRepository.save(Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .consultRoom(consultRoom)
                .startTime(LocalDateTime.now().plusDays(1).withHour(10))
                .endTime(LocalDateTime.now().plusDays(1).withHour(11))
                .status(AppointmentStatus.SCHEDULED)
                .build());

        appointment.setStatus(AppointmentStatus.CANCELED);
        Appointment updated = appointmentRepository.save(appointment);

        assertEquals(AppointmentStatus.CANCELED, updated.getStatus());
    }

    @Test
    void shouldDeleteAppointment() {
        Patient patient = patientRepository.save(Patient.builder().fullName("Juan Rodriguez").email("jrodriguez@gmail.com").phone("3014598321").build());
        Doctor doctor = doctorRepository.save(Doctor.builder().fullName("Pedro Gonzalez").email("pgonzalez@gmail.com").specialty("Cardiology").build());
        ConsultRoom consultRoom = consultRoomRepository.save(ConsultRoom.builder().name("Consultorio A").build());

        Appointment appointment = appointmentRepository.save(Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .consultRoom(consultRoom)
                .startTime(LocalDateTime.now().plusDays(1).withHour(10))
                .endTime(LocalDateTime.now().plusDays(1).withHour(11))
                .status(AppointmentStatus.SCHEDULED)
                .build());

        Long id = appointment.getId();
        appointmentRepository.deleteById(id);

        assertFalse(appointmentRepository.findById(id).isPresent());
    }

    @Test
    void shouldFindConflictConsultRoom() {
        ConsultRoom consultRoom = consultRoomRepository.save(ConsultRoom.builder().name("Consultorio A").build());
        Patient patient = patientRepository.save(Patient.builder().fullName("Juan Rodriguez").email("jrodriguez@gmail.com").phone("3014598321").build());
        Doctor doctor = doctorRepository.save(Doctor.builder().fullName("Pedro Gonzalez").email("pgonzalez@gmail.com").specialty("Cardiology").build());

        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(10);
        LocalDateTime end = LocalDateTime.now().plusDays(1).withHour(11);

        appointmentRepository.save(Appointment.builder()
                .consultRoom(consultRoom)
                .doctor(doctor)
                .patient(patient)
                .startTime(start)
                .endTime(end)
                .status(AppointmentStatus.SCHEDULED)
                .build());

        List<Appointment> conflicts = appointmentRepository.findConflictConsultRoom(consultRoom.getId(), start, end);

        assertFalse(conflicts.isEmpty());
    }

    @Test
    void shouldFindConflictDoctor() {
        Patient patient = patientRepository.save(Patient.builder().fullName("Juan Rodriguez").email("jrodriguez@gmail.com").phone("3014598321").build());
        Doctor doctor = doctorRepository.save(Doctor.builder().fullName("Pedro Gonzalez").email("pgonzalez@gmail.com").specialty("Cardiology").build());
        ConsultRoom consultRoom = consultRoomRepository.save(ConsultRoom.builder().name("Consultorio B").build());

        LocalDateTime start = LocalDateTime.now().plusDays(1).withHour(14);
        LocalDateTime end = LocalDateTime.now().plusDays(1).withHour(15);

        appointmentRepository.save(Appointment.builder()
                .consultRoom(consultRoom)
                .doctor(doctor)
                .patient(patient)
                .startTime(start)
                .endTime(end)
                .status(AppointmentStatus.SCHEDULED)
                .build());

        List<Appointment> conflicts = appointmentRepository.findConflictDoctor(doctor.getId(), start, end);

        assertFalse(conflicts.isEmpty());
    }
}