package com.damageddream.medicalclinic.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@EqualsAndHashCode
@RequiredArgsConstructor
public class AppointmentDTO {
    private final Long id;
    private final PatientDTO patient;
    private final DoctorDTO doctor;
    private final LocalDateTime appointmentStart;
    private final LocalDateTime appointmentEnd;
}
