package com.damageddream.medicalclinic.rest;

import com.damageddream.medicalclinic.dto.ChangePasswordCommand;
import com.damageddream.medicalclinic.dto.NewPatientDTO;
import com.damageddream.medicalclinic.dto.PatientDTO;
import com.damageddream.medicalclinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientRestController {

    private final PatientService patientService;

    @GetMapping
    public List<PatientDTO> findAll() {
        return patientService.findAll();
    }

    @GetMapping("/{patientEmail}")
    public PatientDTO getPatient(@PathVariable String patientEmail) {
        return patientService.findByEmail(patientEmail);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDTO addPatient(@RequestBody NewPatientDTO patient) {
        return patientService.save(patient);
    }

    @PutMapping("/{patientEmail}")
    public PatientDTO updatePatient(@PathVariable String patientEmail, @RequestBody NewPatientDTO patient) {
        return patientService.update(patientEmail, patient);
    }

    @PatchMapping("/{patientEmail}")
    public PatientDTO updatePassword(@PathVariable String patientEmail, @RequestBody ChangePasswordCommand password) {
        return patientService.editPassword(password, patientEmail);
    }

    @DeleteMapping("/{patientEmail}")
    public PatientDTO deletePatient(@PathVariable String patientEmail) {
        return patientService.delete(patientEmail);
    }
}
