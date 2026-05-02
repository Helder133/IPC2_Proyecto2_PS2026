package org.proyecto2.proyecto2.services.usuario;

import org.proyecto2.proyecto2.db.usuario.UsuarioDAO;
import org.proyecto2.proyecto2.dtos.usuario.UsuarioRequest;
import org.proyecto2.proyecto2.dtos.usuario.UsuarioUpdate;
import org.proyecto2.proyecto2.dtos.usuario.cliente.ClienteRequest;
import org.proyecto2.proyecto2.dtos.usuario.freelancer.FreelancerRequest;
import org.proyecto2.proyecto2.exceptions.EntityAlreadyExistsException;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.models.usuario.Usuario;
import org.proyecto2.proyecto2.services.usuario.cliente.ClienteService;
import org.proyecto2.proyecto2.services.usuario.freelancer.FreelancerService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UsuarioService {
    public Usuario createUsuarioClienteFreelancer(UsuarioRequest usuarioRequest) throws UserDataInvalidException, EntityAlreadyExistsException, SQLException {
        Usuario usuario = new Usuario(usuarioRequest);
        if (EnumUsuario.Administrador.equals(usuario.getRol()))
            throw new UserDataInvalidException("No cuenta con el permiso para crear un usuario con este rol Administrador.");
        if (!usuario.isValid()) throw new UserDataInvalidException("Todos los campos son requeridos.");
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        if (usuarioDAO.validUsername(usuario.getUserName()))
            throw new EntityAlreadyExistsException("El username ya està registrado en otro usuario.");
        if (usuarioDAO.validEmail(usuario.getEmail()))
            throw new EntityAlreadyExistsException("El email ya està registrado en otro usuario.");
        if (usuarioDAO.validCUI(usuario.getCui()))
            throw new EntityAlreadyExistsException("El cui ya està registrado en otro usuario.");
        if (usuarioDAO.validTelefono(usuario.getTelefono()))
            throw new EntityAlreadyExistsException("El teléfono ya està registrado en otro usuario.");
        int usuarioId = usuarioDAO.insertClienteFreelancer(usuario);
        usuario.setUsuarioId(usuarioId);
        return usuario;
    }

    public void createUsuarioAdmin(UsuarioRequest usuarioRequest, EnumUsuario rol) throws UserDataInvalidException, EntityAlreadyExistsException, SQLException {
        if (!EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("No cuenta con el permiso para crear un usuario con este rol Administrador.");
        Usuario usuario = new Usuario(usuarioRequest);
        if (!usuario.isValid()) throw new UserDataInvalidException("Todos los campos son requeridos.");
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        if (usuarioDAO.validUsername(usuario.getUserName()))
            throw new EntityAlreadyExistsException("El username ya està registrado en otro usuario.");
        if (usuarioDAO.validEmail(usuario.getEmail()))
            throw new EntityAlreadyExistsException("El email ya està registrado en otro usuario.");
        if (usuarioDAO.validCUI(usuario.getCui()))
            throw new EntityAlreadyExistsException("El cui ya està registrado en otro usuario.");
        if (usuarioDAO.validTelefono(usuario.getTelefono()))
            throw new EntityAlreadyExistsException("El teléfono ya està registrado en otro usuario.");
        usuarioDAO.insert(usuario);
    }

    public List<Usuario> getAllUsuarios(EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("No cuenta con el permiso para ver el listado de usuarios que hay registrado dentro de la plataforma.");
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        return usuarioDAO.getAll();
    }

    public List<Usuario> getByCoincidences(EnumUsuario rol, String coincidencia) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("No cuenta con el permiso para ver el listado de usuarios que hay registrado dentro de la plataforma.");
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        return usuarioDAO.getByCoincidence(coincidencia);
    }

    public Usuario getByUsuarioId(int usuarioId) throws SQLException, UserDataInvalidException {
        if (usuarioId <= 0)
            throw new UserDataInvalidException("El id del usuario es requerido y debe ser un número entero positivo.");
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Optional<Usuario> usuario = usuarioDAO.getById(usuarioId);
        if (usuario.isEmpty())
            throw new UserDataInvalidException("No se puedo obtener el usuario seleccionado, vuelva a intentar.");
        return usuario.get();
    }

    public void updateUsuario(int usuarioId, UsuarioUpdate usuarioUpdate) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        Usuario usuario = new Usuario(usuarioUpdate);
        if (!usuario.isValidUpdate()) throw new UserDataInvalidException("Todos los campos son requeridos.");
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        if (usuario.getUsuarioId() != usuarioId)
            throw new UserDataInvalidException("El id del usuario no coincide con el id del usuario a actualizar.");
        if (usuarioDAO.validUsernameUpdate(usuario.getUserName(), usuario.getUsuarioId()))
            throw new EntityAlreadyExistsException("El username ya està registrado en otro usuario.");
        if (usuarioDAO.validEmailUpdate(usuario.getEmail(), usuario.getUsuarioId()))
            throw new EntityAlreadyExistsException("El email ya està registrado en otro usuario.");
        if (usuarioDAO.validCUIUpdate(usuario.getCui(), usuario.getUsuarioId()))
            throw new EntityAlreadyExistsException("El cui ya està registrado en otro usuario.");
        if (usuarioDAO.validTelefonoUpdate(usuario.getTelefono(), usuario.getUsuarioId()))
            throw new EntityAlreadyExistsException("El teléfono ya està registrado en otro usuario.");
        usuarioDAO.update(usuario);
    }

    public void updateUsuarioEstado(int usuarioId, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("No cuenta con el permiso para cambiar el estado de un usuario.");
        if (usuarioId <= 0)
            throw new UserDataInvalidException("El id del usuario es requerido y debe ser un número entero positivo.");
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Optional<Usuario> usuario = usuarioDAO.getById(usuarioId);
        if (usuario.isEmpty())
            throw new UserDataInvalidException("No se puedo obtener el usuario seleccionado, vuelva a intentar.");
        usuarioDAO.updateEstado(usuarioId);
    }

    public void insertComplementoCliente(ClienteRequest clienteRequest, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        ClienteService clienteService = new ClienteService();
        clienteService.insertComplemento(clienteRequest, rol);
    }

    public void insertComplementoFreelancer(FreelancerRequest freelancerRequest, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        FreelancerService freelancerService = new FreelancerService();
        freelancerService.insertComplemento(freelancerRequest, rol);
    }

}
