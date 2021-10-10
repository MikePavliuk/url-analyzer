package com.mykhailopavliuk.exception;

public class DatabaseOperationException extends RuntimeException {
    public DatabaseOperationException() {    }

    public DatabaseOperationException(String message) {
        super(message);
    }
}
