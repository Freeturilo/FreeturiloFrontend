package com.example.freeturilo.connection;

public interface APIAction<T> {
    T go() throws APIException;
}
