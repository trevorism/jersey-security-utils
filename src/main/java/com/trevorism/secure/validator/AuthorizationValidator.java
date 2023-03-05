package com.trevorism.secure.validator;

import com.trevorism.secure.Secure;

import javax.ws.rs.container.ContainerRequestContext;

public interface AuthorizationValidator {

    String getAuthorizationString(ContainerRequestContext requestContext);

    boolean validate(ContainerRequestContext requestContext, Secure secure);

    String getValidationErrorReason();
}
