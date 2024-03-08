package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dao.PatientDAO;
import com.damageddream.medicalclinic.entity.ChangePasswordCommand;
import com.damageddream.medicalclinic.entity.Patient;
import com.damageddream.medicalclinic.exception.EmailAlreadyExistsException;
import com.damageddream.medicalclinic.exception.PatientNotFoundException;
import com.damageddream.medicalclinic.validation.DataValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientDAO patientDAO;
    private final DataValidator dataValidator;

    @Override
    public Patient save(Patient patient) {
        var existingPatient = patientDAO.findByEmail(patient.getEmail());
        if (existingPatient.isPresent()) {
            //throw new EmailAlreadyExistsException("Patient with that email already exists");
            throw new IllegalArgumentException("test");

        }
        return patientDAO.save(patient);
    }

    @Override
    public Patient findByEmail(String email) {
        return patientDAO.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patinet not found"));
    }

    @Override
    public List<Patient> findAll() {
        return patientDAO.findAll();
    }

    @Override
    public Patient update(String email, Patient patient) {
        var toEdit = patientDAO.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patinet not found"));
        dataValidator.validateEditPatientData(email, patient, toEdit);
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

    @Override
    public Patient editPassword(ChangePasswordCommand password, String email) {
        var toEdit = patientDAO.findByEmail(email).orElseThrow(() -> new PatientNotFoundException("Patient not found"));
        toEdit.setPassword(password.getPassword());
        return toEdit;
    }

}
