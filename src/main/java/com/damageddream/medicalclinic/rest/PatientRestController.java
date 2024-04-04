package com.damageddream.medicalclinic.rest;

import com.damageddream.medicalclinic.dto.ChangePasswordCommand;
import com.damageddream.medicalclinic.dto.NewPatientDTO;
import com.damageddream.medicalclinic.dto.PatientDTO;
import com.damageddream.medicalclinic.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientRestController {

    private final PatientService patientService;

    @Operation(summary = "Get all patients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a list of patients",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PatientDTO.class)))}),
            @ApiResponse(responseCode = "404", description = "Patients not found",
                    content = @Content)})
    @GetMapping
    public List<PatientDTO> findAll() {
        return patientService.findAll();
    }

    @Operation(summary = "Get patient by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the patient",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = @Content)
    })
    @GetMapping("/id/{id}")
    public PatientDTO getPatientById(@PathVariable Long id) {
        return patientService.findPatientById(id);
    }

    @Operation(summary = "Get patient by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the patient",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = @Content)
    })
    @GetMapping("/{patientEmail}")
    public PatientDTO getPatient(@PathVariable String patientEmail) {
        return patientService.findByEmail(patientEmail);
    }

    @Operation(summary = "Add a new patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Patient with that email already exists",
                    content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDTO addPatient(@RequestBody NewPatientDTO patient) {
        return patientService.save(patient);
    }

    @Operation(summary = "Edit patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient edited",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "409", description = "New email is not available",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Changes to Id Card No are forbidden",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "All fields of new Patient must be completed",
                    content = @Content)
    })
    @PutMapping("/{patientEmail}")
    public PatientDTO updatePatient(@PathVariable String patientEmail, @RequestBody NewPatientDTO patient) {
        return patientService.update(patientEmail, patient);
    }

    @Operation(summary = "Edit patient password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password edited",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = @Content)
    })
    @PatchMapping("/{patientEmail}")
    public PatientDTO updatePassword(@PathVariable String patientEmail, @RequestBody ChangePasswordCommand password) {
        return patientService.editPassword(password, patientEmail);
    }

    @Operation(summary = "Delete patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = @Content)
    })
    @DeleteMapping("/{patientEmail}")
    public PatientDTO deletePatient(@PathVariable String patientEmail) {
        return patientService.delete(patientEmail);
    }
}
