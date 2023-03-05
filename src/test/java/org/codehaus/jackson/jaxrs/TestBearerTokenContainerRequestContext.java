package org.codehaus.jackson.jaxrs;

public class TestBearerTokenContainerRequestContext extends TestContainerRequestContext{

    public TestBearerTokenContainerRequestContext(String password) {
        super(password);
    }

    public TestBearerTokenContainerRequestContext() {
        super();
        map.add("Authorization", "Bearer eyxx");
    }

}
