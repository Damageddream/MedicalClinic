package com.damageddream.medicalclinic.rest;

import com.damageddream.medicalclinic.dto.AppointmentDTO;
import com.damageddream.medicalclinic.dto.GetIdCommand;
import com.damageddream.medicalclinic.entity.Appointment;
import com.damageddream.medicalclinic.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointments")
public class AppointmentRestController {
    private final AppointmentService appointmentService;

    @PostMapping("/doctor/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentDTO addAppointment(@PathVariable Long id, @RequestBody Appointment appointment) {
        return appointmentService.addAppointment(id, appointment);
    }

    @GetMapping("/doctor/{id}/free")
    public List<AppointmentDTO> getFreeAppointments(@PathVariable Long id) {
        return appointmentService.getFreeAppointmentsByDoctor(id);
    }

    @PatchMapping("/patient/{id}")
    public AppointmentDTO patchAppointment(@PathVariable Long id, @RequestBody GetIdCommand appointmentId) {
        return appointmentService.makeAnAppointment(id, appointmentId);
    }

    @GetMapping("/patient/{id}")
    public List<AppointmentDTO> getAppointments(@PathVariable Long id) {
        return appointmentService.getPatientsAppointments(id);
    }
}
