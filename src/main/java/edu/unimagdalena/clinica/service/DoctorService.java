package edu.unimagdalena.clinica.service;

import edu.unimagdalena.clinica.dto.response.DoctorResponseDTO;

import java.util.List;

public interface DoctorService {
    List<DoctorResponseDTO> getAllDoctors();
    DoctorResponseDTO getDoctorById(Long id);
    DoctorResponseDTO createDoctor(DoctorResponseDTO doctorResponseDTO);
    DoctorResponseDTO updateDoctor(Long id, DoctorResponseDTO doctorResponseDTO);
    void deleteDoctor(Long id);
}
