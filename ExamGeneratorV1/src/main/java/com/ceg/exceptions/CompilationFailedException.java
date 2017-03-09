package com.ceg.exceptions;

/**
 *
 * @author Martyna
 */
public class CompilationFailedException extends Exception {
    
    public CompilationFailedException() {
    }

    public CompilationFailedException(String message) {
        super(message);
    }

    public CompilationFailedException(Throwable cause) {
        super(cause);
    }

    public CompilationFailedException(String message, Throwable cause) {
        super(message, cause);  
    }
}

