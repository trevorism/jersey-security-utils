package com.trevorism.secure.validator;

import com.trevorism.ClaimProperties;
import com.trevorism.ClaimsProvider;
import com.trevorism.ClasspathBasedPropertiesProvider;
import com.trevorism.PropertiesProvider;
import com.trevorism.secure.*;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import java.util.List;
import java.util.logging.Logger;

public class BearerTokenValidator implements AuthorizationValidator {

    public static final String BEARER_PREFIX = "bearer ";
    private static final Logger log = Logger.getLogger(BearerTokenValidator.class.getName());
    private final PropertiesProvider propertiesProvider;

    private String reason;
    private ClaimProperties claimProperties = new ClaimProperties();

    public BearerTokenValidator() {
        this(new ClasspathBasedPropertiesProvider());
    }

    public BearerTokenValidator(PropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    @Override
    public String getAuthorizationString(ContainerRequestContext requestContext) {
        List<String> list = requestContext.getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (list == null || list.isEmpty()) {
            throw new AuthorizationStringInvalid("Unable to find authorization data");
        }
        String authorizationHeader = list.get(0);
        if (!authorizationHeader.toLowerCase().startsWith(BEARER_PREFIX)) {
            throw new AuthorizationStringInvalid("Authorization header does not begin with 'bearer'");
        }

        return authorizationHeader.substring(BEARER_PREFIX.length());
    }

    @Override
    public boolean validate(ContainerRequestContext requestContext, Secure secure) {
        try {
            claimProperties = ClaimsProvider.getClaims(getAuthorizationString(requestContext), propertiesProvider.getProperty("signingKey"));
            validateClaims(secure);
            log.info("Validated authentication and authorization for " + claimProperties.getSubject());
            return true;
        } catch (Exception e) {
            reason = e.getMessage();
        }
        return false;
    }

    void validateClaims(Secure secure) {
        validateInputs(secure);
        validateIssuer();
        validateRole(secure, claimProperties.getRole());

        if (secure.authorizeAudience()) {
            validateAudience();
        }
    }

    private void validateAudience() {
        String claimAudience = claimProperties.getAudience();
        String thisAudience = propertiesProvider.getProperty("clientId");

        if (claimAudience == null || !claimAudience.equals(thisAudience)) {
            throw new AuthorizationNotValid("Audience in claim does not match clientId in secrets.properties");
        }
    }

    public static void validateRole(Secure secure, String claimRole) {
        String role = secure.value();
        if (claimRole == null) {
            throw new AuthorizationNotValid("Unable to parse claim role");
        }
        if (role.isEmpty()) {
            return;
        }
        if (claimRole.equals(Roles.INTERNAL) && !secure.allowInternal()) {
            throw new AuthorizationNotValid("Insufficient access");
        }
        if (role.equals(Roles.ADMIN)) {
            if (!claimRole.equals(Roles.ADMIN)) {
                throw new AuthorizationNotValid("Insufficient access");
            }
        }
        if (role.equals(Roles.SYSTEM)) {
            if (claimRole.equals(Roles.USER)) {
                throw new AuthorizationNotValid("Insufficient access");
            }
        }
    }

    private void validateIssuer() {
        if (!"https://trevorism.com".equals(claimProperties.getIssuer())) {
            throw new AuthorizationNotValid("Unexpected issuer: " + claimProperties.getIssuer());
        }
    }

    private void validateInputs(Secure secure) {
        if (claimProperties == null) {
            throw new AuthorizationNotValid("Unable to parse claim");
        }
        if (secure == null) {
            throw new AuthorizationNotValid("Unable to validate against a method without the @Secure annotation");
        }
    }

    @Override
    public String getValidationErrorReason() {
        return reason;
    }

    void setClaimProperties(ClaimProperties claimProperties) {
        this.claimProperties = claimProperties;
    }
}
