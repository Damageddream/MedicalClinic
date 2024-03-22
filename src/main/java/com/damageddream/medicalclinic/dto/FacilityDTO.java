package com.damageddream.medicalclinic.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
