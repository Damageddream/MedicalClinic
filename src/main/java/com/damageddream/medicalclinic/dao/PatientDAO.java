package com.damageddream.medicalclinic.dao;

import com.damageddream.medicalclinic.entity.Patient;

import java.util.List;

public interface PatientDAO {
    Patient save(Patient patient);
    Patient findByEmail(String email);
    List<Patient> findAll();
    Patient update(String email, Patient patient);
    Patient delete(String email);

}
