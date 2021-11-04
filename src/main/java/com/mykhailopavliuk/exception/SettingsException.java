package com.mykhailopavliuk.exception;

public class SettingsException extends RuntimeException {
    public SettingsException() {
    }

    public SettingsException(String message) {
        super(message);
    }
}
