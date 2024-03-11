package com.damageddream.medicalclinic.dto.mapper;

import com.damageddream.medicalclinic.dto.NewPatientDTO;
import com.damageddream.medicalclinic.dto.PatientDTO;
import com.damageddream.medicalclinic.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PatientMapper  {
    PatientDTO fromPatient(Patient patient);
    Patient fromPatientDTO(NewPatientDTO newPatientDTO);
    void updatePatientFromDTO(NewPatientDTO dto, @MappingTarget Patient patient);
}
