package com.damageddream.medicalclinic.util;

import com.damageddream.medicalclinic.dto.NewPatientDTO;
import com.damageddream.medicalclinic.dto.PatientDTO;
import com.damageddream.medicalclinic.entity.Patient;


import java.time.LocalDate;

public class TestDataFactory {

    public static PatientDTO createPatientDTO(String email, String firstName) {
        return PatientDTO.builder()
                .email(email)
                .firstName(firstName)
                .lastName("GrabDTO")
                .phoneNumber("222222")
                .birthday(LocalDate.of(1902, 02, 02))
                .build();
    }

    public static Patient createPatient(String email, String firstName) {
        return Patient
                .builder()
                .id(1L)
                .firstName(firstName)
                .lastName("Grab")
                .email(email)
                .password("password")
                .idCardNo("123456")
                .phoneNumber("678910123")
                .birthday(LocalDate.of(1900, 01, 01))
                .build();
    }

    public static NewPatientDTO createNewPatientDTO(String email, String firstName, String idCardNo) {
        return NewPatientDTO.builder()
                .password("passNewDto")
                .idCardNo(idCardNo)
                .email(email)
                .firstName(firstName)
                .lastName("GrabNewDto")
                .phoneNumber("333333333")
                .birthday(LocalDate.of(1903, 03, 03))
                .build();
    }

}
