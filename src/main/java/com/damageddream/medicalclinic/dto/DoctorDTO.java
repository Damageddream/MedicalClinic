package com.damageddream.medicalclinic.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
public class DoctorDTO {
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String specialization;
}
