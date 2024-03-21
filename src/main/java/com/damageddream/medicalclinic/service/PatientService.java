package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.*;
import com.damageddream.medicalclinic.entity.Appointment;

import java.util.List;

public interface PatientService {
    PatientDTO save(NewPatientDTO patient);
    PatientDTO findByEmail(String email);
    List<PatientDTO> findAll();
    PatientDTO update(String email, NewPatientDTO patient);
    PatientDTO delete(String email);

    PatientDTO editPassword(ChangePasswordCommand password, String email);
    AppointmentDTO makeAnAppointment(Long patientId, GetIdCommand appointmentId);
    List<AppointmentDTO> getPatientsAppointments(Long id);
}
