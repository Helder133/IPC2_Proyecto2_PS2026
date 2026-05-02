package org.proyecto2.proyecto2;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Configures Jakarta RESTful Web Services for the application.
 * @author Juneau
 */
@ApplicationPath("api/v1")
public class JakartaRestConfiguration extends ResourceConfig {
    public JakartaRestConfiguration() {
        packages("org.proyecto2.proyecto2.resources", "org.proyecto2.proyecto2.security").register(MultiPartFeature.class);
    }
}