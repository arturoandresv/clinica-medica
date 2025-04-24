package edu.unimagdalena.clinica.repository;

import edu.unimagdalena.clinica.model.Doctor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.TestcontainersConfiguration;

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
        doctorRepository.save(Doctor.builder()
                .fullName("Pedro Gonzalez")
                .email("pgonzalez@gmail.com")
                .specialty("Cardiology")
                .build());

        doctorRepository.save(Doctor.builder()
                .fullName("Laura Medina")
                .email("lmedina@gmail.com")
                .specialty("Dermatology")
                .build());

        List<Doctor> doctors = doctorRepository.findAll();

        assertEquals(2, doctors.size());
    }

    @Test
    void shouldUpdateDoctor() {
        Doctor doctor = doctorRepository.save(Doctor.builder()
                .fullName("Pedro Gonzalez")
                .email("pgonzalez@gmail.com")
                .specialty("Cardiology")
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
                .build());

        doctorRepository.save(Doctor.builder()
                .fullName("Carlos Martinez")
                .email("cmartinez@gmail.com")
                .specialty("Cardiology")
                .build());

        List<Doctor> cardiologists = doctorRepository.findBySpecialty("Cardiology");

        assertEquals(2, cardiologists.size());
        assertTrue(cardiologists.stream().anyMatch(d -> d.getFullName().equals("Pedro Gonzalez")));
        assertTrue(cardiologists.stream().anyMatch(d -> d.getFullName().equals("Carlos Martinez")));
    }
}