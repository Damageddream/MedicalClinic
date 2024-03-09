package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.PatientGetDTO;
import com.damageddream.medicalclinic.entity.ChangePasswordCommand;
import com.damageddream.medicalclinic.dto.PatientCreateUpdateDTO;

import java.util.List;

public interface PatientService {
    PatientGetDTO save(PatientCreateUpdateDTO patient);
    PatientGetDTO findByEmail(String email);
    List<PatientGetDTO> findAll();
    PatientGetDTO update(String email, PatientCreateUpdateDTO patient);
    PatientGetDTO delete(String email);

    PatientGetDTO editPassword(ChangePasswordCommand password, String email);
}
