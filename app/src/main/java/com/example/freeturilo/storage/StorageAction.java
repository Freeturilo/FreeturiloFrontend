package com.example.freeturilo.storage;

public interface StorageAction<T> {
    T go() throws StorageException;
}
