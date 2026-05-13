package org.proyecto2.proyecto2.resources.categoria;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.proyecto2.proyecto2.dtos.categoria.CategoriaRequest;
import org.proyecto2.proyecto2.dtos.categoria.CategoriaResponse;
import org.proyecto2.proyecto2.dtos.categoria.CategoriaUpdate;
import org.proyecto2.proyecto2.exceptions.EntityAlreadyExistsException;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.security.Secured;
import org.proyecto2.proyecto2.services.categoria.CategoriaService;

import java.sql.SQLException;
import java.util.List;

@Path("/categoria")
public class CategoriaResource {

    @POST
    @Path("/create")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCategoria(CategoriaRequest categoriaRequest, @Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            CategoriaService categoriaService = new CategoriaService();
            categoriaService.createCategoria(categoriaRequest, EnumUsuario.valueOf(rol));
            return Response.status(Response.Status.CREATED).entity("{\"mensaje\": \"Categoria creado exitosamente\"}").build();
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
    public Response getAllCategorias() {
        try {
            CategoriaService categoriaService = new CategoriaService();
            List<CategoriaResponse> categorias = categoriaService.getAll()
                    .stream()
                    .map(CategoriaResponse::new)
                    .toList();
            return Response.ok(categorias).build();
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoriaById(@PathParam("id") int categoriaId) {
        try {
            CategoriaService categoriaService = new CategoriaService();
            return Response.ok(new CategoriaResponse(categoriaService.getById(categoriaId))).build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @GET
    @Path("/coincidence/{nombre}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoriaByCoincidence(@PathParam("nombre")  String nombre) {
        try {
            CategoriaService categoriaService = new CategoriaService();
            List<CategoriaResponse> categoriaResponses = categoriaService.getCategoriaByCoincidence(nombre)
                    .stream()
                    .map(CategoriaResponse::new)
                    .toList();
            return Response.ok(categoriaResponses).build();
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @GET
    @Path("/activada")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivadaCategoria() {
        try {
            CategoriaService categoriaService = new CategoriaService();
            List<CategoriaResponse> categoriaResponses = categoriaService.getAllCategoriaActivadas()
                    .stream()
                    .map(CategoriaResponse::new)
                    .toList();
            return Response.ok(categoriaResponses).build();
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @PUT
    @Path("/actualizar")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarCategoria(CategoriaUpdate categoriaUpdate, @Context ContainerRequestContext request) {
        try {
            String rol = (String) request.getProperty("rol");
            CategoriaService categoriaService = new CategoriaService();
            categoriaService.update(categoriaUpdate, EnumUsuario.valueOf(rol));
            return Response.ok("{\"mensaje\": \"Categoria actualizado exitosamente\"}").build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (EntityAlreadyExistsException e) {
            return errorEjecucion(e.getMessage(), 2);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @PUT
    @Path("/actualizar/estado/{id}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarCategoriaEstado(@PathParam("id")int categoriaId, @Context ContainerRequestContext request) {
        try {
            String rol = (String)  request.getProperty("rol");
            CategoriaService categoriaService = new CategoriaService();
            categoriaService.updateEstado(categoriaId, EnumUsuario.valueOf(rol));
            return Response.ok("{\"mensaje\": \"Estado de categoria actualizado exitosamente\"}").build();
        }  catch (UserDataInvalidException e) {
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