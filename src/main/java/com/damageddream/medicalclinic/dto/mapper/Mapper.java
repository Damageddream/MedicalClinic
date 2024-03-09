package com.damageddream.medicalclinic.dto.mapper;

import com.damageddream.medicalclinic.dto.PatientCreateUpdateDTO;
import com.damageddream.medicalclinic.dto.PatientGetDTO;
import com.damageddream.medicalclinic.entity.Patient;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class Mapper {

    public PatientGetDTO toDTOget(Patient patient){
        return new PatientGetDTO(
                patient.getEmail(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getPhoneNumber(),
                patient.getBirthday());
    }

    public Patient fromDTOcreate(PatientCreateUpdateDTO patientCreateData) {
        return new Patient(
                patientCreateData.getEmail(),
                patientCreateData.getPassword(),
                patientCreateData.getIdCardNo(),
                patientCreateData.getFirstName(),
                patientCreateData.getLastName(),
                patientCreateData.getPhoneNumber(),
                patientCreateData.getBirthday()
        );
    }

}
