package com.damageddream.medicalclinic.dao;

import com.damageddream.medicalclinic.entity.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientDAO {
    Patient save(Patient patient);
    Optional<Patient> findByEmail(String email);
    List<Patient> findAll();
    Patient delete(Patient patient);

}
