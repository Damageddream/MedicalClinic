package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.ChangePasswordCommand;
import com.damageddream.medicalclinic.dto.NewPatientDTO;
import com.damageddream.medicalclinic.dto.PatientDTO;
import com.damageddream.medicalclinic.dto.mapper.PatientMapper;
import com.damageddream.medicalclinic.entity.Patient;
import com.damageddream.medicalclinic.exception.EmailAlreadyExistsException;
import com.damageddream.medicalclinic.exception.PatientNotFoundException;
import com.damageddream.medicalclinic.repository.PatientRepository;
import com.damageddream.medicalclinic.util.TestDataFactory;
import com.damageddream.medicalclinic.validation.DataValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



public class PatientServiceTest {
    private DataValidator dataValidator;
    private PatientMapper patientMapper;
    private PatientRepository patientRepository;

    private PatientService patientService;


    @BeforeEach
    void setup() {
        this.dataValidator = Mockito.mock(DataValidator.class);
        this.patientMapper = Mappers.getMapper(PatientMapper.class);
        this.patientRepository = Mockito.mock(PatientRepository.class);

        this.patientService = new PatientServiceImpl(dataValidator, patientMapper, patientRepository);
    }

    @Test
    void findPatientByEmail_PatientExists_PatientDTOReturned() {
        //given
        String email = "mar@email.com";
        Patient patient = TestDataFactory.getDefault_PATIENT();

        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));

        //when
        var result = patientService.findByEmail(email);

        //then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("Mar", result.getFirstName());
        assertEquals("Grab", result.getLastName());
        assertEquals("678910123", result.getPhoneNumber());
        assertEquals(LocalDate.of(1900,01,01), result.getBirthday());

        verify(patientRepository, times(1)).findByEmail(email);
    }

    @Test
    void findPatientByEmail_PatientNotExists_PatientNotFoundExceptionIsThrown() {
        //given
        String email = "mail";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());

        //then                                              //when
        PatientNotFoundException ex = assertThrows(PatientNotFoundException.class,
                () -> patientService.findByEmail(email));
        assertEquals("Patient not found", ex.getMessage());

        verify(patientRepository, times(1)).findByEmail(email);
    }

    @Test
    void savePatient_PatientNotInDB_PatientDtoReturned() {
        //given
        String email = "marNewDto@email.com";
        NewPatientDTO newPatientDTO = TestDataFactory.getNewPatientDTO();

        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());

        //when
        var result = patientService.save(newPatientDTO);

        //then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("MarNewDto", result.getFirstName());
        assertEquals("GrabNewDto", result.getLastName());
        assertEquals("333333333", result.getPhoneNumber());
        assertEquals(LocalDate.of(1903,03,03), result.getBirthday());

        verify(patientRepository, times(1)).findByEmail(email);
    }

    @Test
    void savePatient_PatientInDB_EmailAlreadyExistsExceptionIsThrown() {
        //given
        Patient patient = TestDataFactory.getDefault_PATIENT();
        NewPatientDTO newPatientDTO = TestDataFactory.getNewPatientDTO();

        when(patientRepository.findByEmail("marNewDto@email.com")).thenReturn(Optional.of(patient));

        //then                                            //when
        EmailAlreadyExistsException ex = assertThrows(EmailAlreadyExistsException.class,
                () -> patientService.save(newPatientDTO));
        assertEquals("Patient with that email already exists", ex.getMessage());

        verify(patientRepository, times(1)).findByEmail("marNewDto@email.com");
    }
    @Test
    void findAllPatients_PatientsExists_PatientDTOListReturned() {
        //given
        Patient patient = TestDataFactory.getDefault_PATIENT();
        Patient patient2 = TestDataFactory.createPatient("lesz@email.com", "Leszek");
        List<Patient> patientList = List.of(patient, patient2);

        when(patientRepository.findAll()).thenReturn(patientList);

        //when
        var result = patientService.findAll();

        //then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("mar@email.com", result.get(0).getEmail());
        assertEquals("lesz@email.com", result.get(1).getEmail());

        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void updatePatient_PatientExists_updatedPatientDTOReturned() {
        //given
        String email = "mar@email.com";
        NewPatientDTO patient = TestDataFactory.getNewPatientDTO();
        Patient existingPatient = TestDataFactory.getDefault_PATIENT();

        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(existingPatient));

        //when
        var result = patientService.update(email, patient);

        //then
        assertNotNull(result);
        assertEquals("MarNewDto", result.getFirstName());
        assertEquals("GrabNewDto", result.getLastName());
        assertEquals("marNewDto@email.com", result.getEmail());
        assertEquals("333333333", result.getPhoneNumber());
        assertEquals(LocalDate.of(1903,03,03), result.getBirthday());

        verify(patientRepository, times(1)).findByEmail(email);
    }

    @Test
    void updatePatient_PatientNotExists_PatientNotFoundExceptionIsThrown() {
        //given
        String email = "mail";
        NewPatientDTO patient = TestDataFactory.getNewPatientDTO();

        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());

        //then                                       //when
        PatientNotFoundException ex = assertThrows(PatientNotFoundException.class,
                () -> patientService.update(email, patient));
        assertEquals("Patient not found", ex.getMessage());

        verify(patientRepository, times(1)).findByEmail(email);
    }

    @Test
    void deletePatient_PatientExists_PatientIsDeletedAndDTOIsReturned() {
        //given
        String email = "mar@email.com";
        Patient patient = TestDataFactory.getDefault_PATIENT();

        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));

        //when
        PatientDTO result = patientService.delete(email);

        //then
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("Mar", result.getFirstName());
        assertEquals("Grab", result.getLastName());
        assertEquals("678910123", result.getPhoneNumber());
        assertEquals(LocalDate.of(1900,01,01), result.getBirthday());

        verify(patientRepository, times(1)).findByEmail(email);

    }

    @Test
    void deletePatient_PatientNotExists_PatientNotFoundExceptionIsThrown() {
        //given
        String email = "mail";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());

        //then                                         //when
        PatientNotFoundException ex = assertThrows(PatientNotFoundException.class,
                () -> patientService.delete(email));
        assertEquals("Patient not found", ex.getMessage());

        verify(patientRepository, times(1)).findByEmail(email);
    }

    @Test
    void editPassword_PatientExists_PatientUpdatedAndDTOIsReturned() {
        //given
        String email = "mar@email.com";
        String newPassword = "newPassword";
        ChangePasswordCommand command = new ChangePasswordCommand(newPassword);
        Patient patient = TestDataFactory.getDefault_PATIENT();

        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));

        //when
        PatientDTO result = patientService.editPassword(command, email);

        //then
        assertNotNull(result);
        assertEquals(newPassword, patient.getPassword());
        assertEquals(email, result.getEmail());
        assertEquals("Mar", result.getFirstName());
        assertEquals("Grab", result.getLastName());
        assertEquals("678910123", result.getPhoneNumber());
        assertEquals(LocalDate.of(1900,01,01), result.getBirthday());

        verify(patientRepository, times(1)).findByEmail(email);

    }

    @Test
    void editPassword_PatientNotExists_PatientNotFoundExceptionIsThrown() {
        //given
        String email = "mail";
        String newPassword = "newPassword";
        ChangePasswordCommand command = new ChangePasswordCommand(newPassword);

        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());
        //then                                        //when
        PatientNotFoundException ex = assertThrows(PatientNotFoundException.class,
                () -> patientService.editPassword(command, email));

        assertEquals("Patient not found", ex.getMessage());

        verify(patientRepository, times(1)).findByEmail(email);
    }

}
