package org.proyecto2.proyecto2.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.proyecto2.proyecto2.utils.JwtUtil;

import java.io.IOException;

@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            rechazarPeticion(requestContext, "Acceso denegado. Token no proporcionado.");
            return;
        }
        String token = authHeader.substring(7);
        try {
            Claims claims = JwtUtil.validarYObtenerUsername(token);
            String username = claims.getSubject();
            String rol = claims.get("rol", String.class);
            Object idObj = claims.get("usuarioId");
            int usuarioId = ((Number) idObj).intValue();
            requestContext.setProperty("username", username);
            requestContext.setProperty("rol", rol);
            requestContext.setProperty("usuarioId", usuarioId);
        } catch (JwtException e) {
            rechazarPeticion(requestContext, "Token inválido o expirado. Inicie sesión nuevamente.");
        }
    }

    private void rechazarPeticion(ContainerRequestContext requestContext, String mensajeError) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\":\"" + mensajeError + "\"}")
                        .header(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8")
                        .build()
        );
    }
}
