package com.damageddream.medicalclinic.rest.advice;

import com.damageddream.medicalclinic.exception.EmailAlreadyExistsException;
import com.damageddream.medicalclinic.exception.ForbiddenidCardNoChangeException;
import com.damageddream.medicalclinic.exception.PatientFieldNullException;
import com.damageddream.medicalclinic.exception.PatientNotFoundException;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;

@RestControllerAdvice
@NoArgsConstructor
public class PatientServiceExceptionHandler {
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> emailAlreadyExistsExceptionResponse(
            EmailAlreadyExistsException emailAlreadyExistsException) {

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT,
                emailAlreadyExistsException.getMessage(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(ForbiddenidCardNoChangeException.class)
    public ResponseEntity<ErrorResponse> forbiddenIdCardNoChangeExceptionHandler(
            ForbiddenidCardNoChangeException forbiddenidCardNoChangeException) {

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN,
                forbiddenidCardNoChangeException.getMessage(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(PatientFieldNullException.class)
    public ResponseEntity<ErrorResponse> patientFieldNullExceptionHandler(
            PatientFieldNullException patientFieldNullException) {

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,
                patientFieldNullException.getMessage(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ErrorResponse> patientNotFoundExceptionHandler(
            PatientNotFoundException patientNotFoundException) {

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,
                patientNotFoundException.getMessage(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex) {
        String messageOfResponse = "Something went wrong, we're working on it!";
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, messageOfResponse, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
