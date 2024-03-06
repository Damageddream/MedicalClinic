package com.damageddream.medicalclinic.service;

import com.damageddream.medicalclinic.dao.PatientDAO;
import com.damageddream.medicalclinic.entity.ChangePasswordCommand;
import com.damageddream.medicalclinic.entity.Patient;
import com.damageddream.medicalclinic.exceptions.EmailAlreadyExistsException;
import com.damageddream.medicalclinic.exceptions.ForbiddenidCardNoChangeException;
import com.damageddream.medicalclinic.exceptions.PatientFieldNullException;
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
            throw new EmailAlreadyExistsException("Patient with that email already exists");
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
        validateEditPatientData(email, patient, toEdit);
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
        var toEdit = patientDAO.findByEmail(email).orElseThrow(()->new PatientNotFoundException("Patient not found"));
        toEdit.setPassword(password.getPassword());
        return toEdit;
    }

    private void validateEditPatientData(String email, Patient newPatientData, Patient patientToEdit){
        validateIfEmailIsAvailable(email, newPatientData);
        validateIfIdCardNoIsChanged(patientToEdit, newPatientData);
        validateIfAllNewPatientFieldsAreFilled(newPatientData);
    }

    private void validateIfEmailIsAvailable(String email, Patient newPatientData){
        if (!email.equalsIgnoreCase(newPatientData.getEmail())) {
            var patient = patientDAO.findByEmail(newPatientData.getEmail());
            if (patient.isPresent()) {
                throw new IllegalArgumentException("New email is not available.");
            }
        }
    }

    private void validateIfIdCardNoIsChanged(Patient patientToEdit, Patient newPatientData){
        if(!patientToEdit.getIdCardNo().equals(newPatientData.getIdCardNo())){
            throw new ForbiddenidCardNoChangeException("Changes to Id Card No are forbidden");
        }
    }

    private void validateIfAllNewPatientFieldsAreFilled(Patient newPatientData){
        if(newPatientData.getEmail() == null
        || newPatientData.getPassword() == null
        || newPatientData.getIdCardNo() == null
        || newPatientData.getFirstName() == null
        || newPatientData.getLastName() == null
        || newPatientData.getPhoneNumber() == null
        || newPatientData.getBirthday() == null){
            throw new PatientFieldNullException("All fields of new Patient must be completed");
        }
    }



}
