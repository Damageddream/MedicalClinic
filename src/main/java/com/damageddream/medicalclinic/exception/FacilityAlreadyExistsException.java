package com.damageddream.medicalclinic.exception;

public class FacilityAlreadyExistsException extends RuntimeException{
    public FacilityAlreadyExistsException(String message) {
        super(message);
    }
}
