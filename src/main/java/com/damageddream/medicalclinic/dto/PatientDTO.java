package com.damageddream.medicalclinic.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
@Getter
@RequiredArgsConstructor
public class PatientDTO {
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final LocalDate birthday;
}
