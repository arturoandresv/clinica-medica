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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    void shouldSaveAndFindAppointment() {
        Appointment appointment = Appointment.builder()
                .patient(Patient.builder().id(1L).build())
                .doctor(Doctor.builder().id(1L).build())
                .consultRoom(ConsultRoom.builder().id(1L).build())
                .startTime(LocalDateTime.now().plusDays(1L).plusHours(3))
                .endTime(LocalDateTime.now().plusDays(1L).plusHours(4))
                .status(AppointmentStatus.SCHEDULED)
                .build();

        Appointment saved = appointmentRepository.save(appointment);
        Optional<Appointment> result = appointmentRepository.findById(saved.getId());

        assertTrue(result.isPresent());

    }

    @Test
    void shouldFindAllAppointments() {

    }

    @Test
    void shouldUpdateAppointment() {

    }

    @Test
    void shouldDeleteAppointment() {

    }

    @Test
    void shouldFindConflictConsultRoom() {
    }

    @Test
    void shouldFindConflictDoctor() {
    }
}