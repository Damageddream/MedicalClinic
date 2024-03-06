package com.damageddream.medicalclinic.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PatientFieldNullException extends RuntimeException {
    public PatientFieldNullException(String message) {
        super(message);
    }
}
