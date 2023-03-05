package com.trevorism.secure.validator;

import com.trevorism.ClasspathBasedPropertiesProvider;
import com.trevorism.PropertiesProvider;
import com.trevorism.secure.Secure;

import javax.ws.rs.container.ContainerRequestContext;
import java.util.logging.Logger;

/**
 * Used for development so a token is not required. Only works for localhost; expects a secrets.properties file
 * to contain the following content:
 * localRole=<role>
 * where <role> could be  admin | system | user
 */
public class LocalhostTokenValidator implements AuthorizationValidator {

    private String validationErrorReason;
    private static final Logger log = Logger.getLogger(LocalhostTokenValidator.class.getName());

    @Override
    public String getAuthorizationString(ContainerRequestContext requestContext) {
        return "";
    }

    @Override
    public boolean validate(ContainerRequestContext requestContext, Secure secure) {
        try {
            if(!requestContext.getUriInfo().getBaseUri().getHost().equals("localhost") || secure == null){
                return false;
            }
            PropertiesProvider propertiesProvider = new ClasspathBasedPropertiesProvider();
            String role = propertiesProvider.getProperty("localRole");

            BearerTokenValidator.validateRole(secure, role);
            log.info("Validated localhost authentication");
            return true;
        } catch (Exception e) {
            validationErrorReason = e.getMessage();
        }
        return false;

    }

    @Override
    public String getValidationErrorReason() {
        return validationErrorReason;
    }
}
