package com.damageddream.medicalclinic.rest;

import com.damageddream.medicalclinic.entity.Patient;
import com.damageddream.medicalclinic.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PatientRestController {
    private PatientService patientService;

    @Autowired
    public PatientRestController(PatientService patientService) {
        this.patientService =patientService;
    }

        @GetMapping("/patients")
    public List<Patient> findAll(){
        return patientService.findAll();
    }

    @GetMapping("/patients/{patientEmail}")
    public Patient getPatient(@PathVariable String patientEmail) {
        Patient thePatient = patientService.findByEmail(patientEmail);
        if(thePatient == null){
            throw new RuntimeException("Patient not found - "+patientEmail);
        }
        return  thePatient;
    }

    @PostMapping("/patients")
    public Patient addPatient(@RequestBody Patient patient) {
        Patient thePatient = patientService.save(patient);
        return thePatient;
    }

    @PutMapping("/patients/{patientEmail}")
    public Patient updatePatient(@PathVariable String patientEmail, @RequestBody Patient patient) {
        Patient thePatient = patientService.update(patientEmail, patient);
        if(thePatient == null){
            throw new RuntimeException("Patient not found - "+patientEmail);
        }
        return thePatient;
    }

    @DeleteMapping("/patients/{patientEmail}")
    public Patient deletePatient(@PathVariable String patientEmail) {
        Patient thePatient = patientService.delete(patientEmail);
        if(thePatient == null){
            throw new RuntimeException("Patient not found - "+patientEmail);
        }
        return thePatient;
    }
}
