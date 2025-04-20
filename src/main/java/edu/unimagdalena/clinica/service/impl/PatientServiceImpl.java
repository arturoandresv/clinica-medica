package edu.unimagdalena.clinica.service.impl;

import edu.unimagdalena.clinica.dto.PatientDTO;
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
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream().map(patientMapper::toDto).toList();
    }

    @Override
    public PatientDTO getPatientById(Long id) {
        return patientRepository.findById(id).map(patientMapper::toDto)
                .orElseThrow(()-> new PatientNotFoundException("Patient not found with id: " + id));
    }

    @Override
    public PatientDTO createPatient(PatientDTO patientDTO) {
        Patient patient = patientMapper.toEntity(patientDTO);
        return patientMapper.toDto(patientRepository.save(patient));
    }

    @Override
    public PatientDTO updatePatient(Long id, PatientDTO patientDTO) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(()-> new PatientNotFoundException("Patient not found with id: " + id));
        existing.setFullName(patientDTO.fullName());
        existing.setEmail(patientDTO.email());
        existing.setPhone(patientDTO.phone());
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
