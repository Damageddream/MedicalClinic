package com.damageddream.medicalclinic.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
public class NewPatientDTO {
    private final String email;
    private final String password;
    private final String idCardNo;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final LocalDate birthday;
}
