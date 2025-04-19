package edu.unimagdalena.clinica.repository;

import edu.unimagdalena.clinica.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
