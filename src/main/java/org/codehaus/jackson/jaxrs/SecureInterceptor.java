package org.codehaus.jackson.jaxrs;

import com.trevorism.secure.Secure;
import com.trevorism.secure.SecureRequestFilter;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

/**
 * @author tbrooks
 */
@Provider
public class SecureInterceptor implements DynamicFeature{
    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        if (resourceInfo.getResourceMethod().getAnnotation(Secure.class) == null)
            return;

        context.register(SecureRequestFilter.class);
    }
}
