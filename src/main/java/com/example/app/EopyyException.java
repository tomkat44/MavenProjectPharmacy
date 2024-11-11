package com.example.app;


@SuppressWarnings("serial")
public class EopyyException extends RuntimeException {

    public EopyyException() { }
    
    public EopyyException(String message) {
        super(message);
    }

    public EopyyException(String message, Throwable cause) {
        super(message, cause);
    }

    public EopyyException(Throwable cause) {
        super(cause);
    }
}
 