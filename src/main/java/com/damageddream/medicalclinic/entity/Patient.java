package com.damageddream.medicalclinic.entity;

import com.damageddream.medicalclinic.dto.PatientCreateUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Patient {
    private String email;
    private String password;
    private String idCardNo;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthday;

    public void update(PatientCreateUpdateDTO newPatientData) {
        this.email = newPatientData.getEmail();
        this.firstName = newPatientData.getFirstName();
        this.lastName = newPatientData.getLastName();
        this.password = newPatientData.getPassword();
        this.birthday = newPatientData.getBirthday();
        this.phoneNumber = newPatientData.getPhoneNumber();
    }
}
