package org.proyecto2.proyecto2.services.usuario;

import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.db.usuario.UsuarioDAO;
import org.proyecto2.proyecto2.dtos.usuario.UsuarioRequest;
import org.proyecto2.proyecto2.dtos.usuario.UsuarioUpdate;
import org.proyecto2.proyecto2.dtos.usuario.cartera.CarteraRequest;
import org.proyecto2.proyecto2.dtos.usuario.cliente.ClienteRequest;
import org.proyecto2.proyecto2.dtos.usuario.cliente.ClienteUpdate;
import org.proyecto2.proyecto2.dtos.usuario.freelancer.FreelancerHabilidadRequest;
import org.proyecto2.proyecto2.dtos.usuario.freelancer.FreelancerRequest;
import org.proyecto2.proyecto2.dtos.usuario.freelancer.FreelancerUpdate;
import org.proyecto2.proyecto2.exceptions.EntityAlreadyExistsException;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.models.usuario.Usuario;
import org.proyecto2.proyecto2.models.usuario.cartera.Cartera;
import org.proyecto2.proyecto2.models.usuario.cartera.CarteraPlataforma;
import org.proyecto2.proyecto2.models.usuario.cartera.Transaccion;
import org.proyecto2.proyecto2.models.usuario.cartera.TransaccionPlataforma;
import org.proyecto2.proyecto2.models.usuario.freelancer.FreelancerHabilidad;
import org.proyecto2.proyecto2.services.usuario.cartera.CarteraPlataformaService;
import org.proyecto2.proyecto2.services.usuario.cartera.CarteraService;
import org.proyecto2.proyecto2.services.usuario.cartera.TransaccionPlataformaService;
import org.proyecto2.proyecto2.services.usuario.cartera.TransaccionService;
import org.proyecto2.proyecto2.services.usuario.cliente.ClienteService;
import org.proyecto2.proyecto2.services.usuario.freelancer.FreelancerHabilidadService;
import org.proyecto2.proyecto2.services.usuario.freelancer.FreelancerService;

import java.sql.Connection;
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
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        try {
            int usuarioId = usuarioDAO.insertClienteFreelancer(usuario, connection);
            CarteraService carteraService = new CarteraService();
            carteraService.createCartera(connection, usuarioId);
            usuario.setUsuarioId(usuarioId);
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
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
        Optional<Usuario> usuarioOptional = usuarioDAO.getById(usuarioId);
        if (usuarioOptional.isEmpty())
            throw new UserDataInvalidException("No se puedo obtener el usuario seleccionado, vuelva a intentar.");
        Usuario usuario = usuarioOptional.get();
        if (EnumUsuario.Cliente.equals(usuario.getRol())) {
            ClienteService clienteService = new ClienteService();
            usuario.setCliente(clienteService.getComplemento(usuarioId).orElse(null));
        } else if (EnumUsuario.Freelancer.equals(usuario.getRol())) {
            FreelancerService freelancerService = new FreelancerService();
            usuario.setFreelancer(freelancerService.getComplemento(usuarioId).orElse(null));
        }
        return usuario;
    }

    public void updateUsuario(int usuarioId, UsuarioUpdate usuarioUpdate, EnumUsuario rol) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        Usuario usuario = new Usuario(usuarioUpdate);
        if (!usuario.isValidUpdate()) throw new UserDataInvalidException("Todos los campos son requeridos.");
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuario.setUsuarioId(usuarioId);
        usuario.setRol(rol);
        if (usuarioDAO.validUsernameUpdate(usuario.getUserName(), usuario.getUsuarioId()))
            throw new EntityAlreadyExistsException("El username ya està registrado en otro usuario.");
        if (usuarioDAO.validEmailUpdate(usuario.getEmail(), usuario.getUsuarioId()))
            throw new EntityAlreadyExistsException("El email ya està registrado en otro usuario.");
        if (usuarioDAO.validCUIUpdate(usuario.getCui(), usuario.getUsuarioId()))
            throw new EntityAlreadyExistsException("El cui ya està registrado en otro usuario.");
        if (usuarioDAO.validTelefonoUpdate(usuario.getTelefono(), usuario.getUsuarioId()))
            throw new EntityAlreadyExistsException("El teléfono ya està registrado en otro usuario.");
        if (EnumUsuario.Administrador.equals(usuario.getRol())) {
            usuarioDAO.update(usuario);
        } else {
            actualizarUsuarioClienteFreelancer(usuario, usuarioUpdate.getClienteUpdate(), usuarioUpdate.getFreelancerUpdate(), usuarioDAO);
        }
    }

    private void actualizarUsuarioClienteFreelancer(Usuario usuario, ClienteUpdate clienteUpdate, FreelancerUpdate freelancerUpdate, UsuarioDAO usuarioDAO) throws SQLException, UserDataInvalidException {
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        try {
            if (EnumUsuario.Cliente.equals(usuario.getRol())) {
                ClienteService clienteService = new ClienteService();
                clienteService.updateComplemento(connection, clienteUpdate, usuario.getRol(), usuario.getUsuarioId());
            } else if (EnumUsuario.Freelancer.equals(usuario.getRol())) {
                FreelancerService freelancerService = new FreelancerService();
                freelancerService.updateComplemento(connection, freelancerUpdate, usuario.getRol(), usuario.getUsuarioId());
            }
            usuarioDAO.update(connection, usuario);
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
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

    public void insertComplementoCliente(ClienteRequest clienteRequest, EnumUsuario rol, int usuarioId) throws SQLException, UserDataInvalidException {
        ClienteService clienteService = new ClienteService();
        clienteService.insertComplemento(clienteRequest, rol, usuarioId);
    }

    public void insertComplementoFreelancer(FreelancerRequest freelancerRequest, EnumUsuario rol, int usuarioId) throws SQLException, UserDataInvalidException,EntityAlreadyExistsException {
        FreelancerService freelancerService = new FreelancerService();
        freelancerService.insertComplemento(freelancerRequest, rol, usuarioId);
    }

    public void recargarCartera(EnumUsuario rol, CarteraRequest carteraRequest) throws SQLException, UserDataInvalidException {
        if (EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException(String.format("Un usuario con rol: %s, no puede recargar una cartera, ya que no cuenta con una, solo los usuarios con rol: %s o %s, pueden recargar.", EnumUsuario.Administrador, EnumUsuario.Cliente, EnumUsuario.Freelancer));
        CarteraService carteraService = new CarteraService();
        carteraService.recargarCartera(carteraRequest.getUsuarioId(), carteraRequest.getMonto());
    }

    public Cartera getCartera(int usuarioId) throws SQLException, UserDataInvalidException {
        CarteraService carteraService = new CarteraService();
        return carteraService.getCarteraById(usuarioId);
    }

    public CarteraPlataforma getCarteraPlataforma() throws SQLException {
        CarteraPlataformaService carteraPlataformaService = new CarteraPlataformaService();
        return carteraPlataformaService.getCarteraPlataforma();
    }

    public List<Transaccion> getTransaccionesByUsuarioId(int usuarioId) throws SQLException {
        TransaccionService transaccionService = new TransaccionService();
        return transaccionService.getTransaccionesByUsuarioId(usuarioId);
    }

    public List<TransaccionPlataforma> getTransaccionPlataforma() throws SQLException {
        TransaccionPlataformaService transaccionService = new TransaccionPlataformaService();
        return transaccionService.getAllTransaccionPlataformas();
    }

    public void agregarHabilidadAFreelancer(FreelancerHabilidadRequest freelancerHabilidadRequest, EnumUsuario rol, int usuarioId) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        FreelancerHabilidadService freelancerHabilidadService = new FreelancerHabilidadService();
        freelancerHabilidadService.insert(freelancerHabilidadRequest, rol, usuarioId);
    }

    public void deleteHabilidadAFreelancer(int habilidadId, int usuarioId, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        FreelancerHabilidadService freelancerHabilidadService = new FreelancerHabilidadService();
        freelancerHabilidadService.delete(habilidadId,rol,usuarioId);
    }

}
