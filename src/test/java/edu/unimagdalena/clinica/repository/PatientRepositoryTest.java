package edu.unimagdalena.clinica.repository;

import edu.unimagdalena.clinica.model.Patient;
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
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void shouldSaveAndFindPatient() {
        Patient patient = Patient.builder()
                .fullName("Juan Rodriguez")
                .email("jrodriguez@gmail.com")
                .phone("3014598321")
                .build();

        Patient saved = patientRepository.save(patient);
        Optional<Patient> result = patientRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("Juan Rodriguez", result.get().getFullName());
        assertEquals("jrodriguez@gmail.com", result.get().getEmail());
        assertEquals("3014598321", result.get().getPhone());
    }

    @Test
    void shouldFindAllPatients() {
        Patient patient1 = patientRepository.save(Patient.builder().fullName("Juan Rodriguez").email("jrodriguez@gmail.com").phone("3014598321").build());
        Patient patient2 = patientRepository.save(Patient.builder().fullName("Jose Hernandez").email("jhernandez@gmail.com").phone("3006671543").build());

        List<Patient> patients = patientRepository.findAll();

        assertFalse(patients.isEmpty());
        assertTrue(patients.contains(patient1));
        assertTrue(patients.contains(patient2));
    }

    @Test
    void shouldUpdatePatient() {
        Patient patient = patientRepository.save(Patient.builder().fullName("Juan Rodriguez").email("jrodriguez@gmail.com").phone("3014598321").build());

        patient.setPhone("3009834123");
        Patient updated = patientRepository.save(patient);

        assertEquals("3009834123", updated.getPhone());
    }

    @Test
    void shouldDeletePatient() {
        Patient patient = patientRepository.save(Patient.builder().fullName("Juan Rodriguez").email("jrodriguez@gmail.com").phone("3014598321").build());

        Long id = patient.getId();
        patientRepository.deleteById(id);

        assertFalse(patientRepository.findById(id).isPresent());
    }

}