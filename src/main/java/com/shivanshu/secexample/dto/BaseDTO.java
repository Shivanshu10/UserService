package com.shivanshu.secexample.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseDTO<T> {
    private String message;
    private HttpStatus statusCode;
    T data;
}
