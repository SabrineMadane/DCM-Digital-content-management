package com.example.config;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ApiResponse {
    private int code;
    private String message;

    private HttpStatus status;

    public ApiResponse(int i, String s) {
        this.code = i;
        this.message = s ;
    }
}