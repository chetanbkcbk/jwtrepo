package com.example.ums.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AppResponseBuilder {

    public <T> ResponseEntity<ResponseStructure<T>> build(HttpStatus status, String message, T data){
        return ResponseEntity.status(status)
                .body(ResponseStructure.generate(status.value(), message, data));
    }
}
