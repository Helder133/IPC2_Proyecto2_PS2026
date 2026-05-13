package org.proyecto2.proyecto2.resources.transaccionPlataforma;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.proyecto2.proyecto2.dtos.usuario.cartera.TransaccionPlataformaResponse;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.security.Secured;
import org.proyecto2.proyecto2.services.usuario.cartera.TransaccionPlataformaService;

import java.sql.SQLException;
import java.util.List;

@Path("/transaccion-plataforma")
public class TransaccionPlataformaResource {

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTransaccionPlataformas(@Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            TransaccionPlataformaService  transaccionPlataformaService = new TransaccionPlataformaService();
            List<TransaccionPlataformaResponse> transaccionPlataformaResponseList = transaccionPlataformaService.getAllTransaccionPlataformas(EnumUsuario.valueOf(rol))
                    .stream()
                    .map(TransaccionPlataformaResponse::new)
                    .toList();
            return Response.ok(transaccionPlataformaResponseList).build();
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