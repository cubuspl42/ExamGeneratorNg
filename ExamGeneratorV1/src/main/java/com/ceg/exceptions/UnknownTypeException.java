package com.ceg.exceptions;

public class UnknownTypeException extends Exception {
    public UnknownTypeException() {
    }
    public UnknownTypeException(String message) {
        super(message);
    }
    public UnknownTypeException(Throwable cause) {
        super(cause);
    }
    public UnknownTypeException(String message, Throwable cause) {
        super(message, cause);  
    }
}
