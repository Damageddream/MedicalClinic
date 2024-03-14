package com.damageddream.medicalclinic.dto.mapper;

import com.damageddream.medicalclinic.dto.NewPatientDTO;
import com.damageddream.medicalclinic.dto.PatientDTO;
import com.damageddream.medicalclinic.entity.Patient;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


public class PatientMapperTest {

    private PatientMapper patientMapper;
    private Patient patient;
    private PatientDTO patientDTO;
    private NewPatientDTO newPatientDTO;

    @BeforeEach
    void setup() {
        this.patientMapper = new PatientMapperImpl();
        this.patient = Patient.builder()
                .idCardNo("123")
                .password("password")
                .phoneNumber("123456789")
                .lastName("Grab")
                .birthday(LocalDate.of(1900,01,01))
                .email("mail")
                .firstName("Marcin")
                .build();
        this.patientDTO = PatientDTO.builder()
                .phoneNumber("123456789")
                .lastName("Grab")
                .birthday(LocalDate.of(1900,01,01))
                .email("mail")
                .firstName("Marcin")
                .build();
        this.newPatientDTO = NewPatientDTO.builder()
                .idCardNo("123")
                .password("password")
                .phoneNumber("123456789")
                .lastName("Grab")
                .birthday(LocalDate.of(1900,01,01))
                .email("mail")
                .firstName("Marcin")
                .build();
    }

    @Test
    void fromPatient_ValidPatient_PatientDTOReturned() {
        var result = patientMapper.fromPatient(patient);

        assertNotNull(result);
        assertEquals(patientDTO,result);
    }

    @Test
    void fromPatientDTO_ValidPatientDTO_PatientReturned() {
        var result = patientMapper.fromPatientDTO(newPatientDTO);

        assertNotNull(result);
        assertEquals(patient, result);
    }

    @Test
    void  updatePatientFromDTO_ValidPatientDTO_PatientUpdated () {

        Patient existingPatient = Patient.builder()
                .id(1L)
                .email("old@email.com")
                .firstName("OldName")
                .build();

        NewPatientDTO updateDTO = NewPatientDTO.builder()
                .email("new@email.com")
                .firstName("NewName")
                .build();
        patientMapper.updatePatientFromDTO(updateDTO, existingPatient);

        assertEquals("new@email.com", existingPatient.getEmail());
        assertEquals("NewName", existingPatient.getFirstName());

    }
}
