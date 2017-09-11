package com.aiyolo;

public class AuthenticateError extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AuthenticateError(final String message) {
        super(message);
    }

}
