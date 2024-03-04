package com.damageddream.medicalclinic.model;

import java.time.LocalDate;
import java.util.Date;

public class User {
    private String email;
    private String password;
    private int idCardNo;
    private String firstaName;

    private String lastName;
    private String phoneNumber;

    private LocalDate birthday;

    public User(String email, String password, int idCardNo, String firstaName, String lastName, String phoneNumber, LocalDate birthday) {
        this.email = email;
        this.password = password;
        this.idCardNo = idCardNo;
        this.firstaName = firstaName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(int idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getFirstaName() {
        return firstaName;
    }

    public void setFirstaName(String firstaName) {
        this.firstaName = firstaName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}
