package org.proyecto2.proyecto2.resources.proyecto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.proyecto2.proyecto2.dtos.proyecto.ProyectoRequest;
import org.proyecto2.proyecto2.dtos.proyecto.ProyectoResponse;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.proyecto.Proyecto;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.security.Secured;
import org.proyecto2.proyecto2.services.proyecto.ProyectoService;

import java.sql.SQLException;
import java.util.List;

@Path("/proyecto")
public class ProyectoResource {

    @POST
    @Secured
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProyecto(ProyectoRequest proyectoRequest, @Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            ProyectoService proyectoService = new ProyectoService();
            proyectoService.createProyecto(proyectoRequest, EnumUsuario.valueOf(rol));
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Proyecto creada exitosamente\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProyectos() {
        try {
            ProyectoService proyectoService = new ProyectoService();
            List<ProyectoResponse> proyectoResponses = proyectoService.getAll()
                    .stream()
                    .map(ProyectoResponse::new)
                    .toList();
            return Response.ok(proyectoResponses).build();
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @GET
    @Path("/{proyectoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProyectoById(@PathParam("proyectoId") int proyectoId) {
        try {
            ProyectoService proyectoService = new ProyectoService();
            Proyecto proyecto = proyectoService.getById(proyectoId);
            return Response.ok(new ProyectoResponse(proyecto)).build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @GET
    @Path("/usuario")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsuarioProyecto(@Context ContainerRequestContext request) {
        try {
            int usuarioId = (int) request.getProperty("usuarioId");
            ProyectoService proyectoService = new ProyectoService();
            List<ProyectoResponse> proyectoResponses = proyectoService.getAllUsuarioProyecto(usuarioId)
                    .stream()
                    .map(ProyectoResponse::new)
                    .toList();
            return Response.ok(proyectoResponses).build();
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @GET
    @Secured
    @Path("/usuario/{titulo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsuarioProyectoByCoincidence(@Context ContainerRequestContext request, @PathParam("titulo") String titulo) {
        try {
            int usuarioId = (int) request.getProperty("usuarioId");
            ProyectoService proyectoService = new ProyectoService();
            List<ProyectoResponse> proyectoResponses = proyectoService.getAllUsuarioProyectoByCoincidence(usuarioId, titulo)
                    .stream()
                    .map(ProyectoResponse::new)
                    .toList();
            return Response.ok(proyectoResponses).build();
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @GET
    @Secured
    @Path("/{proyectoId}/usuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarioProyectoById(@Context ContainerRequestContext request, @PathParam("proyectoId") int proyectoId) {
        try {
            int usuarioId = (int) request.getProperty("usuarioId");
            ProyectoService proyectoService = new ProyectoService();
            Proyecto proyecto = proyectoService.getUsuarioProyectoById(usuarioId, proyectoId);
            return Response.ok(new ProyectoResponse(proyecto)).build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
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