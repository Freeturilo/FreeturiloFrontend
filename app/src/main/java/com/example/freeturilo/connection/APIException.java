package com.example.freeturilo.connection;

public class APIException extends Exception {
    final public int responseCode;

    public APIException(int responseCode) {
        this.responseCode = responseCode;
    }
}
