package com.trevorism.secure.validator;

import java.util.ArrayList;
import java.util.List;

public class Validators {

    public static final AuthorizationValidator bearerTokenValidator = new BearerTokenValidator();
    public static final AuthorizationValidator cookieValidator = new CookieValidator();
    public static final AuthorizationValidator localhostTokenValidator = new LocalhostTokenValidator();

    public static List<AuthorizationValidator> allValidators = new ArrayList<>();

    static {
        addValidator(bearerTokenValidator);
        addValidator(cookieValidator);
        addValidator(localhostTokenValidator);
    }

    public static void removeValidator(AuthorizationValidator authorizationValidator) {
        allValidators.remove(authorizationValidator);
    }

    public static void addValidator(AuthorizationValidator authorizationValidator) {
        allValidators.add(authorizationValidator);
    }


}
