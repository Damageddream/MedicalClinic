package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.entity.ChangePasswordCommand;
import com.damageddream.medicalclinic.entity.Patient;
import com.damageddream.medicalclinic.dto.PatientCreateDTO;

import java.util.List;

public interface PatientService {
    Patient save(PatientCreateDTO patient);
    Patient findByEmail(String email);
    List<Patient> findAll();
    Patient update(String email, PatientCreateDTO patient);
    Patient delete(String email);

    Patient editPassword(ChangePasswordCommand password, String email);
}
