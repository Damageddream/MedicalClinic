package com.damageddream.medicalclinic.rest;

import com.damageddream.medicalclinic.dto.*;
import com.damageddream.medicalclinic.entity.Appointment;
import com.damageddream.medicalclinic.service.DoctorServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctors")
public class DoctorRestController {

    private final DoctorServiceImpl doctorServiceImpl;

    @GetMapping("/{id}")
    public DoctorDTO getDoctor(@PathVariable Long id) {
        return doctorServiceImpl.findById(id);
    }

    @GetMapping("/{id}/facilities")
    public List<FacilityDTO> getDoctorsFacilities(@PathVariable Long id) {
        return doctorServiceImpl.findFacilitiesByDoctor(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDTO addDoctor(@RequestBody NewDoctorDTO doctor) {
        return doctorServiceImpl.save(doctor);
    }

    @PutMapping("/{id}/facilities")
    public DoctorDTO addFacility(@PathVariable Long id, @RequestBody GetIdCommand entityId) {
        return doctorServiceImpl.addFacilityToDoctor(id, entityId);
    }

    @PutMapping("/{id}/appointments")
    public DoctorDTO addAppointment(@PathVariable Long id, @RequestBody Appointment appointment) {
        return doctorServiceImpl.addAppointment(id, appointment);
    }
}
