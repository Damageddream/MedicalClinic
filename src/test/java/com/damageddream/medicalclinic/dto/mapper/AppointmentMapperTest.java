package com.damageddream.medicalclinic.dto.mapper;

import com.damageddream.medicalclinic.dto.AppointmentDTO;
import com.damageddream.medicalclinic.dto.DoctorDTO;
import com.damageddream.medicalclinic.dto.PatientDTO;
import com.damageddream.medicalclinic.entity.Appointment;
import com.damageddream.medicalclinic.entity.Doctor;
import com.damageddream.medicalclinic.entity.Patient;
import com.damageddream.medicalclinic.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AppointmentMapperTest {

    private AppointmentMapper appointmentMapper;
    private DoctorMapper doctorMapper;
    private PatientMapper patientMapper;

    @BeforeEach
    void setup()
    {
        this.appointmentMapper = Mappers.getMapper(AppointmentMapper.class);
        this.patientMapper = Mockito.mock(PatientMapper.class);
        this.doctorMapper =Mockito.mock(DoctorMapper.class);

        ReflectionTestUtils.setField(appointmentMapper, "patientMapper", patientMapper);
        ReflectionTestUtils.setField(appointmentMapper, "doctorMapper", doctorMapper);
    }

    @Test
    void fromAppointment_validAppointment_AppointmentDTOReturned(){
        //given
        Appointment appointment = TestDataFactory.createAppointment(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10)
        );

        DoctorDTO doctor = TestDataFactory.createDoctorDTO("doc@mail.com", "Doc");
        PatientDTO patient = TestDataFactory.createPatientDTO("mar@mail.com", "Marcin");

        when(doctorMapper.toDTO(any())).thenReturn(doctor);
        when(patientMapper.toDTO(any())).thenReturn(patient);

        //when
        var result = appointmentMapper.toDTO(appointment);

        //then
        assertNotNull(result);
        assertEquals(LocalDateTime.of(2024, 3, 22, 15, 30, 10)
        ,result.getAppointmentStart());
        assertEquals( LocalDateTime.of(2024, 3, 22, 16, 30, 10)
        ,result.getAppointmentEnd());
        assertEquals(1L, result.getId());
        assertEquals("mar@mail.com", result.getPatient().getEmail());
        assertEquals("Marcin", result.getPatient().getFirstName());
        assertEquals("doc@mail.com", result.getDoctor().getEmail());
        assertEquals("Doc", result.getDoctor().getFirstName());
    }

    @Test
    void fromAppointmentDTO_validAppointmentDTO_appointmentReturned() {
        //given
        AppointmentDTO appointmentDTO = TestDataFactory.createAppointmentDTO(
                LocalDateTime.of(2024, 3, 22, 15, 30, 10),
                LocalDateTime.of(2024, 3, 22, 16, 30, 10)
        );

        Doctor doctor = TestDataFactory.createDoctor("doc@mail.com", "Doc");
        Patient patient = TestDataFactory.createPatient("mar@mail.com", "Marcin");

        when(doctorMapper.fromDTO(any())).thenReturn(doctor);
        when(patientMapper.fromDTO(any())).thenReturn(patient);

        //when
        var result = appointmentMapper.fromDTO(appointmentDTO);

        //then
        assertNotNull(result);
        assertEquals(LocalDateTime.of(2024, 3, 22, 15, 30, 10)
                ,result.getAppointmentStart());
        assertEquals( LocalDateTime.of(2024, 3, 22, 16, 30, 10)
                ,result.getAppointmentEnd());
        assertEquals(1L, result.getId());
        assertEquals("mar@mail.com", result.getPatient().getEmail());
        assertEquals("Marcin", result.getPatient().getFirstName());
        assertEquals("doc@mail.com", result.getDoctor().getEmail());
        assertEquals("Doc", result.getDoctor().getFirstName());
    }
}
