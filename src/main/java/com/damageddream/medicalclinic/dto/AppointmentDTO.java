package com.damageddream.medicalclinic.dto;

import com.damageddream.medicalclinic.entity.Patient;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
public class AppointmentDTO {
    private PatientDTO patient;
    private DoctorDTO doctor;
    private LocalDateTime appointmentStart;
    private Duration duration;
}
