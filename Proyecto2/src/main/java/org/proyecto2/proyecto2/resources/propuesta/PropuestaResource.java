package org.proyecto2.proyecto2.resources.propuesta;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.proyecto2.proyecto2.dtos.propuesta.*;
import org.proyecto2.proyecto2.exceptions.EntityAlreadyExistsException;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.security.Secured;
import org.proyecto2.proyecto2.services.propuesta.PropuestaService;

import java.sql.SQLException;
import java.util.List;

@Path("/propuesta")
public class PropuestaResource {

    @POST
    @Path("/create")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPropuesta(PropuestaRequest propuestaRequest, @Context ContainerRequestContext request) {
        try {
            int usuarioId = (int) request.getProperty("usuarioId");
            String rol = (String) request.getProperty("rol");
            PropuestaService propuestaService = new PropuestaService();
            propuestaService.insertPropuesta(propuestaRequest, usuarioId, EnumUsuario.valueOf(rol));
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Propuesta creada exitosamente\"}")
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
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPropuestaById(@PathParam("id") int id) {
        try {
            PropuestaService propuestaService = new PropuestaService();
            return Response.ok(new PropuestaResponse(propuestaService.getPropuestaById(id))).build();
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @GET
    @Path("/freelancer/{proyectoId}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPropuestaFromAFreelancer(@PathParam("proyectoId") int proyectoId, @Context ContainerRequestContext request) {
        try {
            int usuarioId = (int) request.getProperty("usuarioId");
            PropuestaService propuestaService = new PropuestaService();
            List<PropuestaResponse> propuestaResponses = propuestaService.getAllPropuestaFromAFreelancer(usuarioId, proyectoId)
                    .stream()
                    .map(PropuestaResponse::new)
                    .toList();
            return Response.ok(propuestaResponses).build();
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @GET
    @Path("/cliente/{proyectoId}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPropuestaForAProyecto(@PathParam("proyectoId") int proyectoId, @Context ContainerRequestContext request) {
        try {
            int usuarioId = (int) request.getProperty("usuarioId");
            String rol = (String) request.getProperty("rol");
            PropuestaService propuestaService = new PropuestaService();
            List<PropuestaDetalleResponse> propuestaResponses = propuestaService.getAllPropuestaForAProyecto(proyectoId, usuarioId, EnumUsuario.valueOf(rol))
                    .stream()
                    .map(PropuestaDetalleResponse::new)
                    .toList();
            return Response.ok(propuestaResponses).build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @GET
    @Path("/usuario/historial/{estado}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHistorialFreelancer(@Context ContainerRequestContext request, @PathParam("estado") String estado) {
        try {
            int usuarioId = (int) request.getProperty("usuarioId");
            String rol = (String) request.getProperty("rol");
            PropuestaService propuestaService = new PropuestaService();

            String filtro = (estado == null || estado.trim().isEmpty()) ? "TODAS" : estado;

            List<PropuestaHistorialResponse> historial = propuestaService.getHistorialPropuestasFreelancer(usuarioId, filtro, EnumUsuario.valueOf(rol))
                    .stream()
                    .map(PropuestaHistorialResponse::new)
                    .toList();;
            return Response.ok(historial).build();
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @PUT
    @Path("/actualizar")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarPropuesta(PropuestaUpdate propuestaUpdate, @Context ContainerRequestContext request) {
        try {
            int usuarioId = (int) request.getProperty("usuarioId");
            String rol = (String) request.getProperty("rol");
            PropuestaService propuestaService = new PropuestaService();
            propuestaService.updatePropuesta(propuestaUpdate, usuarioId, EnumUsuario.valueOf(rol));
            return Response.ok("{\"message\": \"Propuesta actualizada exitosamente\"}").build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @PUT
    @Secured
    @Path("/aceptar/{propuestaId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response aceptarPropuesta(@PathParam("propuestaId") int propuestaId, @Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            int usuarioId = (int) request.getProperty("usuarioId");
            PropuestaService propuestaService = new PropuestaService();
            propuestaService.updatePropuestaEstadoAceptar(propuestaId, EnumUsuario.valueOf(rol),  usuarioId);
            return Response.ok("{\"message\": \"Propuesta aceptada y contrato creado exitosamente\"}").build();
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
    @Path("/retirar/{propuestaId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retirarPropuesta(@PathParam("propuestaId") int propuestaId, @Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            int usuarioId = (int) request.getProperty("usuarioId");
            PropuestaService propuestaService = new PropuestaService();
            propuestaService.updatePropuestaEstadoRetirado(propuestaId, usuarioId, EnumUsuario.valueOf(rol));
            return Response.ok("{\"message\": \"Propuesta retirada exitosamente\"}").build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @PUT
    @Secured
    @Path("/rechazar/{propuestaId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response rechazarPropuesta(@PathParam("propuestaId") int propuestaId, @Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            int usuarioId = (int) request.getProperty("usuarioId");
            PropuestaService propuestaService = new PropuestaService();
            propuestaService.updatePropuestaEstadoRechazado(propuestaId, usuarioId, EnumUsuario.valueOf(rol));
            return Response.ok("{\"message\": \"Propuesta rechazada exitosamente\"}").build();
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