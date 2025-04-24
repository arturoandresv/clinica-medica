package edu.unimagdalena.clinica.service;

import edu.unimagdalena.clinica.dto.request.AppointmentRequestCreateDTO;
import edu.unimagdalena.clinica.dto.request.AppointmentRequestUpdateDTO;
import edu.unimagdalena.clinica.dto.response.AppointmentResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    List<AppointmentResponseDTO> getAllAppointments();
    AppointmentResponseDTO getAppointmentById(Long id);
    AppointmentResponseDTO createAppointment(AppointmentRequestCreateDTO appointmentRequestCreateDTO);
    AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestUpdateDTO appointmentRequestUpdateDTO);
    void deleteAppointment(Long id);
    List<AppointmentResponseDTO> findDoctorAppointmentsByDate(Long doctorId, LocalDate date);
}
