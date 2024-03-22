package com.damageddream.medicalclinic.dto.mapper;

import com.damageddream.medicalclinic.dto.DoctorDTO;
import com.damageddream.medicalclinic.dto.NewDoctorDTO;
import com.damageddream.medicalclinic.entity.Doctor;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDTO toDTO(Doctor doctor);

    Doctor fromDTO(NewDoctorDTO newDoctorDTO);
}
