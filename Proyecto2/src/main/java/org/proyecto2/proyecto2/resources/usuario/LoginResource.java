package org.proyecto2.proyecto2.resources.usuario;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.proyecto2.proyecto2.dtos.usuario.login.LoginRequest;
import org.proyecto2.proyecto2.dtos.usuario.login.LoginResponse;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.services.usuario.login.LoginService;

@Path("/auth")
public class LoginResource {

    /*@POST
    @Secured
    @Path("/crear-reporte")
    public Response crearReporte(@Context ContainerRequestContext request) {
        String rolUsuario = (String) request.getProperty("rol");

        if (!"Administrador".equals(rolUsuario)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Solo los administradores pueden hacer esto.")
                    .build();
        }

        // Lógica para crear el reporte...
    }*/

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response login(LoginRequest login) {
        try {
            LoginService loginService = new LoginService();
            LoginResponse loginResponse = loginService.login(login);
            return Response.ok(loginResponse).build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (Exception e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    private Response errorEjecucion(String mensaje, int tipo) {
        switch (tipo) {
            // UserDataInvalidException
            case 1 -> {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"" + mensaje + "\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            // EntityAlreadyExistsException
            case 2 -> {
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"error\": \"" + mensaje + "\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            // SQLException
            case 3 -> {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"" + mensaje + "\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
        }
        return null;
    }

}