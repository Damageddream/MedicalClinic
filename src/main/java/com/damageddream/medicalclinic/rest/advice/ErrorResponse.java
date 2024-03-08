package com.damageddream.medicalclinic.rest.advice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final HttpStatus httpStatus;
    private final String message;
    @JsonFormat(pattern = "HH:mm:ss dd-MM-yyyy")
    private final LocalDateTime timestamp;
}
