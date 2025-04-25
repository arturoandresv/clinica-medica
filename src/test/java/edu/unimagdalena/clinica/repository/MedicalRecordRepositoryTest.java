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
class MedicalRecordRepositoryTest {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private ConsultRoomRepository consultRoomRepository;
    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    void shouldSaveAndFindMedicalRecord() {
        Patient patient = patientRepository.save(Patient.builder()
                .fullName("Juan Rodriguez")
                .email("jrodriguez@gmail.com")
                .phone("3014598321")
                .build());

        Appointment appointment = appointmentRepository.save(Appointment.builder().build());

        MedicalRecord medicalRecord = MedicalRecord.builder()
                .patient(patient)
                .appointment(appointment)
                .diagnosis("Diabetes")
                .notes("none")
                .build();

        MedicalRecord saved = medicalRecordRepository.save(medicalRecord);
        Optional<MedicalRecord> result = medicalRecordRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("Diabetes", result.get().getDiagnosis());
        assertEquals("none", result.get().getNotes());
    }

    @Test
    void shouldFindAllMedicalRecords() {
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

        Appointment appointment1 = appointmentRepository.save(Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .consultRoom(consultRoom)
                .startTime(LocalDateTime.now().plusDays(1).withHour(9))
                .endTime(LocalDateTime.now().plusDays(1).withHour(10))
                .status(AppointmentStatus.SCHEDULED)
                .build());

        Appointment appointment2 = appointmentRepository.save(Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .consultRoom(consultRoom)
                .startTime(LocalDateTime.now().plusDays(2).withHour(11))
                .endTime(LocalDateTime.now().plusDays(2).withHour(12))
                .status(AppointmentStatus.SCHEDULED)
                .build());

        MedicalRecord record1 = MedicalRecord.builder()
                .patient(patient)
                .appointment(appointment1)
                .diagnosis("Diabetes")
                .notes("First visit")
                .build();

        MedicalRecord record2 = MedicalRecord.builder()
                .patient(patient)
                .appointment(appointment2)
                .diagnosis("Hypertension")
                .notes("Second visit")
                .build();

        medicalRecordRepository.save(record1);
        medicalRecordRepository.save(record2);

        List<MedicalRecord> result = medicalRecordRepository.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldUpdateMedicalRecord() {
        Patient patient = patientRepository.save(Patient.builder()
                .fullName("Juan Rodriguez")
                .email("jrodriguez@gmail.com")
                .phone("3014598321")
                .build());

        Appointment appointment = appointmentRepository.save(Appointment.builder().build());

        MedicalRecord record = medicalRecordRepository.save(MedicalRecord.builder()
                .patient(patient)
                .appointment(appointment)
                .diagnosis("Diabetes")
                .notes("none")
                .build());

        record.setDiagnosis("Diabetes");
        record.setNotes("trimester control");

        MedicalRecord updated = medicalRecordRepository.save(record);

        assertEquals("Diabetes", updated.getDiagnosis());
        assertEquals("trimester control", updated.getNotes());
    }

    @Test
    void shouldDeleteMedicalRecord() {
        Patient patient = patientRepository.save(Patient.builder()
                .fullName("Juan Rodriguez")
                .email("jrodriguez@gmail.com")
                .phone("3014598321")
                .build());

        Appointment appointment = appointmentRepository.save(Appointment.builder().build());

        MedicalRecord record = medicalRecordRepository.save(MedicalRecord.builder()
                .patient(patient)
                .appointment(appointment)
                .diagnosis("Gripe")
                .notes("rest")
                .build());

        Long id = record.getId();
        medicalRecordRepository.deleteById(id);

        assertFalse(medicalRecordRepository.findById(id).isPresent());
    }
}