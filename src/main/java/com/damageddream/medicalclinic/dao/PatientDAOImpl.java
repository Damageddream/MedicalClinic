package com.damageddream.medicalclinic.dao;

import com.damageddream.medicalclinic.entity.DB;
import com.damageddream.medicalclinic.entity.Patient;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class PatientDAOImpl implements PatientDAO{

    private final DB db = new DB();

    @Override
    public Patient save(Patient patient) {
        db.getPatients().add(patient);
        return patient;
    }

    @Override
    public Patient findByEmail(String email) {
        List<Patient> filteredPatients = db
                .getPatients()
                .stream()
                .filter(patient -> patient.getEmail().equals(email))
                .toList();
        if(filteredPatients.isEmpty()){
           return null;
        }
        return filteredPatients.get(0);
    }

    @Override
    public List<Patient> findAll() {
        return db.getPatients();
    }

    @Override
    public Patient update(String email, Patient patient) {
        Patient thePatient = findByEmail(email);
        if(thePatient == null){
            return null;
        }
        int index = db.getPatients().indexOf(thePatient);
        db.getPatients().set(index, patient);
        return patient;
    }

    @Override
    public Patient delete(String email) {
        Patient thePatient = findByEmail(email);
        if(thePatient == null){
            return null;
        }
        db.getPatients().remove(thePatient);
        return thePatient;
    }
}
