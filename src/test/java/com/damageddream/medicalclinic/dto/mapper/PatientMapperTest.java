package com.damageddream.medicalclinic.dto.mapper;

import com.damageddream.medicalclinic.dto.NewPatientDTO;
import com.damageddream.medicalclinic.entity.Patient;
import static org.junit.jupiter.api.Assertions.*;

import com.damageddream.medicalclinic.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;


public class PatientMapperTest {

    private PatientMapper patientMapper;

    @BeforeEach
    void setup() {
        this.patientMapper = Mappers.getMapper(PatientMapper.class);
    }

    @Test
    void fromPatient_ValidPatient_PatientDTOReturned() {
        //given
        Patient patient = TestDataFactory.getDefault_PATIENT();

        //when
        var result = patientMapper.toDTO(patient);

        //then
        assertNotNull(result);
        assertEquals("Mar", result.getFirstName());
        assertEquals("Grab", result.getLastName());
        assertEquals("mar@email.com", result.getEmail());
        assertEquals("678910123", result.getPhoneNumber());
        assertEquals(LocalDate.of(1900,01,01), result.getBirthday());

    }

    @Test
    void fromPatientDTO_ValidPatientDTO_PatientReturned() {
        //given
        NewPatientDTO newPatientDTO = TestDataFactory.getDefault_NEWPATIENTDTO();

        //when
        var result = patientMapper.fromDTO(newPatientDTO);

        //then
        assertNotNull(result);
        assertEquals("passNewDto", result.getPassword());
        assertEquals("654321", result.getIdCardNo());
        assertEquals("marNewDto@email.com", result.getEmail());
        assertEquals("MarNewDto", result.getFirstName());
        assertEquals("GrabNewDto", result.getLastName());
        assertEquals("333333333", result.getPhoneNumber());
        assertEquals(LocalDate.of(1903,03,03), result.getBirthday());
    }

    @Test
    void  updatePatientFromDTO_ValidPatientDTO_PatientUpdated () {
        //given
        Patient existingPatient = TestDataFactory.getDefault_PATIENT();
        NewPatientDTO updateDTO = TestDataFactory.getDefault_NEWPATIENTDTO();

        //when
        patientMapper.updatePatientFromDTO(updateDTO, existingPatient);

        //then
        assertNotNull(existingPatient);
        assertEquals(1L, existingPatient.getId());
        assertEquals("marNewDto@email.com", existingPatient.getEmail());
        assertEquals("MarNewDto", existingPatient.getFirstName());
        assertEquals("GrabNewDto", existingPatient.getLastName());
        assertEquals("333333333", existingPatient.getPhoneNumber());
        assertEquals("654321", existingPatient.getIdCardNo());
        assertEquals("passNewDto", existingPatient.getPassword());
        assertEquals(LocalDate.of(1903,03,03), existingPatient.getBirthday());

        assertNotEquals("mar@email.com", existingPatient.getEmail());
        assertNotEquals("Mar", existingPatient.getFirstName());
        assertNotEquals("Grab", existingPatient.getLastName());
        assertNotEquals("678910123", existingPatient.getPhoneNumber());
        assertNotEquals("123456", existingPatient.getIdCardNo());
        assertNotEquals("password", existingPatient.getPassword());
        assertNotEquals(LocalDate.of(1900,01,01), existingPatient.getBirthday());

    }
}
