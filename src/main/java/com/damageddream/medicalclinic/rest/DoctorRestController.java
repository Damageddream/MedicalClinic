package com.damageddream.medicalclinic.rest;

import com.damageddream.medicalclinic.dto.DoctorDTO;
import com.damageddream.medicalclinic.dto.FacilityDTO;
import com.damageddream.medicalclinic.dto.GetIdCommand;
import com.damageddream.medicalclinic.dto.NewDoctorDTO;
import com.damageddream.medicalclinic.service.DoctorServiceImpl;
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
@RequestMapping("/doctors")
public class DoctorRestController {

    private final DoctorServiceImpl doctorServiceImpl;

    @Operation(summary = "Get doctor by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the doctor",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public DoctorDTO getDoctor(@PathVariable Long id) {
        return doctorServiceImpl.findById(id);
    }

    @Operation(summary = "Get facilities by doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a list of facilities by doctor",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FacilityDTO.class)))}),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = @Content)})
    @GetMapping("/{id}/facilities")
    public List<FacilityDTO> getDoctorsFacilities(@PathVariable Long id) {
        return doctorServiceImpl.findFacilitiesByDoctor(id);
    }

    @Operation(summary = "Add a new doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Doctor with that email already exists",
                    content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorDTO addDoctor(@RequestBody NewDoctorDTO doctor) {
        return doctorServiceImpl.save(doctor);
    }

    @Operation(summary = "Add facility to doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Facility added to doctor",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Facility not found",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Facility already is this doctor employer",
                    content = @Content)
    })
    @PutMapping("/{id}/facilities")
    public DoctorDTO addFacility(@PathVariable Long id, @RequestBody GetIdCommand entityId) {
        return doctorServiceImpl.addFacilityToDoctor(id, entityId);
    }

    @Operation(summary = "Delete doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public DoctorDTO deleteDoctor(@PathVariable Long id) {
        return doctorServiceImpl.deleteDoctor(id);
    }

    @Operation(summary = "Edit doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor edited",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = @Content),
    })
    @PutMapping("/{id}")
    public DoctorDTO updateDoctor(@PathVariable Long id, @RequestBody NewDoctorDTO doctor) {
        return doctorServiceImpl.update(id, doctor);
    }
}
