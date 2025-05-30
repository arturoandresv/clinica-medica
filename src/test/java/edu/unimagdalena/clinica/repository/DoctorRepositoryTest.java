package edu.unimagdalena.clinica.repository;

import edu.unimagdalena.clinica.model.Doctor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    void shouldSaveAndFindDoctor() {
        Doctor doctor = Doctor.builder()
                .fullName("Pedro Gonzalez")
                .email("pgonzalez@gmail.com")
                .specialty("Cardiology")
                .availableFrom(LocalTime.now())
                .availableTo(LocalTime.now().plusHours(8))
                .build();

        Doctor saved = doctorRepository.save(doctor);
        Optional<Doctor> result = doctorRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("Pedro Gonzalez", result.get().getFullName());
        assertEquals("pgonzalez@gmail.com", result.get().getEmail());
        assertEquals("Cardiology", result.get().getSpecialty());
    }

    @Test
    void shouldFindAllDoctors() {
        Doctor doctor1 = doctorRepository.save(Doctor.builder()
                .fullName("Pedro Gonzalez")
                .email("pgonzalez@gmail.com")
                .specialty("Cardiology")
                .availableFrom(LocalTime.now())
                .availableTo(LocalTime.now().plusHours(8))
                .build());

        Doctor doctor2 = doctorRepository.save(Doctor.builder()
                .fullName("Laura Medina")
                .email("lmedina@gmail.com")
                .specialty("Dermatology")
                .availableFrom(LocalTime.now())
                .availableTo(LocalTime.now().plusHours(8))
                .build());

        List<Doctor> doctors = doctorRepository.findAll();

        assertFalse(doctors.isEmpty());
        assertTrue(doctors.contains(doctor1));
        assertTrue(doctors.contains(doctor2));
    }

    @Test
    void shouldUpdateDoctor() {
        Doctor doctor = doctorRepository.save(Doctor.builder()
                .fullName("Pedro Gonzalez")
                .email("pgonzalez@gmail.com")
                .specialty("Cardiology")
                .availableFrom(LocalTime.now())
                .availableTo(LocalTime.now().plusHours(8))
                .build());

        doctor.setSpecialty("Internal Medicine");
        Doctor updated = doctorRepository.save(doctor);

        assertEquals("Internal Medicine", updated.getSpecialty());
    }

    @Test
    void shouldDeleteDoctor() {
        Doctor doctor = doctorRepository.save(Doctor.builder()
                .fullName("Pedro Gonzalez")
                .email("pgonzalez@gmail.com")
                .specialty("Cardiology")
                .availableFrom(LocalTime.now())
                .availableTo(LocalTime.now().plusHours(8))
                .build());

        Long id = doctor.getId();
        doctorRepository.deleteById(id);

        assertFalse(doctorRepository.findById(id).isPresent());
    }

    @Test
    void shouldFindBySpecialty() {
        doctorRepository.save(Doctor.builder()
                .fullName("Pedro Gonzalez")
                .email("pgonzalez@gmail.com")
                .specialty("Cardiology")
                .availableFrom(LocalTime.now())
                .availableTo(LocalTime.now().plusHours(8))
                .build());

        doctorRepository.save(Doctor.builder()
                .fullName("Carlos Martinez")
                .email("cmartinez@gmail.com")
                .specialty("Cardiology")
                .availableFrom(LocalTime.now())
                .availableTo(LocalTime.now().plusHours(8))
                .build());

        List<Doctor> cardiologists = doctorRepository.findBySpecialty("Cardiology");

        assertEquals(2, cardiologists.size());
        assertTrue(cardiologists.stream().anyMatch(d -> d.getFullName().equals("Pedro Gonzalez")));
        assertTrue(cardiologists.stream().anyMatch(d -> d.getFullName().equals("Carlos Martinez")));
    }
}