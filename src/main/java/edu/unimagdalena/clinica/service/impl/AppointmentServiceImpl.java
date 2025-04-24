package edu.unimagdalena.clinica.service.impl;

import edu.unimagdalena.clinica.dto.request.AppointmentRequestCreateDTO;
import edu.unimagdalena.clinica.dto.request.AppointmentRequestUpdateDTO;
import edu.unimagdalena.clinica.dto.response.AppointmentResponseDTO;
import edu.unimagdalena.clinica.exception.*;
import edu.unimagdalena.clinica.exception.notfound.AppointmentNotFoundException;
import edu.unimagdalena.clinica.exception.notfound.ConsultRoomNotFoundException;
import edu.unimagdalena.clinica.exception.notfound.DoctorNotFoundException;
import edu.unimagdalena.clinica.exception.notfound.PatientNotFoundException;
import edu.unimagdalena.clinica.mapper.AppointmentMapper;
import edu.unimagdalena.clinica.model.*;
import edu.unimagdalena.clinica.repository.AppointmentRepository;
import edu.unimagdalena.clinica.repository.ConsultRoomRepository;
import edu.unimagdalena.clinica.repository.DoctorRepository;
import edu.unimagdalena.clinica.repository.PatientRepository;
import edu.unimagdalena.clinica.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ConsultRoomRepository consultRoomRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream().map(appointmentMapper::toDTO).toList();
    }

    @Override
    public AppointmentResponseDTO getAppointmentById(Long id) {
        return appointmentRepository.findById(id).map(appointmentMapper::toDTO)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));
    }

    @Override
    public AppointmentResponseDTO createAppointment(AppointmentRequestCreateDTO appointmentRequestCreateDTO) {

        Patient patient = patientRepository.findById(appointmentRequestCreateDTO.patientId())
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: "
                        + appointmentRequestCreateDTO.patientId()));

        Doctor doctor = doctorRepository.findById(appointmentRequestCreateDTO.doctorId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with ID: "
                        + appointmentRequestCreateDTO.doctorId()));

        ConsultRoom consultRoom = consultRoomRepository.findById(appointmentRequestCreateDTO.consultRoomId())
                .orElseThrow(()-> new ConsultRoomNotFoundException("consult room not found with ID: "
                        + appointmentRequestCreateDTO.consultRoomId()));

        List<Appointment> doctorConflicts = appointmentRepository.
                findConflictDoctor(appointmentRequestCreateDTO.doctorId(), appointmentRequestCreateDTO.startTime(), appointmentRequestCreateDTO.endTime());

        List<Appointment> consultRoomConflicts = appointmentRepository.
                findConflictConsultRoom(appointmentRequestCreateDTO.consultRoomId(), appointmentRequestCreateDTO.startTime(), appointmentRequestCreateDTO.endTime());

        if (!doctorConflicts.isEmpty() && !consultRoomConflicts.isEmpty()) {
            throw new AppointmentConflictException("Doctor and consult room have an appointment at this time.");
        } else if (!doctorConflicts.isEmpty()) {
            throw new AppointmentConflictException("The doctor already has an appointment at this time.");
        } else if (!consultRoomConflicts.isEmpty()) {
            throw new AppointmentConflictException("The consult room already has an appointment at this time.");
        }
        long duration = Duration.between(appointmentRequestCreateDTO.startTime(), appointmentRequestCreateDTO.endTime()).toMinutes();
        if(duration < 30 || duration > 180) {
            throw new AppointmentConflictException("The appointment duration is incorrect. (30-180 minutes)");
        }

        if(doctor.getAvailableFrom().isAfter(appointmentRequestCreateDTO.startTime().toLocalTime()) || doctor.getAvailableTo().isBefore(appointmentRequestCreateDTO.endTime().toLocalTime())) {
            throw new DoctorScheduleConflictException("Doctor schedule conflict");
        }

        Appointment appointment = appointmentMapper.toEntity(appointmentRequestCreateDTO);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setConsultRoom(consultRoom);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        return appointmentMapper.toDTO(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestUpdateDTO appointmentRequestUpdateDTO) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));

        if ((existing.getStatus() != AppointmentStatus.SCHEDULED)) {
            throw new AppointmentNotModifiableException("Appointment not modifiable");
        }

        Doctor doctor = existing.getDoctor();
        ConsultRoom consultRoom = existing.getConsultRoom();

        List<Appointment> doctorConflicts = appointmentRepository.
                findConflictDoctor(doctor.getId(), appointmentRequestUpdateDTO.startTime(), appointmentRequestUpdateDTO.endTime())
                .stream()
                .filter(a -> !a.getId().equals(id))
                .toList();

        List<Appointment> consultRoomConflicts = appointmentRepository.
                findConflictConsultRoom
                        (consultRoom.getId(), appointmentRequestUpdateDTO.startTime(), appointmentRequestUpdateDTO.endTime())
                .stream()
                .filter(a -> !a.getId().equals(id))
                .toList();

        if (!doctorConflicts.isEmpty() && !consultRoomConflicts.isEmpty()) {
            throw new AppointmentConflictException("Doctor and consult room have an appointment at this time.");
        } else if (!doctorConflicts.isEmpty()) {
            throw new AppointmentConflictException("The doctor already has an appointment at this time.");
        } else if (!consultRoomConflicts.isEmpty()) {
            throw new AppointmentConflictException("The consult room already has an appointment at this time.");
        }

        long duration = Duration.between(appointmentRequestUpdateDTO.startTime(), appointmentRequestUpdateDTO.endTime()).toMinutes();
        if(duration < 30 || duration > 180) {
            throw new AppointmentConflictException("The appointment duration is incorrect. (30-180 minutes)");
        }

        LocalTime start = appointmentRequestUpdateDTO.startTime().toLocalTime();
        LocalTime end = appointmentRequestUpdateDTO.endTime().toLocalTime();

        if (start.isBefore(doctor.getAvailableFrom()) || end.isAfter(doctor.getAvailableTo())) {
            throw new DoctorScheduleConflictException("Doctor schedule conflict");
        }

        existing.setStartTime(appointmentRequestUpdateDTO.startTime());
        existing.setEndTime(appointmentRequestUpdateDTO.endTime());
        existing.setStatus(appointmentRequestUpdateDTO.status());

        return appointmentMapper.toDTO(appointmentRepository.save(existing));

    }

    @Override
    public AppointmentResponseDTO cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        if (appointment.getStatus() == AppointmentStatus.COMPLETE) {
            throw new IllegalStateException("Appointment is already completed");
        }

        appointment.setStatus(AppointmentStatus.CANCELED);
        return appointmentMapper.toDTO(appointmentRepository.save(appointment));
    }

    @Override
    public void deleteAppointment(Long id) {
        if(!appointmentRepository.existsById(id)) {
            throw new AppointmentNotFoundException("Appointment not found with id: " + id);
        }

        appointmentRepository.deleteById(id);
    }

    @Override
    public List<AppointmentResponseDTO> findDoctorAppointmentsByDate(Long doctorId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id: " + doctorId));

        List<Appointment> appointments = appointmentRepository.findDoctorAppointmentsByDate(doctorId, startOfDay, endOfDay);

        if(appointments.isEmpty()) {
            throw new AppointmentNotFoundException("No appointments found for doctor " + doctorId + " on " + date);
        }

        return appointments.stream().map(appointmentMapper::toDTO).toList();
    }
}
