package com.security.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import java.util.Date;

public class AppResponse<T> {

    private Date timestamp;
    private HttpStatus status;
    private String message;
    private String error;
    private String path;
    private T body;
    private T header;

    private AppResponse(){}

    private AppResponse(HttpStatus status) {
        this.status = status;
        timestamp = new Date();
    }

    public AppResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        timestamp = new Date();
    }

    public static AppResponse build(HttpStatus status) {
        return new AppResponse(status);
    }

    public AppResponse message(String message) {
        this.message = message;
        return this;
    }

    public Integer getStatus(){
        return status.value();
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public T getBody() {
        return body;
    }

    public AppResponse body(T data) {
        this.body = data;
        return this;
    }

    public AppResponse header(T data) {
        this.header = data;
        return this;
    }

    public T getHeader() {
        return header;
    }

    public String toJson() {
        return "{\"status\":"+ status +", \"message\":\"" + message + "\"}";
    }

    public String toJson2() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "{}";
    }

}
