package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dao.PatientDAO;
import com.damageddream.medicalclinic.entity.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PatientServiceImpl implements PatientService{

    private PatientDAO patientDAO;

    @Autowired
    public PatientServiceImpl(PatientDAO patientDAO){
        this.patientDAO = patientDAO;
    }

    @Override
    public Patient save(Patient patient) {
        return patientDAO.save(patient);
    }

    @Override
    public Patient findByEmail(String email) {
        Patient thePatient = patientDAO.findByEmail(email);
        return thePatient;
    }

    @Override
    public List<Patient> findAll() {
        return patientDAO.findAll();
    }

    @Override
    public Patient update(String email, Patient patient) {
        return patientDAO.update(email,patient);
    }

    @Override
    public Patient delete(String email) {
        return patientDAO.delete(email);
    }
}
