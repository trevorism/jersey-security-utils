package com.trevorism.secure.validator;

public class AuthorizationNotValid extends RuntimeException {

    public AuthorizationNotValid(String message) {
        super(message);
    }
}
