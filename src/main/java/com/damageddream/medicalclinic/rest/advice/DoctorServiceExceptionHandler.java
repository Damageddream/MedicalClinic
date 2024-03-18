package com.damageddream.medicalclinic.rest.advice;

import com.damageddream.medicalclinic.exception.DoctorAlreadyExistsException;
import com.damageddream.medicalclinic.exception.DoctorNotFoundException;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@NoArgsConstructor
public class DoctorServiceExceptionHandler {
    @ExceptionHandler(DoctorAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> doctorAlreadyExistsExceptionResponse(
            DoctorAlreadyExistsException doctorAlreadyExistsException) {

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT,
                doctorAlreadyExistsException.getMessage(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<ErrorResponse> doctorNotFoundExceptionResponse(
            DoctorNotFoundException doctorNotFoundException) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,
                doctorNotFoundException.getMessage(),LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
