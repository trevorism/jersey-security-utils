package com.trevorism.secure.validator;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;

public class CookieValidator extends BearerTokenValidator implements AuthorizationValidator {

    public static final String SESSION_COOKIE_NAME = "session";

    @Override
    public String getAuthorizationString(ContainerRequestContext requestContext) {
        Cookie cookie = requestContext.getCookies().get(SESSION_COOKIE_NAME);
        if (cookie == null) {
            throw new AuthorizationStringInvalid("Unable to read cookie '" + SESSION_COOKIE_NAME + "'");
        }
        return cookie.getValue();
    }

}
