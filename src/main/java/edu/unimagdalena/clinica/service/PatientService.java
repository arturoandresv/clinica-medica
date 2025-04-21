package edu.unimagdalena.clinica.service;

import edu.unimagdalena.clinica.dto.request.PatientRequestDTO;
import edu.unimagdalena.clinica.dto.response.PatientResponseDTO;

import java.util.List;

public interface PatientService {
    List<PatientResponseDTO> getAllPatients();
    PatientResponseDTO getPatientById(Long id);
    PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO);
    PatientResponseDTO updatePatient(Long id, PatientRequestDTO patientRequestDTO);
    void deletePatient(Long id);
}
