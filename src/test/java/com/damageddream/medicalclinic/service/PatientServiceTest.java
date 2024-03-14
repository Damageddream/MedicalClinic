package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.ChangePasswordCommand;
import com.damageddream.medicalclinic.dto.NewPatientDTO;
import com.damageddream.medicalclinic.dto.PatientDTO;
import com.damageddream.medicalclinic.dto.mapper.PatientMapper;
import com.damageddream.medicalclinic.entity.Patient;
import com.damageddream.medicalclinic.exception.EmailAlreadyExistsException;
import com.damageddream.medicalclinic.exception.PatientNotFoundException;
import com.damageddream.medicalclinic.repository.PatientRepository;
import com.damageddream.medicalclinic.validation.DataValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PatientServiceTest {
    private DataValidator dataValidator;
    private PatientMapper patientMapper;
    private PatientRepository patientRepository;

    private PatientService patientService;


    @BeforeEach
    void setup() {
        this.dataValidator = Mockito.mock(DataValidator.class);
        this.patientMapper = Mockito.mock(PatientMapper.class);
        this.patientRepository = Mockito.mock(PatientRepository.class);

        this.patientService = new PatientServiceImpl(dataValidator, patientMapper, patientRepository);
    }

    @Test
    void findPatientByEmail_PatientExists_PatientDTOReturned() {
        Patient patient = Patient.builder()
                .email("mail")
                .firstName("Marcin")
                .build();
        PatientDTO patientDTO = PatientDTO.builder()
                .email("mail")
                .firstName("Marcin")
                .build();


        when(patientRepository.findByEmail("mail")).thenReturn(Optional.of(patient));
        when(patientMapper.fromPatient(patient)).thenReturn(patientDTO);

        var result = patientService.findByEmail("mail");

        assertNotNull(result);
        assertEquals("mail", result.getEmail());
        assertEquals("Marcin", result.getFirstName());

    }

    @Test
    void findPatientByEmail_PatientNotExists_PatientNotFoundExceptionIsThrown() {
        String email = "mail";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, ()-> patientService.findByEmail(email));
    }

    @Test
    void savePatient_PatientNotInDb_PatientDtoReturned() {
        PatientDTO patientDTO = PatientDTO.builder()
                .email("mail")
                .firstName("Marcin")
                .build();
        Patient patient = Patient.builder()
                .email("mail")
                .firstName("Marcin")
                .build();
        NewPatientDTO newPatientDTO = NewPatientDTO.builder()
                .email("mail")
                .firstName("Marcin")
                .build();

        when(patientRepository.findByEmail("mail")).thenReturn(Optional.empty());
        when(patientMapper.fromPatientDTO(newPatientDTO)).thenReturn(patient);
        when(patientMapper.fromPatient(patient)).thenReturn(patientDTO);

        var result = patientService.save(newPatientDTO);

        assertNotNull(result);
        assertEquals("mail", result.getEmail());
        assertEquals("Marcin", result.getFirstName());
    }

    @Test
    void savePatient_PatientInDb_EmailAlreadyExistsExceptionIsThrown() {
        Patient patient = Patient.builder()
                .email("mail")
                .firstName("Marcin")
                .build();
        NewPatientDTO newPatientDTO = NewPatientDTO.builder()
                .email("mail")
                .firstName("Marcin")
                .build();

        when(patientRepository.findByEmail("mail")).thenReturn(Optional.of(patient));

        assertThrows(EmailAlreadyExistsException.class, ()->patientService.save(newPatientDTO));
    }
    @Test
    void findAllPatients_PatientsExists_PatientDTOListReturned() {
        Patient patient = Patient.builder()
                .email("mail")
                .firstName("Marcin")
                .build();
        Patient patient2 = Patient.builder()
                .email("mail2")
                .firstName("Leszek")
                .build();
        List<Patient> patientList = List.of(patient, patient2);

        PatientDTO patientDTO = PatientDTO.builder()
                .email("mail")
                .firstName("Marcin")
                .build();
        PatientDTO patientDTO2 = PatientDTO.builder()
                .email("mail2")
                .firstName("Leszek")
                .build();
        List<PatientDTO> patientDTOList = List.of(patientDTO, patientDTO2);

        when(patientRepository.findAll()).thenReturn(patientList);
        when(patientMapper.fromPatient(patient)).thenReturn(patientDTO);
        when(patientMapper.fromPatient(patient2)).thenReturn(patientDTO2);

        var result = patientService.findAll();
        assertNotNull(result);
        assertEquals(patientDTOList, result);
    }

    @Test
    void updatePatient_PatientExists_updatedPatientDTOReturned() {
        String email = "mail";
        NewPatientDTO patient = NewPatientDTO.builder()
                .email(email)
                .firstName("UpdatedName")
                .build();

        Patient existingPatient = Patient.builder()
                .email(email)
                .firstName("OldName")
                .build();

        PatientDTO expectedPatientDTO = PatientDTO.builder()
                .email(email)
                .firstName("UpdatedName")
                .build();

        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(existingPatient));
        when(patientMapper.fromPatient(existingPatient)).thenReturn(expectedPatientDTO);

        var result = patientService.update(email, patient);

        assertNotNull(result);
        assertEquals(expectedPatientDTO,result);
    }

    @Test
    void updatePatient_PatientNotExists_PatientNotFoundExceptionIsThrown() {
        String email = "mail";
        NewPatientDTO patient = NewPatientDTO.builder()
                .email(email)
                .firstName("UpdatedName")
                .build();
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, ()-> patientService.update(email, patient));
    }

    @Test
    void deletePatient_PatientExists_PatientIsDeletedAndDTOIsReturned() {

        String email = "mail";
        Patient patient = Patient.builder()
                .email(email)
                .build();
        PatientDTO expectedPatientDTO = PatientDTO.builder()
                .email(email)
                .build();

        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));
        when(patientMapper.fromPatient(patient)).thenReturn(expectedPatientDTO);

        PatientDTO result = patientService.delete(email);

        assertNotNull(result);
        assertEquals(expectedPatientDTO, result);

    }

    @Test
    void deletePatient_PatientNotExists_PatientNotFoundExceptionIsThrown() {
        String email = "mail";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, ()-> patientService.delete(email));
    }

    @Test
    void editPassword_PatientExists_PatientUpdatedAndDTOIsReturned() {

        String email = "mail";
        String newPassword = "newPassword";
        ChangePasswordCommand command = new ChangePasswordCommand(newPassword);
        Patient patient = Patient.builder()
                .email(email)
                .build();
        PatientDTO expectedPatientDTO = PatientDTO.builder()
                .email(email)
                .build();

        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));
        when(patientMapper.fromPatient(patient)).thenReturn(expectedPatientDTO);


        PatientDTO result = patientService.editPassword(command, email);


        assertEquals(expectedPatientDTO, result);
        assertEquals(newPassword, patient.getPassword());

    }

    @Test
    void editPassword_PatientNotExists_PatientNotFoundExceptionIsThrown() {
        String email = "mail";
        String newPassword = "newPassword";
        ChangePasswordCommand command = new ChangePasswordCommand(newPassword);
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, ()-> patientService.editPassword(command, email));
    }

}
