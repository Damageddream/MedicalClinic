package com.damageddream.medicalclinic.dto;

import com.damageddream.medicalclinic.entity.Patient;
import lombok.*;

import java.time.Duration;
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
