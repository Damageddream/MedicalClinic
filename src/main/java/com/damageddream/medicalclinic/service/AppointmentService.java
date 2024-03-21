package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.AppointmentDTO;
import com.damageddream.medicalclinic.dto.GetIdCommand;
import com.damageddream.medicalclinic.entity.Appointment;

import java.util.List;

public interface AppointmentService {
    AppointmentDTO makeAnAppointment(Long patientId, GetIdCommand appointmentId);
    List<AppointmentDTO> getPatientsAppointments(Long id);

    AppointmentDTO addAppointment(Long doctorId, Appointment appointment);
    List<AppointmentDTO> getFreeAppointmentsByDoctor(Long doctorId);
}
