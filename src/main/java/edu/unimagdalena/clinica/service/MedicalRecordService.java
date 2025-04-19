package edu.unimagdalena.clinica.service;

import edu.unimagdalena.clinica.dto.MedicalRecordDTO;

import java.util.List;

public interface MedicalRecordService {
    List<MedicalRecordDTO> getAllMedicalRecords();
    MedicalRecordDTO getMedicalRecordById(Long id);
    MedicalRecordDTO createAppointment(MedicalRecordDTO appointmentDTO);
    MedicalRecordDTO updateAppointment(Long id, MedicalRecordDTO appointmentDTO);
    void deleteAppointment(Long id);
}
