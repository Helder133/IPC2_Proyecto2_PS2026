package org.proyecto2.proyecto2.db.usuario.cliente;

import org.proyecto2.proyecto2.db.config.CRUD;
import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.models.usuario.cliente.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ClienteDAO implements CRUD<Cliente> {
    private static final String INSERT_COMPLEMENTO = "INSERT INTO cliente (usuario_id, descripcion, sector, sitio_web) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_COMPLEMENTO = "UPDATE cliente SET descripcion = ?, sector = ?, sitio_web = ? WHERE usuario_id = ?";
    private static final String GET_COMPLEMENTO = "SELECT * FROM cliente WHERE usuario_id = ?";
    private static final String VALID_COMPLEMENTO = "SELECT 1 FROM cliente WHERE usuario_id = ?";

    public boolean validComplemento(int usuarioId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(VALID_COMPLEMENTO)) {
            statement.setInt(1, usuarioId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public void insert(Cliente cliente) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement insert = connection.prepareStatement(INSERT_COMPLEMENTO)) {
            insert.setInt(1, cliente.getUsuarioId());
            insert.setString(2, cliente.getDescripcion());
            insert.setString(3, cliente.getSector());
            insert.setString(4, cliente.getSitioWeb());
            insert.executeUpdate();
        }
    }

    @Override
    public void update(Cliente cliente) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement update = connection.prepareStatement(UPDATE_COMPLEMENTO)) {
            update.setString(1, cliente.getDescripcion());
            update.setString(2, cliente.getSector());
            update.setString(3, cliente.getSitioWeb());
            update.setInt(4, cliente.getUsuarioId());
            update.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public Optional<Cliente> getById(int id) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(GET_COMPLEMENTO)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Cliente cliente = new Cliente(
                            resultSet.getInt("usuario_id"),
                            resultSet.getString("descripcion"),
                            resultSet.getString("sector"),
                            resultSet.getString("sitio_web")
                    );
                    return Optional.of(cliente);
                } else {
                    return Optional.empty();
                }

            }
        }
    }

    @Override
    public List<Cliente> getAll() throws SQLException {
        return List.of();
    }
}
