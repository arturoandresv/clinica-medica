package edu.unimagdalena.clinica.repository;

import edu.unimagdalena.clinica.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
}