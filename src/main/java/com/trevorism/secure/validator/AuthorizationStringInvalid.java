package com.trevorism.secure.validator;

public class AuthorizationStringInvalid extends RuntimeException {

    public AuthorizationStringInvalid(String message){
        super(message);
    }
}
