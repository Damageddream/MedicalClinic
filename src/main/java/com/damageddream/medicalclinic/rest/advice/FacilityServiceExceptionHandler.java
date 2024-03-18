package com.damageddream.medicalclinic.rest.advice;

import com.damageddream.medicalclinic.exception.DoctorAlreadyExistsException;
import com.damageddream.medicalclinic.exception.DoctorNotFoundException;
import com.damageddream.medicalclinic.exception.FacilityAlreadyExistsException;
import com.damageddream.medicalclinic.exception.FacilityNotFoundException;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
@RestControllerAdvice
@NoArgsConstructor
public class FacilityServiceExceptionHandler {
    @ExceptionHandler(FacilityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> facilityAlreadyExistsExceptionResponse(
            FacilityAlreadyExistsException facilityAlreadyExistsException) {

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT,
                facilityAlreadyExistsException.getMessage(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(FacilityNotFoundException.class)
    public ResponseEntity<ErrorResponse> facilityNotFoundExceptionResponse(
            FacilityNotFoundException facilityNotFoundException) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,
                facilityNotFoundException.getMessage(),LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}

