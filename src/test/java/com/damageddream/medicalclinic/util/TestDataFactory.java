package com.damageddream.medicalclinic.util;

import com.damageddream.medicalclinic.dto.NewPatientDTO;
import com.damageddream.medicalclinic.dto.PatientDTO;
import com.damageddream.medicalclinic.entity.Patient;


import java.time.LocalDate;

public class TestDataFactory {
    private static final Patient DEAFULT_PATIENT = Patient
            .builder()
            .id(1L)
            .firstName("Mar")
            .lastName("Grab")
            .email("mar@email.com")
            .password("password")
            .idCardNo("123456")
            .phoneNumber("678910123")
            .birthday(LocalDate.of(1900,01,01))
            .build();
    private static final PatientDTO DEFAULT_PATIENTDTO = PatientDTO
            .builder()
            .email("marDTO@email.com")
            .firstName("MarDTO")
            .lastName("GrabDTO")
            .phoneNumber("222222")
            .birthday(LocalDate.of(1902,02,02))
            .build();

    private static final NewPatientDTO DEFAULT_NEWPATIENTDTO = NewPatientDTO
            .builder()
            .password("passNewDto")
            .idCardNo("654321")
            .email("marNewDto@email.com")
            .firstName("MarNewDto")
            .lastName("GrabNewDto")
            .phoneNumber("333333333")
            .birthday(LocalDate.of(1903,03,03))
            .build();

    public static Patient getDefault_PATIENT() {
        return DEAFULT_PATIENT;
    }

    public static PatientDTO getDefault_PATIENTDTO() {
        return DEFAULT_PATIENTDTO;
    }

    public static NewPatientDTO getDefault_NEWPATIENTDTO() {
        return DEFAULT_NEWPATIENTDTO;
    }

}
