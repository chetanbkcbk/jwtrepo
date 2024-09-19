package com.example.ums.util;

import lombok.*;

@Getter
@Setter
public class ResponseStructure<T> {

    private int status;
    private String message;
    private T data;

    public static <T> ResponseStructure<T> generate(int status, String message, T data){
        ResponseStructure<T> structure = new ResponseStructure<>();
        structure.setData(data);
        structure.setStatus(status);
        structure.setMessage(message);

        return structure;
    }
}
