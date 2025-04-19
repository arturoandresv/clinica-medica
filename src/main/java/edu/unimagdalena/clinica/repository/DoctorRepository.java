package edu.unimagdalena.clinica.repository;

import edu.unimagdalena.clinica.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
