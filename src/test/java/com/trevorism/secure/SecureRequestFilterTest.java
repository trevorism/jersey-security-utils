package com.trevorism.secure;

import com.trevorism.secure.validator.AuthorizationStringInvalid;
import com.trevorism.secure.validator.AuthorizationValidator;
import com.trevorism.secure.validator.Validators;
import org.codehaus.jackson.jaxrs.TestBearerTokenContainerRequestContext;
import org.codehaus.jackson.jaxrs.TestContainerRequestContext;
import org.junit.Test;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author tbrooks
 */
public class SecureRequestFilterTest {

    @Test
    public void testWithCorrectInjectedPassword() throws IOException {
        TestContainerRequestContext context = new TestContainerRequestContext("test");
        AuthorizationValidator allowTestValidator = new AllowTestValidator();
        Validators.addValidator(allowTestValidator);
        SecureRequestFilter filter = new SecureRequestFilter();
        filter.filter(context);
        assertTrue(!context.isAborted());
        Validators.removeValidator(allowTestValidator);
    }

    @Test
    public void testWithInvalidPassword() throws IOException {
        TestContainerRequestContext context = new TestContainerRequestContext("asdfsadf");
        SecureRequestFilter filter = new SecureRequestFilter();
        filter.filter(context);

        assertTrue(context.isAborted());
    }

    @Test
    public void testWithBearerToken() throws IOException {
        TestContainerRequestContext context = new TestBearerTokenContainerRequestContext();
        SecureRequestFilter filter = new SecureRequestFilter();
        filter.resourceInfo = new ResourceInfo() {
            @Override
            public Method getResourceMethod() {
                return SecuredMethod.class.getMethods()[0];
            }

            @Override
            public Class<?> getResourceClass() {
                return SecuredMethod.class;
            }
        };
        filter.filter(context);

        assertTrue(context.isAborted());
    }

    public class SecuredMethod {
        public void doSomething() {

        }
    }

    public class AllowTestValidator implements AuthorizationValidator{

        @Override
        public String getAuthorizationString(ContainerRequestContext requestContext) {
            List<String> list = requestContext.getHeaders().get(HttpHeaders.AUTHORIZATION);
            if (list == null || list.isEmpty()) {
                throw new AuthorizationStringInvalid("Unable to find authorization data");
            }
            return list.get(0);
        }

        @Override
        public boolean validate(ContainerRequestContext requestContext, Secure secure) {
            return getAuthorizationString(requestContext).equals("test");
        }

        @Override
        public String getValidationErrorReason() {
            return null;
        }
    }
}