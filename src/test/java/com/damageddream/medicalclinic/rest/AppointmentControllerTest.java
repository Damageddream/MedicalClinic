package com.damageddream.medicalclinic.rest;

import com.damageddream.medicalclinic.dto.AppointmentDTO;
import com.damageddream.medicalclinic.dto.GetIdCommand;
import com.damageddream.medicalclinic.entity.Appointment;
import com.damageddream.medicalclinic.exception.AppointmentNotFoundException;
import com.damageddream.medicalclinic.exception.DoctorNotFoundException;
import com.damageddream.medicalclinic.exception.InvalidDateTimeException;
import com.damageddream.medicalclinic.exception.PatientNotFoundException;
import com.damageddream.medicalclinic.service.AppointmentServiceImpl;
import com.damageddream.medicalclinic.util.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppointmentServiceImpl appointmentService;

    @Test
    void postAppointment_appointmentValid_returnAppointmentDTO() throws Exception {
        //given
        Appointment appointment = TestDataFactory.createAppointment(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10)
        );
        AppointmentDTO appointmentDTO = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10)
        );

        when(appointmentService.addAppointment(any(), any())).thenReturn(appointmentDTO);

        //when //then
        mockMvc.perform(post("/appointments")
                        .param("doctorId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointment)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.appointmentStart")
                        .value("2024-03-22T15:30:10"))
                .andExpect(jsonPath("$.appointmentEnd").value("2024-03-22T16:30:10"))
                .andExpect(jsonPath("$.doctor.email").value("doc@mail.com"))
                .andExpect(jsonPath("$.doctor.firstName").value("Doc"))
                .andExpect(jsonPath("$.patient.email").value("mar@mail.com"));
    }

    @Test
    void getFreeAppointmentsByDoctor_appointmentsExists_returnAppointmentsDTOList() throws Exception {
        //given
        AppointmentDTO appointmentDTO1 = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10));
        AppointmentDTO appointmentDTO2 = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 13, 30, 10),
                LocalDateTime.of(2024, 3, 22, 14, 30, 10));
        List<AppointmentDTO> appointments = List.of(appointmentDTO1, appointmentDTO2);

        when(appointmentService.getDoctorAppointments(any(), any())).thenReturn(appointments);

        //when //then
        mockMvc.perform(get("/appointments/doctor/{id}", 1L)
                        .param("onlyFree", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].appointmentStart").value("2024-03-22T15:30:10"))
                .andExpect(jsonPath("$[0].appointmentEnd").value("2024-03-22T16:30:10"))
                .andExpect(jsonPath("$[1].appointmentStart").value("2024-03-22T13:30:10"))
                .andExpect(jsonPath("$[1].appointmentEnd").value("2024-03-22T14:30:10"));
    }

    @Test
    void getAllAppointmentsByDoctor_appointmentsExists_returnAppointmentsDTOList() throws Exception {
        //given
        AppointmentDTO appointmentDTO1 = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10));
        AppointmentDTO appointmentDTO2 = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 13, 30, 10),
                LocalDateTime.of(2024, 3, 22, 14, 30, 10));
        List<AppointmentDTO> appointments = List.of(appointmentDTO1, appointmentDTO2);

        when(appointmentService.getDoctorAppointments(any(), any())).thenReturn(appointments);

        //when //then
        mockMvc.perform(get("/appointments/doctor/{id}", 1L)
                        .param("onlyFree", "false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].appointmentStart").value("2024-03-22T15:30:10"))
                .andExpect(jsonPath("$[0].appointmentEnd").value("2024-03-22T16:30:10"))
                .andExpect(jsonPath("$[1].appointmentStart").value("2024-03-22T13:30:10"))
                .andExpect(jsonPath("$[1].appointmentEnd").value("2024-03-22T14:30:10"));
    }


    @Test
    void patchAppointment_appointmentExists_returnAppointmentDTO() throws Exception {
        //given
        GetIdCommand appointmentId = new GetIdCommand(2L);
        AppointmentDTO appointmentDTO = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10));

        when(appointmentService.makeAnAppointment(any(), any())).thenReturn(appointmentDTO);

        //when //then
        mockMvc.perform(patch("/appointments")
                        .param("patientId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentId)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.appointmentStart")
                        .value("2024-03-22T15:30:10"))
                .andExpect(jsonPath("$.appointmentEnd").value("2024-03-22T16:30:10"))
                .andExpect(jsonPath("$.doctor.email").value("doc@mail.com"))
                .andExpect(jsonPath("$.doctor.firstName").value("Doc"))
                .andExpect(jsonPath("$.patient.email").value("mar@mail.com"));
    }

    @Test
    void getPatientsAppointments_appointmentsExists_returnAppointmentsDTOList() throws Exception {
        //given
        AppointmentDTO appointmentDTO1 = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10));
        AppointmentDTO appointmentDTO2 = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 13, 30, 10),
                LocalDateTime.of(2024, 3, 22, 14, 30, 10));
        List<AppointmentDTO> appointments = List.of(appointmentDTO1, appointmentDTO2);

        when(appointmentService.getPatientsAppointments(any())).thenReturn(appointments);

        //when //then
        mockMvc.perform(get("/appointments/patient/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].appointmentStart").value("2024-03-22T15:30:10"))
                .andExpect(jsonPath("$[0].appointmentEnd").value("2024-03-22T16:30:10"))
                .andExpect(jsonPath("$[1].appointmentStart").value("2024-03-22T13:30:10"))
                .andExpect(jsonPath("$[1].appointmentEnd").value("2024-03-22T14:30:10"));
    }

    @Test
    void postAppointment_doctorNotFound_returnNotFoundStatus() throws Exception {
        //given
        Appointment appointment = TestDataFactory.createAppointment(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10)
        );

        when(appointmentService.addAppointment(any(), any())).thenThrow(DoctorNotFoundException.class);

        //when //then
        mockMvc.perform(post("/appointments")
                        .param("doctorId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointment)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void postAppointment_appointmentInvalid_returnConflictStatus() throws Exception {
        //given
        Appointment appointment = TestDataFactory.createAppointment(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10)
        );

        when(appointmentService.addAppointment(any(), any())).thenThrow(InvalidDateTimeException.class);

        //when //then
        mockMvc.perform(post("/appointments")
                        .param("doctorId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointment)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void getAllAppointmentsByDoctor_appointmentsNotExists_returnNotFoundStatus() throws Exception {
        //given
        when(appointmentService.getDoctorAppointments(any(), any())).thenThrow(AppointmentNotFoundException.class);

        //when //then
        mockMvc.perform(get("/appointments/doctor/{id}", 1L)
                        .param("onlyFree", "false"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getFreeAppointmentsByDoctor_appointmentsNotExists_returnNotFoundStatus() throws Exception {
        //given
        when(appointmentService.getDoctorAppointments(any(), any())).thenThrow(AppointmentNotFoundException.class);

        //when //then
        mockMvc.perform(get("/appointments/doctor/{id}", 1L)
                        .param("onlyFree", "true"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void patchAppointment_patientNotExists_returnNotFoundStatus() throws Exception {
        //given
        GetIdCommand appointmentId = new GetIdCommand(2L);
        AppointmentDTO appointmentDTO = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10));

        when(appointmentService.makeAnAppointment(any(), any())).thenThrow(PatientNotFoundException.class);

        //when //then
        mockMvc.perform(patch("/appointments")
                        .param("patientId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentId)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void patchAppointment_appointmentNotExists_returnNotFoundStatus() throws Exception {
        //given
        GetIdCommand appointmentId = new GetIdCommand(2L);
        AppointmentDTO appointmentDTO = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10));

        when(appointmentService.makeAnAppointment(any(), any())).thenThrow(AppointmentNotFoundException.class);

        //when //then
        mockMvc.perform(patch("/appointments")
                        .param("patientId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointmentId)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getPatientsAppointments_appointmentsNotExists_returnNotFoundStatus() throws Exception {
        //given
        when(appointmentService.getPatientsAppointments(any())).thenThrow(AppointmentNotFoundException.class);

        //when //then
        mockMvc.perform(get("/appointments/patient/{id}", 1L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getPatientsAppointments_patientNotExists_returnNotFoundStatus() throws Exception {
        //given
        when(appointmentService.getPatientsAppointments(any())).thenThrow(PatientNotFoundException.class);

        //when //then
        mockMvc.perform(get("/appointments/patient/{id}", 1L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllAppointments_AppointmentsExists_returnAppointmentsDTOList() throws Exception {
        //given
        AppointmentDTO appointmentDTO1 = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10));
        AppointmentDTO appointmentDTO2 = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 13, 30, 10),
                LocalDateTime.of(2024, 3, 22, 14, 30, 10));
        List<AppointmentDTO> appointments = List.of(appointmentDTO1, appointmentDTO2);

        when(appointmentService.getAppointments(any())).thenReturn(appointments);

        //when //then
        mockMvc.perform(get("/appointments")
                        .param("onlyFree", "false"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].appointmentStart").value("2024-03-22T15:30:10"))
                .andExpect(jsonPath("$[0].appointmentEnd").value("2024-03-22T16:30:10"))
                .andExpect(jsonPath("$[1].appointmentStart").value("2024-03-22T13:30:10"))
                .andExpect(jsonPath("$[1].appointmentEnd").value("2024-03-22T14:30:10"));
    }

    @Test
    void getFreeAppointments_AppointmentsExists_returnAppointmentsDTOList() throws Exception {
        //given
        AppointmentDTO appointmentDTO1 = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10));
        AppointmentDTO appointmentDTO2 = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 13, 30, 10),
                LocalDateTime.of(2024, 3, 22, 14, 30, 10));
        List<AppointmentDTO> appointments = List.of(appointmentDTO1, appointmentDTO2);

        when(appointmentService.getAppointments(any())).thenReturn(appointments);

        //when //then
        mockMvc.perform(get("/appointments")
                        .param("onlyFree", "true"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].appointmentStart").value("2024-03-22T15:30:10"))
                .andExpect(jsonPath("$[0].appointmentEnd").value("2024-03-22T16:30:10"))
                .andExpect(jsonPath("$[1].appointmentStart").value("2024-03-22T13:30:10"))
                .andExpect(jsonPath("$[1].appointmentEnd").value("2024-03-22T14:30:10"));
    }

    @Test
    void getFreeAppointments_appointmentsNotExists_returnNotFoundStatus() throws Exception {
        //given
        when(appointmentService.getAppointments(any())).thenThrow(AppointmentNotFoundException.class);

        //when //then
        mockMvc.perform(get("/appointments")
                        .param("onlyFree", "true"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAppointments_appointmentsNotExists_returnNotFoundStatus() throws Exception {
        //given
        when(appointmentService.getAppointments(any())).thenThrow(AppointmentNotFoundException.class);

        //when //then
        mockMvc.perform(get("/appointments")
                        .param("onlyFree", "false"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
