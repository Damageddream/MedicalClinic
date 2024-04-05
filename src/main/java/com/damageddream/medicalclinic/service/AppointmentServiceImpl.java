package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.AppointmentDTO;
import com.damageddream.medicalclinic.dto.GetIdCommand;
import com.damageddream.medicalclinic.dto.PatientDTO;
import com.damageddream.medicalclinic.dto.mapper.AppointmentMapper;
import com.damageddream.medicalclinic.dto.mapper.PatientMapper;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentMapper appointmentMapper;
    private final PatientMapper patientMapper;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final DataValidator dataValidator;

    @Override
    @Transactional
    public AppointmentDTO addAppointment(Long doctorId, Appointment appointment) {
        dataValidator.validateDateTime(appointment.getAppointmentStart(), appointment.getAppointmentEnd());
        List<Appointment> conflictingAppointments = appointmentRepository.findConflictingAppointments(doctorId, appointment.getAppointmentStart(), appointment.getAppointmentEnd());
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
    public List<AppointmentDTO> getDoctorAppointments(Long doctorId, Boolean onlyFree) {
        List<Appointment> appointments = onlyFree ?
                appointmentRepository.findFreeAppointmentsByDoctor(doctorId) :
                appointmentRepository.findByDoctorId(doctorId);
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
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("There is no such appointment"));
        if (appointment.getPatient() != null) {
            throw new AppointmentNotFoundException("That appointment is no longer free");
        }
        appointment.setPatient(patient);

        return appointmentMapper.toDTO(appointment);
    }

    @Override
    public List<AppointmentDTO> getPatientsAppointments(Long id) {
        patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        List<Appointment> appointments = appointmentRepository.findByPatientId(id);
        if (appointments.isEmpty()) {
            throw new AppointmentNotFoundException("Patient don't have any appointments");
        }

        return appointments.stream().map(appointmentMapper::toDTO).toList();
    }

    @Override
    public List<AppointmentDTO> getAppointments(Boolean onlyFree) {
        List<Appointment> appointments = onlyFree ?
                appointmentRepository.findByPatientIsNull() :
                appointmentRepository.findAll();

        if (appointments.isEmpty()) {
            throw new AppointmentNotFoundException("We have no appointments at this time");
        }
        return appointments.stream().map(appointmentMapper::toDTO).toList();
    }

    @Override
    public List<PatientDTO> getPatientByAppointmentDate(String appointmentDate) {
        LocalDateTime parsedDate = null;
        try{
            parsedDate = LocalDate.parse(appointmentDate, DateTimeFormatter.ISO_DATE).atStartOfDay();
        } catch (DateTimeParseException e){
            throw new InvalidDateTimeException("Passed date is not valid date type");
        }

        LocalDateTime endDate = parsedDate.plusDays(1);

        List<Appointment> appointments = appointmentRepository
                .findByAppointmentStartGreaterThanEqualAndAppointmentStartLessThan(parsedDate, endDate);

        if(appointments.isEmpty()){
            throw new AppointmentNotFoundException("There are no appointments at that date");
        }

        Set<Patient> distinctPatients = new HashSet<>();
        for (Appointment appointment : appointments) {
            distinctPatients.add(appointment.getPatient());
        }

        return distinctPatients.stream()
                .map(patientMapper::toDTO)
                .toList();
    }
}
