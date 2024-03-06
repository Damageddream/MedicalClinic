package com.damageddream.medicalclinic.rest;

import com.damageddream.medicalclinic.entity.ChangePasswordCommand;
import com.damageddream.medicalclinic.entity.Patient;
import com.damageddream.medicalclinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientRestController {

    private final PatientService patientService;

    @GetMapping
    public List<Patient> findAll(){
        return patientService.findAll();
    }

    @GetMapping("/{patientEmail}")
    public Patient getPatient(@PathVariable String patientEmail) {
        return patientService.findByEmail(patientEmail);
    }

    @PostMapping
    public Patient addPatient(@RequestBody Patient patient) {
        return patientService.save(patient);
    }

    @PutMapping("/{patientEmail}")
    public Patient updatePatient(@PathVariable String patientEmail, @RequestBody Patient patient) {
        return patientService.update(patientEmail, patient);
    }

    @PatchMapping("/{patientEmail}")
    public Patient updatePassword(@PathVariable String patientEmail, @RequestBody ChangePasswordCommand password) {
        return patientService.editPassword(password, patientEmail);
    }

    @DeleteMapping("/{patientEmail}")
    public Patient deletePatient(@PathVariable String patientEmail) {
        return patientService.delete(patientEmail);
    }
}
