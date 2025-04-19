package edu.unimagdalena.clinica.service;

import edu.unimagdalena.clinica.dto.DoctorDTO;

import java.util.List;

public interface DoctorService {
    List<DoctorDTO> getAllDoctors();
    DoctorDTO getDoctorById(Long id);
    DoctorDTO createDoctor(DoctorDTO doctorDTO);
    DoctorDTO updateDoctor(Long id, DoctorDTO doctorDTO);
    void deleteDoctor(Long id);
}
