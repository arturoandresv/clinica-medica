package edu.unimagdalena.clinica.service;

import edu.unimagdalena.clinica.dto.request.DoctorRequestDTO;
import edu.unimagdalena.clinica.dto.response.DoctorResponseDTO;

import java.util.List;

public interface DoctorService {
    List<DoctorResponseDTO> getAllDoctors();
    DoctorResponseDTO getDoctorById(Long id);
    DoctorResponseDTO createDoctor(DoctorRequestDTO doctorRequestDTO);
    DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO doctorRequestDTO);
    void deleteDoctor(Long id);
    List<DoctorResponseDTO> findBySpecialty(String specialty);
}
