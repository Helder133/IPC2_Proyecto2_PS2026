package org.proyecto2.proyecto2.resources.usuario;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.proyecto2.proyecto2.dtos.usuario.UsuarioRequest;
import org.proyecto2.proyecto2.dtos.usuario.UsuarioResponse;
import org.proyecto2.proyecto2.dtos.usuario.UsuarioUpdate;
import org.proyecto2.proyecto2.dtos.usuario.cliente.ClienteRequest;
import org.proyecto2.proyecto2.dtos.usuario.freelancer.FreelancerRequest;
import org.proyecto2.proyecto2.exceptions.EntityAlreadyExistsException;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.models.usuario.Usuario;
import org.proyecto2.proyecto2.security.Secured;
import org.proyecto2.proyecto2.services.usuario.UsuarioService;

import java.sql.SQLException;
import java.util.List;

@Path("/usuario")
public class UsuarioResource {
    //Registro publico, cualquiera puede registrarse, pero solo con rol de cliente o freelancer, el admin solo puede ser creado por otro admin
    @POST
    @Path("/cliente-freelancer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUsuarioClienteFreelancer(UsuarioRequest usuarioRequest) {
        try {
            UsuarioService usuarioService = new UsuarioService();
            Usuario usuario = usuarioService.createUsuarioClienteFreelancer(usuarioRequest);
            return Response.status(Response.Status.CREATED)
                    .entity(new UsuarioResponse(usuario))
                    .build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (EntityAlreadyExistsException e) {
            return errorEjecucion(e.getMessage(), 2);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    //Registro para crear otros administradores, solo un admin puede crear otro admin, el admin debe estar autenticado para poder crear otro admin
    @POST
    @Secured
    @Path("/admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUsuarioAdmin(@Context ContainerRequestContext request, UsuarioRequest usuarioRequest) {
        try {
            String rolUsuario = (String) request.getProperty("rol");
            UsuarioService usuarioService = new UsuarioService();
            usuarioService.createUsuarioAdmin(usuarioRequest, EnumUsuario.valueOf(rolUsuario));
            return Response.status(Response.Status.CREATED)
                    .entity("{\"mensaje\": \"Administrador creado exitosamente\"}")
                    .build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (EntityAlreadyExistsException e) {
            return errorEjecucion(e.getMessage(), 2);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @POST
    @Secured
    @Path("/complemento/cliente")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUsuarioComplementoCliente(@Context ContainerRequestContext request, ClienteRequest clienteRequest) {
        try {
            String rolUsuario = (String) request.getProperty("rol");
            UsuarioService usuarioService = new UsuarioService();
            usuarioService.insertComplementoCliente(clienteRequest, EnumUsuario.valueOf(rolUsuario));
            return Response.status(Response.Status.CREATED)
                    .entity("{\"mensaje\": \"Complemento de cliente agregado exitosamente\"}")
                    .build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @POST
    @Secured
    @Path("/complemento/freelancer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUsuarioComplementoFreelancer(@Context ContainerRequestContext request, FreelancerRequest freelancerRequest) {
        try {
            String rolUsuario = (String) request.getProperty("rol");
            UsuarioService usuarioService = new UsuarioService();
            usuarioService.insertComplementoFreelancer(freelancerRequest, EnumUsuario.valueOf(rolUsuario));
            return Response.status(Response.Status.CREATED)
                    .entity("{\"mensaje\": \"Complemento de freelancer agregado exitosamente\"}")
                    .build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsuarios(@Context ContainerRequestContext request) {
        try {
            String rolUsuario = (String) request.getProperty("rol");
            UsuarioService usuarioService = new UsuarioService();
            List<UsuarioResponse> usuarioResponses = usuarioService.getAllUsuarios(EnumUsuario.valueOf(rolUsuario))
                    .stream()
                    .map(UsuarioResponse::new)
                    .toList();
            return Response.ok(usuarioResponses).build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarioById(@PathParam("id") int usuarioId) {
        try {
            UsuarioService usuarioService = new UsuarioService();
            Usuario usuario = usuarioService.getByUsuarioId(usuarioId);
            return Response.ok(new UsuarioResponse(usuario)).build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @GET
    @Path("/coincidence/{coincidencia}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuariosByCoincidence(@Context ContainerRequestContext request, @PathParam("coincidencia") String coincidencia) {
        try {
            String rolUsuario = (String) request.getProperty("rol");
            UsuarioService usuarioService = new UsuarioService();
            List<UsuarioResponse> usuarioResponses = usuarioService.getByCoincidences(EnumUsuario.valueOf(rolUsuario), coincidencia)
                    .stream()
                    .map(UsuarioResponse::new)
                    .toList();
            return Response.ok(usuarioResponses).build();
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
    public Response updateUsuarioActualizar(@Context ContainerRequestContext request, UsuarioUpdate usuarioUpdate) {
        try {
            int usuarioId = (int) request.getProperty("usuarioId");
            String rolUsuario = (String) request.getProperty("rol");
            UsuarioService usuarioService = new UsuarioService();
            usuarioService.updateUsuario(usuarioId, usuarioUpdate, EnumUsuario.valueOf(rolUsuario));
            return Response.ok("{\"mensaje\": \"Usuario actualizado exitosamente\"}").build();
        } catch (UserDataInvalidException e) {
            return errorEjecucion(e.getMessage(), 1);
        } catch (EntityAlreadyExistsException e) {
            return errorEjecucion(e.getMessage(), 2);
        } catch (SQLException e) {
            return errorEjecucion(e.getMessage(), 3);
        }
    }

    @PUT
    @Path("/actualizar-estado/{id}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUsuarioEstado(@Context ContainerRequestContext request, @PathParam("id") int usuarioId) {
        try {
            String rolUsuario = (String) request.getProperty("rol");
            UsuarioService usuarioService = new UsuarioService();
            usuarioService.updateUsuarioEstado(usuarioId, EnumUsuario.valueOf(rolUsuario));
            return Response.ok("{\"mensaje\": \"Estado del usuario actualizado exitosamente\"}").build();
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