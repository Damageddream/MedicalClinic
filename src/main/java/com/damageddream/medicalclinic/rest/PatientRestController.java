package com.damageddream.medicalclinic.rest;

import com.damageddream.medicalclinic.dto.PatientGetDTO;
import com.damageddream.medicalclinic.entity.ChangePasswordCommand;
import com.damageddream.medicalclinic.dto.PatientCreateUpdateDTO;
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
    public List<PatientGetDTO> findAll(){
        return patientService.findAll();
    }

    @GetMapping("/{patientEmail}")
    public PatientGetDTO getPatient(@PathVariable String patientEmail) {
        return patientService.findByEmail(patientEmail);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientGetDTO addPatient(@RequestBody PatientCreateUpdateDTO patient) {
        return patientService.save(patient);
    }

    @PutMapping("/{patientEmail}")
    public PatientGetDTO updatePatient(@PathVariable String patientEmail, @RequestBody PatientCreateUpdateDTO patient) {
        return patientService.update(patientEmail, patient);
    }

    @PatchMapping("/{patientEmail}")
    public PatientGetDTO updatePassword(@PathVariable String patientEmail, @RequestBody ChangePasswordCommand password) {
        return patientService.editPassword(password, patientEmail);
    }

    @DeleteMapping("/{patientEmail}")
    public PatientGetDTO deletePatient(@PathVariable String patientEmail) {
        return patientService.delete(patientEmail);
    }
}
