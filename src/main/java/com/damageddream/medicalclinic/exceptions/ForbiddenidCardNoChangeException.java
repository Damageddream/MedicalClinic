package com.damageddream.medicalclinic.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class ForbiddenidCardNoChangeException extends RuntimeException {
    public ForbiddenidCardNoChangeException(String message) {
        super(message);
    }
}
