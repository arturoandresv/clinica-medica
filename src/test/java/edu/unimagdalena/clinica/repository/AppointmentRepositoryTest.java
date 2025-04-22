package edu.unimagdalena.clinica.repository;

import edu.unimagdalena.clinica.model.Appointment;
import edu.unimagdalena.clinica.model.ConsultRoom;
import edu.unimagdalena.clinica.model.Doctor;
import edu.unimagdalena.clinica.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;
/*
    @Test
    void shouldSaveAndFindAppointment() {
        Appointment appointment = Appointment.builder()
                .patient(Patient.builder().id(1L).build())
                .doctor(Doctor.builder().id(1L).build())
                .consultRoom(ConsultRoom.builder().id(1L).build())
                .startTime(LocalDateTime.now().plusDays(1L).plusHours(3))
                .endTime(LocalDateTime.now().plusDays(1L).plusHours(4))
                .status()
                .build()

    }
*/


    @Test
    void shouldFindConflictConsultRoom() {
    }

    @Test
    void shouldFindConflictDoctor() {
    }
}