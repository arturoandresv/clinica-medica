package edu.unimagdalena.clinica.service;

import edu.unimagdalena.clinica.dto.request.MedicalRecordRequestDTO;
import edu.unimagdalena.clinica.dto.response.MedicalRecordResponseDTO;

import java.util.List;

public interface MedicalRecordService {
    List<MedicalRecordResponseDTO> getAllMedicalRecords();
    MedicalRecordResponseDTO getMedicalRecordById(Long id);
    MedicalRecordResponseDTO createMedicalRecord(MedicalRecordRequestDTO dto);
    void deleteMedicalRecord(Long id);
    List<MedicalRecordResponseDTO> getMedicalRecordsByPatientId(Long patientId);
}
