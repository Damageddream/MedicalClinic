package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.AppointmentDTO;
import com.damageddream.medicalclinic.dto.GetIdCommand;
import com.damageddream.medicalclinic.dto.mapper.AppointmentMapper;
import com.damageddream.medicalclinic.entity.Appointment;
import com.damageddream.medicalclinic.entity.Doctor;
import com.damageddream.medicalclinic.entity.Patient;
import com.damageddream.medicalclinic.exception.AppointmentNotFoundException;
import com.damageddream.medicalclinic.exception.DoctorNotFoundException;
import com.damageddream.medicalclinic.exception.InvalidDateTimeException;
import com.damageddream.medicalclinic.exception.PatientNotFoundException;
import com.damageddream.medicalclinic.repository.AppointmentRepository;
import com.damageddream.medicalclinic.repository.DoctorRepository;
import com.damageddream.medicalclinic.repository.PatientRepository;
import com.damageddream.medicalclinic.validation.DataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentMapper appointmentMapper;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    private final DataValidator dataValidator;

    @Override
    @Transactional
    public AppointmentDTO addAppointment(Long doctorId, Appointment appointment) {
        dataValidator.validateDateTime(appointment.getAppointmentStart(), appointment.getAppointmentEnd());
        List<Appointment> conflictingAppointments = appointmentRepository
                .findConflictingAppointments(doctorId, appointment.getAppointmentStart()
                        , appointment.getAppointmentEnd());
        if (!conflictingAppointments.isEmpty()) {
            throw new InvalidDateTimeException("There is already appointment at this time");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found"));

        appointment.setDoctor(doctor);
        appointmentRepository.save(appointment);

        return appointmentMapper.toDTO(appointment);
    }

    @Override
    public List<AppointmentDTO> getFreeAppointmentsByDoctor(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findFreeAppointmentsByDoctor(doctorId);
        if (appointments.isEmpty()) {
            throw new AppointmentNotFoundException("There are no free appointments for this doctor");
        }
        return appointments.stream().map(appointmentMapper::toDTO).toList();
    }

    @Override
    @Transactional
    public AppointmentDTO makeAnAppointment(Long patientId, GetIdCommand appointmentIdCommand) {
        Long appointmentId = appointmentIdCommand.getEntityId();
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(
                () -> new AppointmentNotFoundException("There is no such appointment")
        );
        if (appointment.getPatient() != null) {
            throw new AppointmentNotFoundException("That appointment is no longer free");
        }
        appointment.setPatient(patient);
        return appointmentMapper.toDTO(appointment);
    }

    @Override
    public List<AppointmentDTO> getPatientsAppointments(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));

        List<Appointment> appointments = appointmentRepository.findAppointmentsByPatient(id);
        if (appointments.isEmpty()) {
            throw new AppointmentNotFoundException("Patient don't have any appointments");
        }

        return appointments.stream().map(appointmentMapper::toDTO).toList();
    }
}
