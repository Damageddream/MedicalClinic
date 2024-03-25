package com.damageddream.medicalclinic.dto.mapper;

import com.damageddream.medicalclinic.dto.DoctorDTO;
import com.damageddream.medicalclinic.dto.NewDoctorDTO;
import com.damageddream.medicalclinic.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDTO toDTO(Doctor doctor);

    Doctor fromDTO(NewDoctorDTO newDoctorDTO);
    @Mapping(target = "facilities", ignore = true)
    void updateDoctorFromDTO(NewDoctorDTO dto, @MappingTarget Doctor doctor);
}
