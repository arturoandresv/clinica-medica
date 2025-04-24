package edu.unimagdalena.clinica.service;

import edu.unimagdalena.clinica.dto.request.MedicalRecordRequestCreateDTO;
import edu.unimagdalena.clinica.dto.request.MedicalRecordRequestUpdateDTO;
import edu.unimagdalena.clinica.dto.response.MedicalRecordResponseDTO;

import java.util.List;

public interface MedicalRecordService {
    List<MedicalRecordResponseDTO> getAllMedicalRecords();
    MedicalRecordResponseDTO getMedicalRecordById(Long id);
    MedicalRecordResponseDTO createMedicalRecord(MedicalRecordRequestCreateDTO dto);
    MedicalRecordResponseDTO updateMedicalRecord(Long id, MedicalRecordRequestUpdateDTO dto);

    void deleteMedicalRecord(Long id);
    List<MedicalRecordResponseDTO> getMedicalRecordsByPatientId(Long patientId);
}
