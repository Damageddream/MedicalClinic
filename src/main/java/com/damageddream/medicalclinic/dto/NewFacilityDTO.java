package com.damageddream.medicalclinic.dto;

import com.damageddream.medicalclinic.entity.Doctor;
import jakarta.persistence.ManyToMany;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
public class NewFacilityDTO {
    private final String name;
    private final String city;
    private final String zipCode;
    private final String street;
    private final String buildingNo;
    private final List<Doctor> doctors;
}
