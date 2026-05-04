package org.proyecto2.proyecto2.resources.habilidad;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.proyecto2.proyecto2.dtos.habilidad.HabilidadRequest;
import org.proyecto2.proyecto2.dtos.habilidad.HabilidadResponse;
import org.proyecto2.proyecto2.dtos.habilidad.HabilidadUpdate;
import org.proyecto2.proyecto2.exceptions.EntityAlreadyExistsException;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.habilidad.Habilidad;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.security.Secured;
import org.proyecto2.proyecto2.services.habilidad.HabilidadService;

import java.sql.SQLException;
import java.util.List;

@Path("/habilidad")
public class HabilidadResource {

    @POST
    @Secured
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createHabilidad(@Context ContainerRequestContext request, HabilidadRequest habilidadRequest) {
        try {
            String rol = (String) request.getProperty("rol");
            HabilidadService habilidadService = new HabilidadService();
            habilidadService.createHabilidad(habilidadRequest, EnumUsuario.valueOf(rol));
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Habilidad creada exitosamente\"}")
                    .type(MediaType.APPLICATION_JSON)
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
    public Response getAllHabilidad() {
        try {
            HabilidadService habilidadService = new HabilidadService();
            List<HabilidadResponse> habilidadResponse = habilidadService.getAllHabilidad()
                    .stream()
                    .map(HabilidadResponse::new)
                    .toList();
            return Response.ok(habilidadResponse).build();
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @GET
    @Path("/activa")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllHabilidadActivada() {
        try {
            HabilidadService habilidadService = new HabilidadService();
            List<HabilidadResponse> habilidadResponses = habilidadService.getAllHabilidadActivada()
                    .stream()
                    .map(HabilidadResponse::new)
                    .toList();
            return Response.ok(habilidadResponses).build();
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHabilidadByCoincidence(@PathParam("id") String id) {
        try {
            int habilidadId = Integer.parseInt(id);
            HabilidadService habilidadService = new HabilidadService();
            Habilidad habilidad = habilidadService.getHabilidadById(habilidadId);
            return Response.ok(new HabilidadResponse(habilidad)).build();
        } catch (NumberFormatException e) {
            return getHabilidad(id);
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    private Response getHabilidad(String nombre) {
        try {
            HabilidadService habilidadService = new HabilidadService();
            List<HabilidadResponse> habilidadResponses = habilidadService.getAllHabilidadByCoincidence(nombre)
                    .stream()
                    .map(HabilidadResponse::new)
                    .toList();
            return Response.ok(habilidadResponses).build();
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @PUT
    @Path("/actualizar")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateHabilidad(@Context ContainerRequestContext request, HabilidadUpdate habilidadUpdate) {
        try {
            String rol = (String) request.getProperty("rol");
            HabilidadService habilidadService = new HabilidadService();
            habilidadService.updateHabilidad(habilidadUpdate, EnumUsuario.valueOf(rol));
            return Response.ok("{\"message\": \"Habilidad actualizada exitosamente\"}").build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (EntityAlreadyExistsException e) {
            return errorEjecucion(e.getMessage(), 2);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @PUT
    @Secured
    @Path("/actualizar/estado/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateHabilidadEstado(@PathParam("id") int id, @Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            HabilidadService habilidadService = new HabilidadService();
            habilidadService.updateHabilidadEstado(id, EnumUsuario.valueOf(rol));
            return Response.ok("{\"message\": \"Estado de habilidad actualizado exitosamente\"}").build();
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