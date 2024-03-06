package com.damageddream.medicalclinic.dao;

import com.damageddream.medicalclinic.db.DB;
import com.damageddream.medicalclinic.entity.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
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
        return new ArrayList<>(db.getPatients());
    }

    @Override
    public Patient delete(Patient patient) {
        db.getPatients().remove(patient);
        return patient;
    }
}
