package com.damageddream.medicalclinic.rest;

import com.damageddream.medicalclinic.dto.GetFacilityIdCommand;
import com.damageddream.medicalclinic.entity.Doctor;
import com.damageddream.medicalclinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctors")
public class DoctorRestController {

    private final DoctorService doctorService;

    @GetMapping("/{id}")
    public Doctor getDoctor(@PathVariable Long id) {
        return doctorService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Doctor addDoctor(@RequestBody Doctor doctor) {
        return doctorService.save(doctor);
    }

    @PostMapping("/{id}/facilities")
    public Doctor addFacilityToDoctor(@PathVariable Long id, @RequestBody GetFacilityIdCommand facilityId) {
        return doctorService.addFacilityToDoctor(id, facilityId);
    }
}
