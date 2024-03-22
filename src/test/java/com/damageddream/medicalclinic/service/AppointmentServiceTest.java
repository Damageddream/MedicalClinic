package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.AppointmentDTO;
import com.damageddream.medicalclinic.dto.GetIdCommand;
import com.damageddream.medicalclinic.dto.mapper.AppointmentMapper;
import com.damageddream.medicalclinic.entity.Appointment;
import com.damageddream.medicalclinic.entity.Doctor;
import com.damageddream.medicalclinic.entity.Patient;
import com.damageddream.medicalclinic.exception.AppointmentNotFoundException;
import com.damageddream.medicalclinic.exception.DoctorNotFoundException;
import com.damageddream.medicalclinic.exception.InvalidDateTimeException;
import com.damageddream.medicalclinic.exception.PatientNotFoundException;
import com.damageddream.medicalclinic.repository.AppointmentRepository;
import com.damageddream.medicalclinic.repository.DoctorRepository;
import com.damageddream.medicalclinic.repository.PatientRepository;
import com.damageddream.medicalclinic.util.TestDataFactory;
import com.damageddream.medicalclinic.validation.DataValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AppointmentServiceTest {
    private AppointmentMapper appointmentMapper;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private AppointmentRepository appointmentRepository;
    private DataValidator dataValidator;
    private AppointmentService appointmentService;

    @BeforeEach
    void setup() {
        this.appointmentMapper = Mockito.mock(AppointmentMapper.class);
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.appointmentRepository = Mockito.mock(AppointmentRepository.class);
        this.dataValidator = Mockito.mock(DataValidator.class);

        this.appointmentService = new AppointmentServiceImpl(appointmentMapper, patientRepository,
                doctorRepository, appointmentRepository, dataValidator);
    }

    @Test
    void addAppointment_validAppointment_returnAppointmentDTO() {
        //given
        Appointment appointment = TestDataFactory.createAppointment(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10)
        );
        AppointmentDTO appointmentDTO = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10)
        );

        Doctor doctor = TestDataFactory.createDoctor("doc@mail.com", "Doc");

        when(doctorRepository.findById(any())).thenReturn(Optional.of(doctor));
        when(appointmentMapper.toDTO(any())).thenReturn(appointmentDTO);

        //when
        var result = appointmentService.addAppointment(1L, appointment);

        //then
        assertNotNull(result);
        assertEquals(LocalDateTime.of(2024, 3, 22, 15, 30, 10)
                , result.getAppointmentStart());
        assertEquals(LocalDateTime.of(2024, 3, 22, 16, 30, 10)
                , result.getAppointmentEnd());
        assertEquals(1L, result.getId());
        assertEquals("mar@mail.com", result.getPatient().getEmail());
        assertEquals("Marcin", result.getPatient().getFirstName());
        assertEquals("doc@mail.com", result.getDoctor().getEmail());
        assertEquals("Doc", result.getDoctor().getFirstName());
    }

    @Test
    void addAppointment_doctorNotExists_throwDoctorNotFoundException() {
        //given
        Appointment appointment = TestDataFactory.createAppointment(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10)
        );

        when(doctorRepository.findById(any())).thenReturn(Optional.empty());

        //when // then
        DoctorNotFoundException ex = assertThrows(DoctorNotFoundException.class,
                () -> appointmentService.addAppointment(1L, appointment));
        assertEquals("Doctor not found", ex.getMessage());
    }

    @Test
    void addAppointment_overlappingAppointments_throwInvalidDateTimeException() {
        //given
        Appointment appointment = TestDataFactory.createAppointment(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10)
        );

        when(appointmentRepository.findConflictingAppointments(any(), any(), any())).thenReturn(List.of(appointment));

        //when then
        InvalidDateTimeException ex = assertThrows(InvalidDateTimeException.class,
                () -> appointmentService.addAppointment(1L, appointment));
        assertEquals("There is already appointment at this time", ex.getMessage());
    }

    @Test
    void getFreeAppointmentsByDoctor_areFreeAppointments_returnListOfAppointmentsDTO() {
        //given
        Appointment appointment1 = TestDataFactory.createAppointment(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10));
        Appointment appointment2 = TestDataFactory.createAppointment(
                LocalDateTime.of(2024, 3, 22, 13, 30, 10),
                LocalDateTime.of(2024, 3, 22, 14, 30, 10));
        List<Appointment> appointments = List.of(appointment1, appointment2);

        AppointmentDTO appointmentDTO1 = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10));
        AppointmentDTO appointmentDTO2 = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 13, 30, 10),
                LocalDateTime.of(2024, 3, 22, 14, 30, 10));

        when(appointmentRepository.findFreeAppointmentsByDoctor(any())).thenReturn(appointments);
        when(appointmentMapper.toDTO(any())).thenReturn(appointmentDTO1, appointmentDTO2);

        //when
        var result = appointmentService.getFreeAppointmentsByDoctor(1L);

        assertNotNull(result);
        assertEquals(LocalDateTime.of(2024, 3, 22, 15, 30, 10)
                , result.get(0).getAppointmentStart());
        assertEquals(LocalDateTime.of(2024, 3, 22, 16, 30, 10)
                , result.get(0).getAppointmentEnd());
        assertEquals(LocalDateTime.of(2024, 3, 22, 13, 30, 10),
                result.get(1).getAppointmentStart());
        assertEquals(LocalDateTime.of(2024, 3, 22, 14, 30, 10),
                result.get(1).getAppointmentEnd());
    }

    @Test
    void getFreeAppointmentsByDoctor_noFreeAppointments_throwAppointmentNotFoundException() {
        //given
        when(appointmentRepository.findFreeAppointmentsByDoctor(any())).thenReturn(Collections.emptyList());

        //when then
        AppointmentNotFoundException ex = assertThrows(AppointmentNotFoundException.class,
                () -> appointmentService.getFreeAppointmentsByDoctor(any()));
        assertEquals("There are no free appointments for this doctor", ex.getMessage());
    }

    @Test
    void makeAnAppointment_appointmentExists_returnAppointmentDTO() {
        //given
        GetIdCommand idCommand = new GetIdCommand(1L);
        Patient patient = TestDataFactory.createPatient("marc@email.com", "Marcin");
        Appointment appointment = TestDataFactory.createAppointment(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10)
        );
        AppointmentDTO appointmentDTO = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10)
        );
        appointment.setPatient(null);

        when(patientRepository.findById(any())).thenReturn(Optional.of(patient));
        when(appointmentRepository.findById(any())).thenReturn(Optional.of(appointment));
        when(appointmentMapper.toDTO(any())).thenReturn(appointmentDTO);

        //when
        var result = appointmentService.makeAnAppointment(2L, idCommand );

        //then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("mar@mail.com", result.getPatient().getEmail());
        assertEquals("Marcin", result.getPatient().getFirstName());
        assertEquals("doc@mail.com", result.getDoctor().getEmail());
        assertEquals("Doc", result.getDoctor().getFirstName());

    }

    @Test
    void makeAnAppointment_patientNotFound_throwPatientNotFoundException() {
        //given
        GetIdCommand idCommand = new GetIdCommand(1L);

        when(patientRepository.findById(any())).thenReturn(Optional.empty());

        //when then
        PatientNotFoundException ex = assertThrows(PatientNotFoundException.class,
                () -> appointmentService.makeAnAppointment(2L, idCommand));
        assertEquals("Patient not found", ex.getMessage());
    }

    @Test
    void makeAnAppointment_appointmentNotFound_throwAppointmentNotFoundException() {
        //given
        GetIdCommand idCommand = new GetIdCommand(1L);
        Patient patient = TestDataFactory.createPatient("marc@email.com", "Marcin");

        when(patientRepository.findById(any())).thenReturn(Optional.of(patient));
        when(appointmentRepository.findById(any())).thenReturn(Optional.empty());

        //when then
        AppointmentNotFoundException ex = assertThrows(AppointmentNotFoundException.class,
                () -> appointmentService.makeAnAppointment(2L, idCommand));
        assertEquals("There is no such appointment", ex.getMessage());
    }

    @Test
    void makeAnAppointment_appointmentTaken_throwAppointmentNotFoundException() {
        //given
        GetIdCommand idCommand = new GetIdCommand(1L);
        Patient patient = TestDataFactory.createPatient("marc@email.com", "Marcin");
        Appointment appointment = TestDataFactory.createAppointment(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10)
        );


        when(patientRepository.findById(any())).thenReturn(Optional.of(patient));
        when(appointmentRepository.findById(any())).thenReturn(Optional.of(appointment));


        //when then
        AppointmentNotFoundException ex = assertThrows(AppointmentNotFoundException.class,
                () -> appointmentService.makeAnAppointment(2L, idCommand));
        assertEquals("That appointment is no longer free", ex.getMessage());
    }

    @Test
    void getPatientAppointment_appointmentExists_returnAppointmentsDTOList() {
        //given
        Patient patient = TestDataFactory.createPatient("marc@email.com", "Marcin");
        Appointment appointment1 = TestDataFactory.createAppointment(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10));
        Appointment appointment2 = TestDataFactory.createAppointment(
                LocalDateTime.of(2024, 3, 22, 13, 30, 10),
                LocalDateTime.of(2024, 3, 22, 14, 30, 10));
        List<Appointment> appointments = List.of(appointment1, appointment2);

        AppointmentDTO appointmentDTO1 = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10));
        AppointmentDTO appointmentDTO2 = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 13, 30, 10),
                LocalDateTime.of(2024, 3, 22, 14, 30, 10));

        when(patientRepository.findById(any())).thenReturn(Optional.of(patient));
        when(appointmentRepository.findAppointmentsByPatient(any())).thenReturn(appointments);
        when(appointmentMapper.toDTO(any())).thenReturn(appointmentDTO1, appointmentDTO2);

        //when
        var result = appointmentService.getPatientsAppointments(1L);

        //then
        assertNotNull(result);
        assertEquals(LocalDateTime.of(2024, 3, 22, 15, 30, 10)
                , result.get(0).getAppointmentStart());
        assertEquals(LocalDateTime.of(2024, 3, 22, 16, 30, 10)
                , result.get(0).getAppointmentEnd());
        assertEquals(LocalDateTime.of(2024, 3, 22, 13, 30, 10),
                result.get(1).getAppointmentStart());
        assertEquals(LocalDateTime.of(2024, 3, 22, 14, 30, 10),
                result.get(1).getAppointmentEnd());
    }

    @Test
    void getPatientAppointment_patientNotFound_throwPatientNotFoundException() {
        //given
        when(patientRepository.findById(any())).thenReturn(Optional.empty());

        //when then
        PatientNotFoundException ex = assertThrows(PatientNotFoundException.class,
                () -> appointmentService.getPatientsAppointments(2L));
        assertEquals("Patient not found", ex.getMessage());
    }

    @Test
    void getPatientAppointment_noPatientAppointments_throwAppointmentNotFoundException() {
        //given
        Patient patient = TestDataFactory.createPatient("marc@email.com", "Marcin");

        when(patientRepository.findById(any())).thenReturn(Optional.of(patient));
        when(appointmentRepository.findAppointmentsByPatient(any())).thenReturn(Collections.emptyList());

        //when then
        AppointmentNotFoundException ex = assertThrows(AppointmentNotFoundException.class,
                () -> appointmentService.getPatientsAppointments(any()));
        assertEquals("Patient don't have any appointments", ex.getMessage());
    }

}
