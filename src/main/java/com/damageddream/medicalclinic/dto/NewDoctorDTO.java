package com.damageddream.medicalclinic.dto;

import com.damageddream.medicalclinic.entity.Facility;
import lombok.*;

import java.util.List;

@Getter
@Builder
@EqualsAndHashCode
@RequiredArgsConstructor
public class NewDoctorDTO {
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String specialization;
    private final String password;
    private final List<Facility> facilities;
}
