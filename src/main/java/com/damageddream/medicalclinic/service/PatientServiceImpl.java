package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dao.PatientDAO;
import com.damageddream.medicalclinic.entity.Patient;
import com.damageddream.medicalclinic.exceptions.PatientNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService{

    private final PatientDAO patientDAO;

    @Override
    public Patient save(Patient patient) {
        var existingPatient = patientDAO.findByEmail(patient.getEmail());
        if (existingPatient.isPresent()) {
            throw new IllegalArgumentException("Patient with given email exists.");
        }
        return patientDAO.save(patient);
    }

    @Override
    public Patient findByEmail(String email) {
        return patientDAO.findByEmail(email)
                .orElseThrow(()-> new PatientNotFoundException("Patient with given mail does not exist"));
    }

    @Override
    public List<Patient> findAll() {
        return patientDAO.findAll();
    }

    @Override
    public Patient update(String email, Patient patient) {
        validateEditPatientData(email, patient);
        return patientDAO.update(email,patient);
    }

    @Override
    public Patient delete(String email) {
        return patientDAO.delete(email);
    }

    private void validateEditPatientData(String email, Patient newPatientData){
        if (!email.equalsIgnoreCase(newPatientData.getEmail())) {
            var patient = patientDAO.findByEmail(newPatientData.getEmail());
            if (patient.isPresent()) {
                throw new IllegalArgumentException("New email is not available.");
            }
        }
    }
}
