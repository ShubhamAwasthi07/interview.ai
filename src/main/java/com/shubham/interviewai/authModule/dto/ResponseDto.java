package com.shubham.interviewai.authModule.dto;

import lombok.Data;

@Data
public class ResponseDto<T> {
    private String status;
    private T data;
    private String message;
    private Integer responseCode;

    public ResponseDto() {
    }

    public ResponseDto(String status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public ResponseDto(String status, String message, Integer responseCode) {
        this.status = status;
        this.message = message;
        this.responseCode = responseCode;
    }
}
