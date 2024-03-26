package com.damageddream.medicalclinic.dto.mapper;


import com.damageddream.medicalclinic.dto.FacilityDTO;
import com.damageddream.medicalclinic.dto.NewFacilityDTO;
import com.damageddream.medicalclinic.entity.Facility;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FacilityMapper {
    FacilityDTO toDTO(Facility facility);

    Facility fromDTO(NewFacilityDTO newFacilityDTO);

    @Mapping(target = "doctors", ignore = true)
    void updateFacilityFromDTO(NewFacilityDTO dto, @MappingTarget Facility facility);
}
