package org.proyecto2.proyecto2.db.usuario.freelancer;

import org.proyecto2.proyecto2.db.config.CRUD;
import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.models.usuario.freelancer.EnumFreelancer;
import org.proyecto2.proyecto2.models.usuario.freelancer.Freelancer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class FreelancerDAO implements CRUD<Freelancer> {
    private static final String INSERT_COMPLEMENTO = "INSERT freelancer (usuario_id, descripcion, experiencia, tarifa_hora) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_COMPLEMENTO = "UPDATE freelancer SET descripcion = ?, experiencia = ?, tarifa_hora = ? WHERE usuario_id = ?";
    private static final String GET_COMPLEMENTO = "SELECT * FROM freelancer WHERE usuario_id = ?";
    private static final String VALID_COMPLEMENTO = "SELECT 1 FROM freelancer WHERE usuario_id = ?";

    public boolean validComplemento(int usuarioId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(VALID_COMPLEMENTO)) {
            statement.setInt(1, usuarioId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public void insert(Freelancer freelancer, Connection connection) throws SQLException {
        try (PreparedStatement insert = connection.prepareStatement(INSERT_COMPLEMENTO)) {
            insert.setInt(1, freelancer.getUsuarioId());
            insert.setString(2, freelancer.getDescripcion());
            insert.setString(3, freelancer.getExperiencia().name());
            insert.setDouble(4, freelancer.getTarifaHora());
            insert.executeUpdate();
        }
    }

    @Override
    public void insert(Freelancer freelancer) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement insert = connection.prepareStatement(INSERT_COMPLEMENTO)) {
            insert.setInt(1, freelancer.getUsuarioId());
            insert.setString(2, freelancer.getDescripcion());
            insert.setString(3, freelancer.getExperiencia().name());
            insert.setDouble(4, freelancer.getTarifaHora());
            insert.executeUpdate();
        }
    }

    @Override
    public void update(Freelancer freelancer) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement update = connection.prepareStatement(UPDATE_COMPLEMENTO)) {
            update.setString(1, freelancer.getDescripcion());
            update.setString(2, freelancer.getExperiencia().name());
            update.setDouble(3, freelancer.getTarifaHora());
            update.setInt(4, freelancer.getUsuarioId());
            update.executeUpdate();
        }
    }

    public void update(Connection connection, Freelancer freelancer) throws SQLException {
        try (PreparedStatement update = connection.prepareStatement(UPDATE_COMPLEMENTO)) {
            update.setString(1, freelancer.getDescripcion());
            update.setString(2, freelancer.getExperiencia().name());
            update.setDouble(3, freelancer.getTarifaHora());
            update.setInt(4, freelancer.getUsuarioId());
            update.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public Optional<Freelancer> getById(int id) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(GET_COMPLEMENTO)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Freelancer freelancer = new Freelancer(
                            resultSet.getInt("usuario_id"),
                            resultSet.getString("descripcion"),
                            EnumFreelancer.valueOf(resultSet.getString("experiencia")),
                            resultSet.getDouble("tarifa_hora")
                    );
                    return Optional.of(freelancer);
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    @Override
    public List<Freelancer> getAll() throws SQLException {
        return List.of();
    }
}
