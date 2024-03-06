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
                .orElseThrow(()-> new PatientNotFoundException("Patinet not found"));
    }

    @Override
    public List<Patient> findAll() {
        return patientDAO.findAll();
    }

    @Override
    public Patient update(String email, Patient patient) {
        var toEdit =patientDAO.findByEmail(email)
                        .orElseThrow(()-> new PatientNotFoundException("Patinet not found"));
        validateEditPatientData(email, patient);
        toEdit.update(patient);
        return toEdit;
    }

    @Override
    public Patient delete(String email) {
        var existingPatient = patientDAO.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        patientDAO.delete(existingPatient);
        return existingPatient;
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
