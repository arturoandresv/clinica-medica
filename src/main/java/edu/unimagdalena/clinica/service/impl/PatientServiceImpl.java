package edu.unimagdalena.clinica.service.impl;

import edu.unimagdalena.clinica.dto.request.PatientRequestDTO;
import edu.unimagdalena.clinica.dto.response.PatientResponseDTO;
import edu.unimagdalena.clinica.exception.notfound.PatientNotFoundException;
import edu.unimagdalena.clinica.mapper.PatientMapper;
import edu.unimagdalena.clinica.model.Patient;
import edu.unimagdalena.clinica.repository.PatientRepository;
import edu.unimagdalena.clinica.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll().stream().map(patientMapper::toDto).toList();
    }

    @Override
    public PatientResponseDTO getPatientById(Long id) {
        return patientRepository.findById(id).map(patientMapper::toDto)
                .orElseThrow(()-> new PatientNotFoundException("Patient not found with id: " + id));
    }

    @Override
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        Patient patient = patientMapper.toEntity(patientRequestDTO);
        return patientMapper.toDto(patientRepository.save(patient));
    }

    @Override
    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO patientRequestDTO) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(()-> new PatientNotFoundException("Patient not found with id: " + id));
        existing.setFullName(patientRequestDTO.fullName());
        existing.setEmail(patientRequestDTO.email());
        existing.setPhone(patientRequestDTO.phone());
        return patientMapper.toDto(patientRepository.save(existing));

    }

    @Override
    public void deletePatient(Long id) {
        if(!patientRepository.existsById(id)){
            throw new PatientNotFoundException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);

    }
}
