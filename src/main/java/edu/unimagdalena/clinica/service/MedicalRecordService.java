package edu.unimagdalena.clinica.service;

import edu.unimagdalena.clinica.dto.response.MedicalRecordResponseDTO;

import java.util.List;

public interface MedicalRecordService {
    List<MedicalRecordResponseDTO> getAllMedicalRecords();
    MedicalRecordResponseDTO getMedicalRecordById(Long id);
    MedicalRecordResponseDTO createAppointment(MedicalRecordResponseDTO appointmentDTO);
    MedicalRecordResponseDTO updateAppointment(Long id, MedicalRecordResponseDTO appointmentDTO);
    void deleteAppointment(Long id);
}
