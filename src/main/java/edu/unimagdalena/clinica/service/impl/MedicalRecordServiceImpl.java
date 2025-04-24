package edu.unimagdalena.clinica.service.impl;

import edu.unimagdalena.clinica.dto.request.MedicalRecordRequestCreateDTO;
import edu.unimagdalena.clinica.dto.request.MedicalRecordRequestUpdateDTO;
import edu.unimagdalena.clinica.dto.response.MedicalRecordResponseDTO;
import edu.unimagdalena.clinica.exception.ResourceNotFoundException;
import edu.unimagdalena.clinica.mapper.MedicalRecordMapper;
import edu.unimagdalena.clinica.model.Appointment;
import edu.unimagdalena.clinica.model.AppointmentStatus;
import edu.unimagdalena.clinica.model.MedicalRecord;
import edu.unimagdalena.clinica.model.Patient;
import edu.unimagdalena.clinica.repository.AppointmentRepository;
import edu.unimagdalena.clinica.repository.MedicalRecordRepository;
import edu.unimagdalena.clinica.repository.PatientRepository;
import edu.unimagdalena.clinica.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordMapper medicalRecordMapper;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    @Override
    public List<MedicalRecordResponseDTO> getAllMedicalRecords() {
        return medicalRecordRepository.findAll().stream()
                .map(medicalRecordMapper::toDTO)
                .toList();
    }

    @Override
    public MedicalRecordResponseDTO getMedicalRecordById(Long id) {
        return medicalRecordRepository.findById(id)
                .map(medicalRecordMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Medical Record not found with id: " + id));
    }

    @Override
    public MedicalRecordResponseDTO createMedicalRecord(MedicalRecordRequestCreateDTO dto) {
        Patient patient = patientRepository.findById(dto.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + dto.patientId()));
        Appointment appointment = appointmentRepository.findById(dto.appointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + dto.appointmentId()));

        if(appointment.getStatus() != AppointmentStatus.COMPLETE){
            throw new IllegalStateException("Medical record can only be created with complete appointment");
        }
        MedicalRecord medicalRecord = medicalRecordMapper.toEntity(dto);
        medicalRecord.setPatient(patient);
        medicalRecord.setAppointment(appointment);

        return medicalRecordMapper.toDTO(medicalRecordRepository.save(medicalRecord));
    }

    @Override
    public MedicalRecordResponseDTO updateMedicalRecord(Long id, MedicalRecordRequestUpdateDTO dto) {
        return null;
    }

    @Override
    public void deleteMedicalRecord(Long id) {
        if(!medicalRecordRepository.existsById(id)){
            throw new ResourceNotFoundException("Medical Record not found with id: " + id);
        }
        medicalRecordRepository.deleteById(id);
    }

    @Override
    public List<MedicalRecordResponseDTO> getMedicalRecordsByPatientId(Long patientId) {
        List<MedicalRecord> records = medicalRecordRepository.findByPatientId(patientId);
        return records.stream()
                .map(medicalRecordMapper::toDTO)
                .toList();
    }
}
