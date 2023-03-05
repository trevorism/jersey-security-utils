package org.codehaus.jackson.jaxrs;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

/**
 * @author tbrooks
 */
public class TestContainerRequestContext implements ContainerRequestContext {
    protected final MultivaluedMap<String, String> map;
    private boolean aborted = false;

    public TestContainerRequestContext() {
        this.map = new MultivaluedHashMap<>();
    }

    public TestContainerRequestContext(String password) {
        this.map = new MultivaluedHashMap<>();
        map.add("Authorization", password);
    }

    @Override
    public MultivaluedMap<String, String> getHeaders() {
        return map;
    }

    @Override
    public Object getProperty(String name) {
        return null;
    }

    @Override
    public Collection<String> getPropertyNames() {
        return null;
    }

    @Override
    public void setProperty(String name, Object object) {

    }

    @Override
    public void removeProperty(String name) {

    }

    @Override
    public UriInfo getUriInfo() {
        return null;
    }

    @Override
    public void setRequestUri(URI requestUri) {

    }

    @Override
    public void setRequestUri(URI baseUri, URI requestUri) {

    }

    @Override
    public Request getRequest() {
        return null;
    }

    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public void setMethod(String method) {

    }

    @Override
    public String getHeaderString(String name) {
        return null;
    }

    @Override
    public Date getDate() {
        return null;
    }

    @Override
    public Locale getLanguage() {
        return null;
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public MediaType getMediaType() {
        return null;
    }

    @Override
    public List<MediaType> getAcceptableMediaTypes() {
        return null;
    }

    @Override
    public List<Locale> getAcceptableLanguages() {
        return null;
    }

    @Override
    public Map<String, Cookie> getCookies() {
        return null;
    }

    @Override
    public boolean hasEntity() {
        return false;
    }

    @Override
    public InputStream getEntityStream() {
        return null;
    }

    @Override
    public void setEntityStream(InputStream input) {

    }

    @Override
    public SecurityContext getSecurityContext() {
        return null;
    }

    @Override
    public void setSecurityContext(SecurityContext context) {

    }

    @Override
    public void abortWith(Response response) {
        setAborted(true);
    }

    public boolean isAborted() {
        return aborted;
    }

    public void setAborted(boolean aborted) {
        this.aborted = aborted;
    }
}
