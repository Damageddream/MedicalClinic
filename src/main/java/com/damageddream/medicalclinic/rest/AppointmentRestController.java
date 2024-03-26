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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentDTO addAppointment(@RequestParam Long doctorId, @RequestBody Appointment appointment) {
        return appointmentService.addAppointment(doctorId, appointment);
    }

    @GetMapping("/doctor/{id}")
    public List<AppointmentDTO> getDoctorAppointments(@PathVariable Long id, @RequestParam Boolean onlyFree) {
        return appointmentService.getDoctorAppointments(id, onlyFree);
    }

    @PatchMapping
    public AppointmentDTO patchAppointment(@RequestParam Long patientId, @RequestBody GetIdCommand appointmentId) {
        return appointmentService.makeAnAppointment(patientId, appointmentId);
    }

    @GetMapping("/patient/{id}")
    public List<AppointmentDTO> getPatientAppointments(@PathVariable Long id) {
        return appointmentService.getPatientsAppointments(id);
    }

    @GetMapping
    public List<AppointmentDTO> getAppointments(@RequestParam Boolean onlyFree){
        return appointmentService.getAppointments(onlyFree);
    }
}
