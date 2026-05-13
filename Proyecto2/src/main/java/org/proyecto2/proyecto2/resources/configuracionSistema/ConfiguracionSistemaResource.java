package org.proyecto2.proyecto2.resources.configuracionSistema;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.proyecto2.proyecto2.dtos.configuracionSistema.ConfiguracionSistemaRequest;
import org.proyecto2.proyecto2.dtos.configuracionSistema.ConfiguracionSistemaResponse;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.security.Secured;
import org.proyecto2.proyecto2.services.configuracionSistema.ConfiguracionSistemaService;

import java.sql.SQLException;
import java.util.List;

@Path("/configuracion-sistema")
public class ConfiguracionSistemaResource {

    //crear nueva configuracion, y esa se usa por defecto
    @POST
    @Path("/create")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(ConfiguracionSistemaRequest configuracionSistemaRequest, @Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            ConfiguracionSistemaService configuracionSistemaService = new ConfiguracionSistemaService();
            configuracionSistemaService.insert(configuracionSistemaRequest, EnumUsuario.valueOf(rol));
            return Response.status(Response.Status.CREATED).entity("{\"message\": \"Configuración creada exitosamente\"}").build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    //trae la ultima configuracion del sistema, si no existe ninguna, devuelve un error
    @GET
    @Path("/ultimo")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUltimaConfiguracionSistema(@Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            ConfiguracionSistemaService configuracionSistemaService = new ConfiguracionSistemaService();
            return Response.ok(new ConfiguracionSistemaResponse(configuracionSistemaService.getUltimaConfiguracionSistema(EnumUsuario.valueOf(rol)))).build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    //el historial de todas las configuraciones del sistema
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllConfiguracionSistemas(@Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            ConfiguracionSistemaService configuracionSistemaService = new ConfiguracionSistemaService();
            List<ConfiguracionSistemaResponse> configuracionSistemaResponses = configuracionSistemaService.getAllConfiguracionSistema(EnumUsuario.valueOf(rol))
                    .stream()
                    .map(ConfiguracionSistemaResponse::new)
                    .toList();
            return Response.ok(configuracionSistemaResponses).build();
        } catch (SQLException e) {
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
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"error\": \"" + mensaje + "\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            default -> {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"" + mensaje + "\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
        }
    }
}