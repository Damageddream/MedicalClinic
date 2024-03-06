package com.damageddream.medicalclinic.dao;

import com.damageddream.medicalclinic.db.DB;
import com.damageddream.medicalclinic.entity.Patient;
import com.damageddream.medicalclinic.exceptions.PatientNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class PatientDAOImpl implements PatientDAO{

    private final DB db;

    @Override
    public Patient save(Patient patient) {
        db.getPatients().add(patient);
        return patient;
    }

    @Override
    public Optional<Patient> findByEmail(String email) {
        return db.getPatients().stream()
                .filter(patient -> patient.getEmail() != null && patient.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<Patient> findAll() {
        return db.getPatients();
    }

    @Override
    public Patient update(String email, Patient patient) {
        var thePatient = findByEmail(email)
                .orElseThrow(()-> new PatientNotFoundException("Patient with given mail does not exist"));;
        thePatient.update(patient);
        return patient;
    }

    @Override
    public Patient delete(String email) {
        var thePatient = findByEmail(email)
                .orElseThrow(()-> new PatientNotFoundException("Patient with given mail does not exist"));;
        db.getPatients().remove(thePatient);
        return thePatient;
    }
}
