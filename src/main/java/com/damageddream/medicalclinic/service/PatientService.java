package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dto.ChangePasswordCommand;
import com.damageddream.medicalclinic.dto.NewPatientDTO;
import com.damageddream.medicalclinic.dto.PatientDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatientService {
    PatientDTO save(NewPatientDTO patient);

    PatientDTO findByEmail(String email);

    List<PatientDTO> findAll(Pageable pageable);

    PatientDTO update(String email, NewPatientDTO patient);

    PatientDTO delete(String email);

    PatientDTO editPassword(ChangePasswordCommand password, String email);

}
