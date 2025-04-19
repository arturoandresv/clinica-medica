package edu.unimagdalena.clinica.repository;

import edu.unimagdalena.clinica.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
