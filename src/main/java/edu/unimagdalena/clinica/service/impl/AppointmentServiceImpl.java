package edu.unimagdalena.clinica.service.impl;

import edu.unimagdalena.clinica.dto.AppointmentDTO;
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
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream().map(appointmentMapper::toDTO).toList();
    }

    @Override
    public AppointmentDTO getAppointmentById(Long id) {
        return appointmentRepository.findById(id).map(appointmentMapper::toDTO)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with id: " + id));
    }

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {

        Patient patient = patientRepository.findById(appointmentDTO.patientId())
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: "
                        + appointmentDTO.patientId()));

        Doctor doctor = doctorRepository.findById(appointmentDTO.doctorId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with ID: "
                        + appointmentDTO.doctorId()));

        ConsultRoom consultRoom = consultRoomRepository.findById(appointmentDTO.consultRoomId())
                .orElseThrow(()-> new ConsultRoomNotFoundException("consult room not found with ID: "
                        + appointmentDTO.consultRoomId()));

        List<Appointment> doctorConflicts = appointmentRepository.
                findConflictDoctor(appointmentDTO.doctorId(), appointmentDTO.startTime(), appointmentDTO.endTime());

        List<Appointment> consultRoomConflicts = appointmentRepository.
                findConflictConsultRoom(appointmentDTO.consultRoomId(), appointmentDTO.startTime(), appointmentDTO.endTime());

        if (!doctorConflicts.isEmpty() && !consultRoomConflicts.isEmpty()) {
            throw new AppointmentConflictException("Doctor and consult room have an appointment at this time.");
        } else if (!doctorConflicts.isEmpty()) {
            throw new AppointmentConflictException("The doctor already has an appointment at this time.");
        } else if (!consultRoomConflicts.isEmpty()) {
            throw new AppointmentConflictException("The consult room already has an appointment at this time.");
        }

        if(doctor.getAvailableFrom().isAfter(appointmentDTO.startTime().toLocalTime()) || doctor.getAvailableTo().isBefore(appointmentDTO.endTime().toLocalTime())) {
            throw new DoctorScheduleConflictException("Doctor schedule conflict");
        }

        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setConsultRoom(consultRoom);

        return appointmentMapper.toDTO(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO) {
        Appointment existing = appointmentRepository.findById(id)
                .orElseThrow(()-> new AppointmentNotFoundException("Appointment not found with id: " + id));

        if((existing.getStatus() != AppointmentStatus.SCHEDULED)) {
            throw new AppointmentNotModifiableException("Appointment not modifiable");
        }

        Patient patient = patientRepository.findById(appointmentDTO.patientId())
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: "
                        + appointmentDTO.patientId()));

        Doctor doctor = doctorRepository.findById(appointmentDTO.doctorId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with ID: "
                        + appointmentDTO.doctorId()));

        ConsultRoom consultRoom = consultRoomRepository.findById(appointmentDTO.consultRoomId())
                .orElseThrow(()-> new ConsultRoomNotFoundException("consult room not found with ID: "
                        + appointmentDTO.consultRoomId()));

        List<Appointment> doctorConflicts = appointmentRepository.
                findConflictDoctor(appointmentDTO.doctorId(), appointmentDTO.startTime(), appointmentDTO.endTime())
                .stream()
                .filter(a -> !a.getId().equals(id))
                .toList();

        List<Appointment> consultRoomConflicts = appointmentRepository.
                findConflictConsultRoom
                        (appointmentDTO.consultRoomId(), appointmentDTO.startTime(), appointmentDTO.endTime())
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

        LocalTime start = appointmentDTO.startTime().toLocalTime();
        LocalTime end = appointmentDTO.endTime().toLocalTime();
        if (start.isBefore(doctor.getAvailableFrom()) || end.isAfter(doctor.getAvailableTo())) {
            throw new DoctorScheduleConflictException("Doctor schedule conflict");
        }

        existing.setPatient(patient);
        existing.setDoctor(doctor);
        existing.setConsultRoom(consultRoom);
        existing.setStartTime(appointmentDTO.startTime());
        existing.setEndTime(appointmentDTO.endTime());
        existing.setStatus(appointmentDTO.status());

        return appointmentMapper.toDTO(appointmentRepository.save(existing));

    }

    @Override
    public void deleteAppointment(Long id) {
        if(!appointmentRepository.existsById(id)) {
            throw new AppointmentNotFoundException("Appointment not found with id: " + id);
        }

        appointmentRepository.deleteById(id);
    }
}
