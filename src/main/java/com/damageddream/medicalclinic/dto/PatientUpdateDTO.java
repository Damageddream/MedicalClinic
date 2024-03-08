package com.damageddream.medicalclinic.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
@Getter
@RequiredArgsConstructor
public class PatientUpdateDTO {
    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final LocalDate birthday;
}
