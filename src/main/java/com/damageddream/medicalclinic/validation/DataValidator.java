package com.damageddream.medicalclinic.validation;

import com.damageddream.medicalclinic.dto.NewPatientDTO;
import com.damageddream.medicalclinic.entity.Patient;
import com.damageddream.medicalclinic.exception.InvalidDateTimeException;
import com.damageddream.medicalclinic.exception.EmailAlreadyExistsException;
import com.damageddream.medicalclinic.exception.ForbiddenidCardNoChangeException;
import com.damageddream.medicalclinic.exception.PatientFieldNullException;
import com.damageddream.medicalclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataValidator {

    private final PatientRepository patientRepository;
    public void validateEditPatientData(String email, NewPatientDTO newPatientData, Patient patientToEdit){
        validateIfEmailIsAvailable(email, newPatientData);
        validateIfIdCardNoIsChanged(patientToEdit, newPatientData);
        validateIfAllNewPatientFieldsAreFilled(newPatientData);
    }

    public void validateDateTime(LocalDateTime startDate, LocalDateTime endDate){
        validateIfdateIsNotPast(startDate);
        validateIfEndNotBeforeStart(startDate, endDate);
        validateIfAppointmentAtLeast30min(startDate, endDate);
        validateIfAppointmentNotLongerThen2h(startDate, endDate);
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

    private void validateIfdateIsNotPast(LocalDateTime startDate){
        LocalDateTime now = LocalDateTime.now().plusHours(2);
        if(startDate.isBefore(now)){
            throw new InvalidDateTimeException("You have to choose date after "+now.toString());
        }
    }

    private void validateIfEndNotBeforeStart(LocalDateTime startDate, LocalDateTime endDate) {
        if(endDate.isBefore(startDate) || endDate.isEqual(startDate)){
            throw new InvalidDateTimeException("End date cannot be equal or before start date");
        }
    }

    private void validateIfAppointmentAtLeast30min(LocalDateTime startDate, LocalDateTime endDate) {
        if(endDate.isBefore(startDate.plusMinutes(30))){
            throw new InvalidDateTimeException("Appointment duration must be at least 30 minutes");
        }
    }

    private void validateIfAppointmentNotLongerThen2h(LocalDateTime startDate, LocalDateTime endDate) {
        if(endDate.isAfter(startDate.plusHours(2))){
            throw new InvalidDateTimeException("Appointment duration must be max 2 hours");
        }
    }
}
