package com.damageddream.medicalclinic.rest.advice;

import com.damageddream.medicalclinic.exception.*;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@NoArgsConstructor
public class ServiceExceptionHandler {
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
                doctorNotFoundException.getMessage(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

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
                facilityNotFoundException.getMessage(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidDateTimeException.class)
    public ResponseEntity<ErrorResponse> invalidDateTimeExceptionResponse(
            InvalidDateTimeException invalidDateTimeException) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT,
                invalidDateTimeException.getMessage(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> appointmentNotFoundResponse(
            AppointmentNotFoundException appointmentNotFound) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND,
                appointmentNotFound.getMessage(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
