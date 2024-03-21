package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.*;
import com.damageddream.medicalclinic.dto.mapper.AppointmentMapper;
import com.damageddream.medicalclinic.dto.mapper.PatientMapper;
import com.damageddream.medicalclinic.entity.Appointment;
import com.damageddream.medicalclinic.entity.Patient;
import com.damageddream.medicalclinic.exception.AppointmentNotFoundException;
import com.damageddream.medicalclinic.exception.EmailAlreadyExistsException;
import com.damageddream.medicalclinic.exception.PatientNotFoundException;
import com.damageddream.medicalclinic.repository.AppointmentRepository;
import com.damageddream.medicalclinic.repository.PatientRepository;
import com.damageddream.medicalclinic.validation.DataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final DataValidator dataValidator;
    private final PatientMapper patientMapper;
    private final PatientRepository patientRepository;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public PatientDTO save(NewPatientDTO patient) {
        var existingPatient = patientRepository.findByEmail(patient.getEmail());
        if (existingPatient.isPresent()) {
            throw new EmailAlreadyExistsException("Patient with that email already exists");
        }
        Patient newPatient = patientMapper.fromDTO(patient);
        patientRepository.save(newPatient);
        return patientMapper.toDTO(newPatient);
    }

    @Override
    public PatientDTO findByEmail(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        return patientMapper.toDTO(patient);
    }

    @Override
    public List<PatientDTO> findAll() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(patientMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public PatientDTO update(String email, NewPatientDTO patient) {
        var toEdit = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        dataValidator.validateEditPatientData(email, patient, toEdit);
        patientMapper.updatePatientFromDTO(patient, toEdit);
        patientRepository.save(toEdit);
        return patientMapper.toDTO(toEdit);
    }


    @Override
    @Transactional
    public PatientDTO delete(String email) {
        var existingPatient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        patientRepository.delete(existingPatient);
        return patientMapper.toDTO(existingPatient);
    }

    @Override
    @Transactional
    public PatientDTO editPassword(ChangePasswordCommand password, String email) {
        var toEdit = patientRepository.findByEmail(email).orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        toEdit.setPassword(password.getPassword());
        patientRepository.save(toEdit);
        return patientMapper.toDTO(toEdit);
    }

    @Override
    @Transactional
    public AppointmentDTO makeAnAppointment(Long patientId, GetIdCommand appointmentIdCommand) {
        Long appointmentId = appointmentIdCommand.getEntityId();
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(()->new PatientNotFoundException("Patient not found"));
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(
                ()-> new AppointmentNotFoundException("There is no such appointment")
        );
        if(appointment.getPatient() != null){
            throw  new AppointmentNotFoundException("That appointment is no longer free");
        }
        appointment.setPatient(patient);
        return appointmentMapper.toDTO(appointment);
    }

    @Override
    public List<AppointmentDTO> getPatientsAppointments(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(()->new PatientNotFoundException("Patient not found"));

        List<Appointment> appointments = appointmentRepository.findAppointmentsByPatient(id);
        if(appointments.isEmpty()){
            throw new AppointmentNotFoundException("Patient don't have any appointments");
        }

        return appointments.stream().map(appointmentMapper::toDTO).toList();
    }
}
