package org.proyecto2.proyecto2.resources.entrega;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.proyecto2.proyecto2.dtos.contrato.ContratoFinalizado;
import org.proyecto2.proyecto2.dtos.entrega.EntregaRechazada;
import org.proyecto2.proyecto2.dtos.entrega.EntregaRequest;
import org.proyecto2.proyecto2.dtos.entrega.EntregaResponse;
import org.proyecto2.proyecto2.dtos.entrega.EntregaUpdate;
import org.proyecto2.proyecto2.exceptions.EntityAlreadyExistsException;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.security.Secured;
import org.proyecto2.proyecto2.services.entrega.EntregaService;

import java.sql.SQLException;
import java.util.List;

@Path("/entrega")
public class EntregaResource {

    @POST
    @Path("/create")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response createEntrega(EntregaRequest entregaRequest, @Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            EntregaService entregaService = new EntregaService();
            entregaService.createEntrega(entregaRequest, EnumUsuario.valueOf(rol));
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"Entrega creada exitosamente\"}")
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
    @Path("{entregaId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEntregaById(@PathParam("entregaId") int entregaId) {
        try {
            EntregaService entregaService = new EntregaService();
            return Response.ok(new EntregaResponse(entregaService.getEntregaById(entregaId))).build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @GET
    @Path("/contrato/{contratoId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEntregasOfAContract(@PathParam("contratoId") int contratoId) {
        try {
            EntregaService entregaService = new EntregaService();
            List<EntregaResponse> entregas = entregaService.getAllEntregasOfAContract(contratoId)
                    .stream()
                    .map(EntregaResponse::new)
                    .toList();
            return Response.ok(entregas).build();
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @PUT
    @Path("/actualizar")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEntrega(EntregaUpdate entregaUpdate, @Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            EntregaService entregaService = new EntregaService();
            entregaService.updateEntrega(entregaUpdate, EnumUsuario.valueOf(rol));
            return Response.ok("{\"message\": \"Entrega actualizada exitosamente\"}").build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (EntityAlreadyExistsException e) {
            return errorEjecucion(e.getMessage(), 2);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @PUT
    @Path("/actualizar/rechazar")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response rechazarEntrega(EntregaRechazada entregaRechazada, @Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            EntregaService entregaService = new EntregaService();
            entregaService.rechazarEntrega(entregaRechazada, EnumUsuario.valueOf(rol));
            return Response.ok("{\"message\": \"Entrega rechazada exitosamente\"}").build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (EntityAlreadyExistsException e) {
            return errorEjecucion(e.getMessage(), 2);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @PUT
    @Path("/actualizar/aprobar/{entregaId}")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response aprobarEntrega(ContratoFinalizado contratoFinalizado, @Context ContainerRequestContext request, @PathParam("entregaId") int entregaId) {
        try {
            String rol = (String) request.getProperty("rol");
            int usuarioId = (int) request.getProperty("usuarioId");
            EntregaService entregaService = new EntregaService();
            entregaService.aprobarEntrega(entregaId, usuarioId, EnumUsuario.valueOf(rol), contratoFinalizado);
            return Response.ok("{\"message\": \"Entrega aprobada exitosamente\"}").build();
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