package com.damageddream.medicalclinic.dto.mapper;

import com.damageddream.medicalclinic.dto.NewPatientDTO;
import com.damageddream.medicalclinic.dto.PatientDTO;
import com.damageddream.medicalclinic.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientDTO toDTO(Patient patient);

    Patient fromDTO(NewPatientDTO newPatientDTO);

    void updatePatientFromDTO(NewPatientDTO dto, @MappingTarget Patient patient);
}
