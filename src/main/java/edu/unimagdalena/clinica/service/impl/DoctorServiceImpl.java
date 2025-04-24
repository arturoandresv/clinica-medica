package edu.unimagdalena.clinica.service.impl;

import edu.unimagdalena.clinica.dto.request.DoctorRequestDTO;
import edu.unimagdalena.clinica.dto.response.DoctorResponseDTO;
import edu.unimagdalena.clinica.exception.notfound.DoctorNotFoundException;
import edu.unimagdalena.clinica.mapper.DoctorMapper;
import edu.unimagdalena.clinica.model.Doctor;
import edu.unimagdalena.clinica.repository.DoctorRepository;
import edu.unimagdalena.clinica.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Override
    public List<DoctorResponseDTO> getAllDoctors() {
        return doctorRepository.findAll().stream().map(doctorMapper::toDto).toList();
    }

    @Override
    public DoctorResponseDTO getDoctorById(Long id) {
        return doctorRepository.findById(id).map(doctorMapper::toDto)
                .orElseThrow(()-> new DoctorNotFoundException("Doctor not found with id: " + id));
    }

    @Override
    public DoctorResponseDTO createDoctor(DoctorRequestDTO doctorRequestDTO) {
        Doctor doctor = doctorMapper.toEntity(doctorRequestDTO);
        return doctorMapper.toDto(doctorRepository.save(doctor));
    }

    @Override
    public DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO doctorRequestDTO) {
        Doctor existing = doctorRepository.findById(id)
                .orElseThrow(()-> new DoctorNotFoundException("Doctor not found with id: " + id));
        existing.setFullName(doctorRequestDTO.fullName());
        existing.setEmail(doctorRequestDTO.email());
        existing.setSpecialty(doctorRequestDTO.specialty());
        existing.setAvailableFrom(doctorRequestDTO.availableFrom());
        existing.setAvailableTo(doctorRequestDTO.availableTo());
        return doctorMapper.toDto(doctorRepository.save(existing));
    }

    @Override
    public void deleteDoctor(Long id) {
        if(!doctorRepository.existsById(id)){
            throw new DoctorNotFoundException("Doctor not found with id: " + id);
        }

        doctorRepository.deleteById(id);
    }

    @Override
    public List<DoctorResponseDTO> findBySpecialty(String specialty) {
        List<Doctor> doctors = doctorRepository.findBySpecialty(specialty);
        if(doctors.isEmpty()){
            throw new DoctorNotFoundException("Doctor not found with specialty: " + specialty);
        }

        return doctors.stream().map(doctorMapper::toDto).toList();
    }
}
