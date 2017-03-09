package com.ceg.exceptions;

public class EmptyExamException extends Exception {
    
    public EmptyExamException() {
    }
    public EmptyExamException(String message) {
        super(message);
    }
    public EmptyExamException(Throwable cause) {
        super(cause);
    }
    public EmptyExamException(String message, Throwable cause) {
        super(message, cause);  
    }
}
