package com.damageddream.medicalclinic.exception;

public class FacilityNotFoundException extends RuntimeException{
    public FacilityNotFoundException(String message) {
        super(message);
    }
}
