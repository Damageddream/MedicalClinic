package com.damageddream.medicalclinic.dto.mapper;

import com.damageddream.medicalclinic.dto.PatientCreateUpdateDTO;
import com.damageddream.medicalclinic.dto.PatientGetDTO;
import com.damageddream.medicalclinic.entity.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper  {
    PatientGetDTO patientToPatientGetDTO(Patient patient);
    Patient patentCreateUpdateDTOToPatient(PatientCreateUpdateDTO patientCreateUpdateDTO);
}
