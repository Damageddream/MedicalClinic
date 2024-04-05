package com.damageddream.medicalclinic.rest;

import com.damageddream.medicalclinic.dto.AppointmentDTO;
import com.damageddream.medicalclinic.dto.GetIdCommand;
import com.damageddream.medicalclinic.dto.PatientDTO;
import com.damageddream.medicalclinic.entity.Appointment;
import com.damageddream.medicalclinic.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointments")
public class AppointmentRestController {
    private final AppointmentService appointmentService;

    @Operation(summary = "Add new appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "There is already appointment at this time",
                    content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentDTO addAppointment(@Parameter(description = "Doctor id",
            required = true) @RequestParam Long doctorId, @RequestBody Appointment appointment) {
        return appointmentService.addAppointment(doctorId, appointment);
    }

    @Operation(description = "Get appointments by doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found doctor appointments",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AppointmentDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid parameter values",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = @Content)
    })
    @GetMapping("/doctor/{id}")
    public List<AppointmentDTO> getDoctorAppointments(@PathVariable Long id,
                                                      @Parameter(description = "Filter for only available appointments",
                                                              required = false) @RequestParam Boolean onlyFree) {
        return appointmentService.getDoctorAppointments(id, onlyFree);
    }

    @Operation(description = "Patient making an appointment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment was reserved for the patient",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetIdCommand.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Appointment not found",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "That appointment is no longer free",
                    content = @Content)
    })
    @PatchMapping
    public AppointmentDTO patchAppointment(@Parameter(description = "Patient id",
            required = true) @RequestParam Long patientId, @RequestBody GetIdCommand appointmentId) {
        return appointmentService.makeAnAppointment(patientId, appointmentId);
    }

    @Operation(summary = "Get patient's appointments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return a list of appointments by patient",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AppointmentDTO.class)))}),
            @ApiResponse(responseCode = "404", description = "Patient don't have any appointments",
                    content = @Content)})
    @GetMapping("/patient/{id}")
    public List<AppointmentDTO> getPatientAppointments(@PathVariable Long id) {
        return appointmentService.getPatientsAppointments(id);
    }

    @Operation(description = "Get all appointments, optionally filtered by availability")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found appointments",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AppointmentDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "Invalid parameter values",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "No appointments found",
                    content = @Content)
    })
    @GetMapping
    public List<AppointmentDTO> getAppointments(@Parameter(description = "Filter for only available appointments",
            required = false) @RequestParam Boolean onlyFree) {
        return appointmentService.getAppointments(onlyFree);
    }

    @GetMapping("/patients")
    public List<PatientDTO> getPatientsByAppointmentsDate(
            @RequestParam("appointmentDate") String appointmentDate){
        return appointmentService.getPatientByAppointmentDate(appointmentDate);
    }
}
