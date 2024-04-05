package com.damageddream.medicalclinic.repository;

import com.damageddream.medicalclinic.entity.Appointment;
import com.damageddream.medicalclinic.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByPatientIsNull();

    List<Appointment> findByAppointmentStartGreaterThanEqualAndAppointmentStartLessThan
            (LocalDateTime startDate, LocalDateTime endDate);


    @Query("SELECT a FROM Appointment a WHERE :doctorId = a.doctor.id AND" +
            "(a.appointmentStart <=:newAppointmentEnd AND a.appointmentEnd >=:newAppointmentStart)")
    List<Appointment> findConflictingAppointments(
            @Param("doctorId") Long doctorId,
            @Param("newAppointmentStart") LocalDateTime newAppointmentStart,
            @Param("newAppointmentEnd") LocalDateTime newAppointmentEnd);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.patient IS NULL")
    List<Appointment> findFreeAppointmentsByDoctor(@Param("doctorId") Long doctorId);
}
