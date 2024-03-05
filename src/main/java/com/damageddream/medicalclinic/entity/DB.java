package com.damageddream.medicalclinic.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Data
public class DB {
    private List<Patient> patients = new ArrayList<>();

    public DB(){
        createPatients();
    }
    private void createPatients(){
        Patient patient1 = new Patient("pat1@gmail.com", "passw", 1, "Pat1", "Tient1", "111111", LocalDate.of(1991, 1,1));
        Patient patient2 = new Patient("pat2@gmail.com", "passw", 2, "Pat2", "Tient2", "222222", LocalDate.of(1992, 2,2));
        Patient patient3 = new Patient("pat3@gmail.com", "passw", 3, "Pat3", "Tient3", "333333", LocalDate.of(1993, 3,3));
        Patient patient4 = new Patient("pat4@gmail.com", "passw", 4, "Pat4", "Tient4", "444444", LocalDate.of(1994, 4,4));
        Patient patient5 = new Patient("pat5@gmail.com", "passw", 5, "Pat5", "Tient5", "555555", LocalDate.of(1995, 5,5));

        patients.add(patient1);
        patients.add(patient2);
        patients.add(patient3);
        patients.add(patient4);
        patients.add(patient5);
    }
}
