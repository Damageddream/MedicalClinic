package com.damageddream.medicalclinic.rest;

import com.damageddream.medicalclinic.dto.DoctorDTO;
import com.damageddream.medicalclinic.dto.FacilityDTO;
import com.damageddream.medicalclinic.dto.GetIdCommand;
import com.damageddream.medicalclinic.dto.NewFacilityDTO;
import com.damageddream.medicalclinic.service.FacilityServiceImpl;
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
@RequestMapping("/facilities")
public class FacilityRestController {

    private final FacilityServiceImpl facilityServiceImpl;

    @Operation(summary = "Get facility by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the facility",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FacilityDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Facility not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public FacilityDTO getFacility(@PathVariable Long id) {
        return facilityServiceImpl.findById(id);
    }

    @Operation(summary = "Get doctors by facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a list of doctors by facility",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DoctorDTO.class)))}),
            @ApiResponse(responseCode = "404", description = "Facility not found",
                    content = @Content)})
    @GetMapping("/{id}/doctors")
    public List<DoctorDTO> getFacilityDoctors(@PathVariable Long id) {
        return facilityServiceImpl.findDoctorsByFacility(id);
    }

    @Operation(summary = "Add a new facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Facility created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FacilityDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Facility with that name already exists",
                    content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FacilityDTO addFacility(@RequestBody NewFacilityDTO facility) {
        return facilityServiceImpl.save(facility);
    }

    @Operation(summary = "Add doctor to facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor added to facility",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FacilityDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Facility not found",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Doctor already is in this facility",
                    content = @Content)
    })
    @PutMapping("/{id}/doctors")
    public FacilityDTO addDoctor(@PathVariable Long id, @RequestBody GetIdCommand entityId) {
        return facilityServiceImpl.addDoctorToFacility(id, entityId);
    }

    @Operation(summary = "Edit facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Facility edited",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FacilityDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Facility not found",
                    content = @Content),
    })
    @PutMapping("/{id}")
    public FacilityDTO updateFacility(@PathVariable Long id, @RequestBody NewFacilityDTO facility) {
        return facilityServiceImpl.update(id, facility);
    }

    @Operation(summary = "Delete facility")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Facility deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = FacilityDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Facility not found",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public FacilityDTO deleteFacility(@PathVariable Long id) {
        return facilityServiceImpl.deleteFacility(id);
    }

}
