package org.proyecto2.proyecto2.resources.nueva_h_c;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.proyecto2.proyecto2.dtos.nueva_h_c.Nueva_h_cRequest;
import org.proyecto2.proyecto2.dtos.nueva_h_c.Nueva_h_cResponse;
import org.proyecto2.proyecto2.dtos.nueva_h_c.Nueva_h_cUpdate;
import org.proyecto2.proyecto2.exceptions.EntityAlreadyExistsException;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.security.Secured;
import org.proyecto2.proyecto2.services.nueva_h_cService.Nueva_h_cService;

import java.sql.SQLException;
import java.util.List;

@Path("/nueva-h-c")
public class Nueva_h_cResource {

    @POST
    @Secured
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNueva_h_c(Nueva_h_cRequest nueva_h_cRequest, @Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            int usuarioId = (int) request.getProperty("usuarioId");
            Nueva_h_cService nueva_h_cService = new Nueva_h_cService();
            nueva_h_cService.createNueva_h_c(nueva_h_cRequest, EnumUsuario.valueOf(rol), usuarioId);
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Solicitud de nueva " + nueva_h_cRequest.getTipo().name() + " creada exitosamente\"}")
                    .build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (EntityAlreadyExistsException e) {
            return errorEjecucion(e.getMessage(), 2);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getNueva_h_cById(@PathParam("id") int id) {
        try {
            Nueva_h_cService nueva_h_cService = new Nueva_h_cService();
            return Response.ok(new Nueva_h_cResponse(nueva_h_cService.getById(id))).build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    //Para el admin
    @GET
    @Path("/admin")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllNueva_h_c(@Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            Nueva_h_cService nueva_h_cService = new Nueva_h_cService();
            List<Nueva_h_cResponse> nueva_h_cResponseList = nueva_h_cService.getAll(EnumUsuario.valueOf(rol))
                    .stream()
                    .map(Nueva_h_cResponse::new)
                    .toList();
            return Response.ok(nueva_h_cResponseList).build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    //Para el freelancer o cliente
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllNueva_h_cByUsuarioId(@Context ContainerRequestContext request) {
        try {
            int usuarioId = (int) request.getProperty("usuarioId");
            Nueva_h_cService nueva_h_cService = new Nueva_h_cService();
            List<Nueva_h_cResponse> nueva_h_cResponseList = nueva_h_cService.getAllByUsuario(usuarioId)
                    .stream()
                    .map(Nueva_h_cResponse::new)
                    .toList();
            return Response.ok(nueva_h_cResponseList).build();
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @PUT
    @Path("/actualizar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateNueva_h_c(Nueva_h_cUpdate nueva_h_cUpdate) {
        try {
            Nueva_h_cService nueva_h_cService = new Nueva_h_cService();
            nueva_h_cService.updateNuevo(nueva_h_cUpdate);
            return Response.ok("{\"message\": \"Solicitud actualizada exitosamente\"}").build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (EntityAlreadyExistsException e) {
            return errorEjecucion(e.getMessage(), 2);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @PUT
    @Path("/rechazar/{id}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response rechazarSolicitud(@PathParam("id") int id, @Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            Nueva_h_cService nueva_h_cService = new Nueva_h_cService();
            nueva_h_cService.UpdateEstadoRechazar(EnumUsuario.valueOf(rol), id);
            return Response.ok("{\"message\": \"Solicitud rechazada exitosamente\"}").build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @PUT
    @Path("/aceptar/{id}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response aceptarSolicitud(@PathParam("id") int id, @Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            Nueva_h_cService nueva_h_cService = new Nueva_h_cService();
            nueva_h_cService.updateEstadoAceptada(EnumUsuario.valueOf(rol), id);
            return Response.ok("{\"message\": \"Solicitud aceptada exitosamente\"}").build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (EntityAlreadyExistsException e) {
            return errorEjecucion(e.getMessage(), 2);
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