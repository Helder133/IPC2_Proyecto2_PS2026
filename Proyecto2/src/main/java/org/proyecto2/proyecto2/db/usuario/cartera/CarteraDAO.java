package org.proyecto2.proyecto2.db.usuario.cartera;

import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.models.usuario.cartera.Cartera;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class CarteraDAO {
    private static final String INSERT_CARTERA = "INSERT INTO cartera (usuario_id, saldo, saldo_bloqueado) VALUES (?, ?, ?)";
    private static final String UPDATE_CARTERA = "UPDATE cartera SET saldo = ?, saldo_bloqueado = ? WHERE usuario_id = ?";
    private static final String SELECT_CARTERA_BY_ID = "SELECT * FROM cartera WHERE usuario_id = ?";
    private static final String VALID_CARTERA = "SELECT 1 FROM cartera WHERE usuario_id = ?";

    public boolean validCartera(Connection connection, int usuarioId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(VALID_CARTERA)) {
            preparedStatement.setInt(1, usuarioId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public void insert(Connection connection, Cartera cartera) throws SQLException {
        try (PreparedStatement insert = connection.prepareStatement(INSERT_CARTERA)) {
            insert.setInt(1, cartera.getUsuarioId());
            insert.setDouble(2, cartera.getSaldo());
            insert.setDouble(3, cartera.getSaldoBloqueado());
            insert.executeUpdate();
        }
    }

    public void update(Connection connection, Cartera cartera) throws SQLException {
        try (PreparedStatement update = connection.prepareStatement(UPDATE_CARTERA)) {
            update.setDouble(1, cartera.getSaldo());
            update.setDouble(2, cartera.getSaldoBloqueado());
            update.setInt(3, cartera.getUsuarioId());
            update.executeUpdate();
        }
    }

    public Optional<Cartera> getById(int id) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CARTERA_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Cartera cartera = new Cartera(
                        resultSet.getInt("usuario_id"),
                        resultSet.getDouble("saldo"),
                        resultSet.getDouble("saldo_bloqueado")
                    );
                    return Optional.of(cartera);
                }
                return Optional.empty();
            }
        }
    }
}
