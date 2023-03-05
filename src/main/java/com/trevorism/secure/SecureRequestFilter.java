package com.trevorism.secure;

import com.trevorism.secure.validator.AuthorizationValidator;
import com.trevorism.secure.validator.Validators;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author tbrooks
 */
public class SecureRequestFilter implements ContainerRequestFilter {

    private static final Logger log = Logger.getLogger(SecureRequestFilter.class.getName());

    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Secure secure = null;
        if (resourceInfo != null) {
            secure = resourceInfo.getResourceMethod().getAnnotation(Secure.class);
        }

        for (AuthorizationValidator validator : Validators.allValidators) {
            boolean result = validator.validate(requestContext, secure);
            if (!result) {
                log.finer(validator.getValidationErrorReason());
            } else {
                return;
            }
        }

        log.fine("Authentication for secure endpoint failed. All validators show the request did not meet the authentication and authorization requirement.");
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    }

}
