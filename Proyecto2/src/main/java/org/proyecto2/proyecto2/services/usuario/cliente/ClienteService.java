package org.proyecto2.proyecto2.services.usuario.cliente;

import org.proyecto2.proyecto2.db.usuario.cliente.ClienteDAO;
import org.proyecto2.proyecto2.dtos.usuario.cliente.ClienteRequest;
import org.proyecto2.proyecto2.dtos.usuario.cliente.ClienteUpdate;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.models.usuario.cliente.Cliente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class ClienteService {
    public void insertComplemento(ClienteRequest clienteRequest, EnumUsuario rol, int usuarioId) throws SQLException, UserDataInvalidException {
        Cliente cliente = new Cliente(clienteRequest);
        cliente.setUsuarioId(usuarioId);
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException(String.format("Solo un usuario con el rol: %s, puede agregar un complemento de cliente.", EnumUsuario.Cliente));
        if (!cliente.isValid()) throw new UserDataInvalidException("Descripción y sector son requeridos");
        ClienteDAO clienteDAO = new ClienteDAO();
        if (clienteDAO.validComplemento(cliente.getUsuarioId()))
            throw new UserDataInvalidException("El complemento de cliente ya està registrado para este usuario.");
        clienteDAO.insert(cliente);
    }

    public void updateComplemento(Connection connection, ClienteUpdate clienteclienteUpdate, EnumUsuario rol, int usuarioId) throws SQLException, UserDataInvalidException {
        Cliente cliente = new Cliente(clienteclienteUpdate);
        if (!cliente.isValid()) throw new UserDataInvalidException("Descripción y sector son requeridos");
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException(String.format("Solo un usuario con el rol: %s, puede actualizar su complemento de cliente.", EnumUsuario.Cliente));
        if (cliente.getUsuarioId() != usuarioId)
            throw new UserDataInvalidException("No cuenta con el permiso para actualizar el complemento de cliente de otro usuario.");
        ClienteDAO clienteDAO = new ClienteDAO();
        clienteDAO.update(cliente, connection);
    }

    public Optional<Cliente> getComplemento(int usuarioId) throws SQLException {
        ClienteDAO clienteDAO = new ClienteDAO();
        return clienteDAO.getById(usuarioId);
    }

}
