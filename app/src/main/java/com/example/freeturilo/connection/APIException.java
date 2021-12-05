package com.example.freeturilo.connection;

public class APIException extends Exception {
    public int responseCode;

    public APIException(int responseCode) {
        this.responseCode = responseCode;
    }
}
