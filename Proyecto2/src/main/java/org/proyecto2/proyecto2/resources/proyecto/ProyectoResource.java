package org.proyecto2.proyecto2.resources.proyecto;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.proyecto2.proyecto2.dtos.proyecto.ProyectoHabilidadRequest;
import org.proyecto2.proyecto2.dtos.proyecto.ProyectoRequest;
import org.proyecto2.proyecto2.dtos.proyecto.ProyectoResponse;
import org.proyecto2.proyecto2.dtos.proyecto.ProyectoUpdate;
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
            int usuarioId = (int) request.getProperty("usuarioId");
            ProyectoService proyectoService = new ProyectoService();
            proyectoService.createProyecto(proyectoRequest, EnumUsuario.valueOf(rol), usuarioId);
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

    @POST
    @Secured
    @Path("/{proyectoId}/habilidad")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response agregarHabilidadProyecto(@Context ContainerRequestContext request, @PathParam("proyectoId") int proyectoId, ProyectoHabilidadRequest proyectoHabilidadRequest) {
        try {
            String rol = (String) request.getProperty("rol");
            int usuarioId = (int) request.getProperty("usuarioId");
            ProyectoService proyectoService = new ProyectoService();
            proyectoService.insertProyectoHabilidad(proyectoHabilidadRequest, EnumUsuario.valueOf(rol), usuarioId, proyectoId);
            return Response.ok("{\"message\": \"Habilidad agregada al proyecto exitosamente\"}").build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    // EndPoint del freelancer
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProyectos(@Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            ProyectoService proyectoService = new ProyectoService();
            List<ProyectoResponse> proyectoResponses = proyectoService.getAllProyectoInAbiertoEstado(EnumUsuario.valueOf(rol))
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
    @Secured
    @Path("/presupuesto/{inicio}/{fin}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProyectoByPresupuesto(@PathParam("inicio") double inicio, @PathParam("fin") double fin, @Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            ProyectoService proyectoService = new ProyectoService();
            List<ProyectoResponse> proyectoResponses = proyectoService.getAllProyectoByPresupuesto(inicio, fin, EnumUsuario.valueOf(rol))
                    .stream()
                    .map(ProyectoResponse::new)
                    .toList();
            return Response.ok(proyectoResponses).build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    // EndPoint para el cliente
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
    @Path("/{proyectoId}/usuario")
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

    // endpoint para freelancer y cliente
    @GET
    @Secured
    @Path("/categoria/{categoriaId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProyectoByCategoria(@PathParam("categoriaId") int categoriaId, @Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            ProyectoService proyectoService = new ProyectoService();
            List<ProyectoResponse> proyectoResponses = proyectoService.getAllProyectoByCategoria(categoriaId, EnumUsuario.valueOf(rol))
                    .stream()
                    .map(ProyectoResponse::new)
                    .toList();
            return Response.ok(proyectoResponses).build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @GET
    @Secured
    @Path("/habilidad/{habilidadId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProyectoByHabilidad(@PathParam("habilidadId") int habilidadId, @Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            ProyectoService proyectoService = new ProyectoService();
            List<ProyectoResponse> proyectoResponses = proyectoService.getAllProyectoByHabilidad(habilidadId, EnumUsuario.valueOf(rol))
                    .stream()
                    .map(ProyectoResponse::new)
                    .toList();
            return Response.ok(proyectoResponses).build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @PUT
    @Secured
    @Path("/actualizar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarProyecto(@Context ContainerRequestContext request, ProyectoUpdate proyectoUpdate) {
        try {
            int usuarioId = (int) request.getProperty("usuarioId");
            String rol = (String) request.getProperty("rol");
            ProyectoService proyectoService = new ProyectoService();
            proyectoService.updateProyecto(proyectoUpdate, EnumUsuario.valueOf(rol), usuarioId);
            return Response.ok("{\"message\": \"Proyecto actualizado exitosamente\"}").build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @PUT
    @Secured
    @Path("/actualizar/cancelar/{proyectoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarProyectoACancelado(@PathParam("proyectoId") int proyectoId, @Context ContainerRequestContext request) {
        try {
            int usuarioId = (int) request.getProperty("usuarioId");
            String rol = (String) request.getProperty("rol");
            ProyectoService proyectoService = new ProyectoService();
            proyectoService.updateProyectoEstadoCancelacion(proyectoId, usuarioId, EnumUsuario.valueOf(rol));
            return Response.ok("{\"message\": \"Proyecto cancelado exitosamente\"}").build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    //Cuando el cliente ingresa el listado de las propuestas de un proyecto, el proyecto pasa ha estado en revisión
    @PUT
    @Secured
    @Path("/actualizar/en-reserva/{proyectoId}")
    public Response actualizarProyectoEnReserva(@PathParam("proyectoId") int proyectoId, @Context ContainerRequestContext request) {
        try {
            int usuarioId = (int) request.getProperty("usuarioId");
            String rol = (String) request.getProperty("rol");
            ProyectoService proyectoService = new ProyectoService();
            proyectoService.updateProyectoEstadoEnRevision(proyectoId, usuarioId, EnumUsuario.valueOf(rol));
            return Response.ok().build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @DELETE
    @Secured
    @Path("/eliminar/{proyectoId}/habilidad/{habilidadId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarProyecto(@PathParam("proyectoId") int proyectoId, @PathParam("habilidadId") int habilidadId, @Context ContainerRequestContext request) {
        try {
            int usuarioId = (int) request.getProperty("usuarioId");
            String rol = (String) request.getProperty("rol");
            ProyectoService proyectoService = new ProyectoService();
            proyectoService.deleteProyectoUsuario(usuarioId, proyectoId, habilidadId, EnumUsuario.valueOf(rol));
            return Response.ok("{\"message\": \"Habilidad eliminado del proyecto exitosamente\"}").build();
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