package com.shivanshu.secexample.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.shivanshu.secexample.dto.BaseDTO;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<BaseDTO<Object>> handleGenericException(Exception e) {
        BaseDTO<Object> data = BaseDTO.<Object>builder()
                .message("An unexpected error occurred: " + e.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .data(null)
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(data);
    }
}
