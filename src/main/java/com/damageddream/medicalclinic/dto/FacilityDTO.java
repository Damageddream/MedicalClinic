package com.damageddream.medicalclinic.dto;

import com.damageddream.medicalclinic.entity.Doctor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
public class FacilityDTO {
    private final String name;
    private final String city;
    private final String zipCode;
    private final String street;
    private final String buildingNo;
}
