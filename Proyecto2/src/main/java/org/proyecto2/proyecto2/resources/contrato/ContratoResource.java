package org.proyecto2.proyecto2.resources.contrato;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.proyecto2.proyecto2.dtos.contrato.ContratoCancelado;
import org.proyecto2.proyecto2.dtos.contrato.ContratoResponse;
import org.proyecto2.proyecto2.exceptions.EntityAlreadyExistsException;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.security.Secured;
import org.proyecto2.proyecto2.services.contrato.ContratoService;

import java.sql.SQLException;
import java.util.List;

@Path("/contrato")
public class ContratoResource {

    @GET
    @Path("/cliente")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllContractsFromAClient(@Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            int usuarioId = (int) request.getProperty("usuarioId");
            ContratoService contratoService = new ContratoService();
            List<ContratoResponse> contratoResponse = contratoService.getAllContractsFromACliente(usuarioId, EnumUsuario.valueOf(rol))
                    .stream()
                    .map(ContratoResponse::new)
                    .toList();
            return Response.ok(contratoResponse).build();
        } catch (Exception e) {
            return errorEjecucion(e.getMessage(), 1);
        }
    }

    @GET
    @Path("/freelancer")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllContractsFromAFreelancer(@Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            int usuarioId = (int) request.getProperty("usuarioId");
            ContratoService contratoService = new ContratoService();
            List<ContratoResponse> contratoResponse = contratoService.getAllContractsFromAFreelancer(usuarioId, EnumUsuario.valueOf(rol))
                    .stream()
                    .map(ContratoResponse::new)
                    .toList();
            return Response.ok(contratoResponse).build();
        } catch (Exception e) {
            return errorEjecucion(e.getMessage(), 1);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getContratoById(@PathParam("id") int id) {
        try {
            ContratoService contratoService = new ContratoService();
            return Response.ok(new ContratoResponse(contratoService.getContractById(id))).build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @PUT
    @Path("/cancelar")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response cancelarContrato(ContratoCancelado contratoCancelado, @Context ContainerRequestContext request) {
        try {
            int usuarioId = (int) request.getProperty("usuarioId");
            String rol = (String) request.getProperty("rol");
            ContratoService contratoService = new ContratoService();
            contratoService.updateCanceladoContrato(contratoCancelado, EnumUsuario.valueOf(rol), usuarioId);
            return Response.ok("{\"message\": \"Contrato cancelado exitosamente\"}").build();
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