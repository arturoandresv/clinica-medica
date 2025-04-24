package edu.unimagdalena.clinica.repository;

import edu.unimagdalena.clinica.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.consultRoom.id = :roomId " +
            "AND a.status = 'SCHEDULED' " +
            "AND ((a.startTime < :endTime AND a.endTime > :startTime))")
    List<Appointment> findConflictConsultRoom(@Param("roomId") Long roomId,
                                              @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
            "AND a.status = 'SCHEDULED' " +
            "AND ((a.startTime < :endTime AND a.endTime > :startTime))")
    List<Appointment> findConflictDoctor(@Param("doctorId") Long doctorId,
                                         @Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
            "AND ((a.startTime >= :startOfDay AND a.startTime < :endOfDay))")
    List<Appointment> findDoctorAppointmentsByDate(@Param("doctorId") Long doctorId,
                                                   @Param("startOfDay") LocalDateTime startOfDay,
                                                   @Param("endOfDay") LocalDateTime endOfDay);
}
