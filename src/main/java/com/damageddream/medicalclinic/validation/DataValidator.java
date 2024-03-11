package com.damageddream.medicalclinic.validation;

import com.damageddream.medicalclinic.dto.NewPatientDTO;
import com.damageddream.medicalclinic.entity.Patient;
import com.damageddream.medicalclinic.exception.EmailAlreadyExistsException;
import com.damageddream.medicalclinic.exception.ForbiddenidCardNoChangeException;
import com.damageddream.medicalclinic.exception.PatientFieldNullException;
import com.damageddream.medicalclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataValidator {

    private final PatientRepository patientRepository;
    public void validateEditPatientData(String email, NewPatientDTO newPatientData, Patient patientToEdit){
        validateIfEmailIsAvailable(email, newPatientData);
        validateIfIdCardNoIsChanged(patientToEdit, newPatientData);
        validateIfAllNewPatientFieldsAreFilled(newPatientData);
    }

    private void validateIfEmailIsAvailable(String email, NewPatientDTO newPatientData){
        if (!email.equalsIgnoreCase(newPatientData.getEmail())) {
            var patient = patientRepository.findByEmail(newPatientData.getEmail());
            if (patient.isPresent()) {
                throw new EmailAlreadyExistsException("New email is not available.");
            }
        }
    }

    private void validateIfIdCardNoIsChanged(Patient patientToEdit, NewPatientDTO newPatientData){
        if(!patientToEdit.getIdCardNo().equals(newPatientData.getIdCardNo())){
            throw new ForbiddenidCardNoChangeException("Changes to Id Card No are forbidden");
        }
    }

    private void validateIfAllNewPatientFieldsAreFilled(NewPatientDTO newPatientData){
        if(newPatientData.getEmail() == null
                || newPatientData.getPassword() == null
                || newPatientData.getFirstName() == null
                || newPatientData.getLastName() == null
                || newPatientData.getPhoneNumber() == null
                || newPatientData.getBirthday() == null){
            throw new PatientFieldNullException("All fields of new Patient must be completed");
        }
    }
}
