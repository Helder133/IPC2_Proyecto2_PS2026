package org.proyecto2.proyecto2.db.usuario.cartera;

import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.usuario.cartera.CarteraPlataforma;

import java.sql.*;
import java.util.Optional;

public class CarteraPlataformaDAO {
    private static final String INSERT_CARTERA_PLATAFORMA = "INSERT INTO cartera_plataforma (saldo) VALUES (?)";
    private static final String GET_CARTERA_PLATAFORMA = "SELECT * FROM cartera_plataforma ORDER BY plataforma_id DESC LIMIT 1";
    private static final String UPDATE_CARTERA_PLATAFORMA = "UPDATE cartera_plataforma SET saldo = ? WHERE plataforma_id = ?";

    public int insert(CarteraPlataforma carteraPlataforma) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement insert = connection.prepareStatement(INSERT_CARTERA_PLATAFORMA, Statement.RETURN_GENERATED_KEYS)) {
            insert.setDouble(1, carteraPlataforma.getSaldo());
            insert.executeUpdate();
            try (ResultSet generatedKeys = insert.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating cartera plataforma failed, no ID obtained.");
                }
            }

        }
    }

    public Optional<CarteraPlataforma> getCarteraPlataforma() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(GET_CARTERA_PLATAFORMA);
             ResultSet result = statement.executeQuery()) {
            if (result.next()) {
                CarteraPlataforma carteraPlataforma = new CarteraPlataforma(
                        result.getDouble("saldo"));
                carteraPlataforma.setPlataformaId(result.getInt("plataforma_id"));
                return Optional.of(carteraPlataforma);
            }
            return Optional.empty();
        }
    }

    public void updateCarteraPlataforma(Connection connection, CarteraPlataforma carteraPlataforma) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_CARTERA_PLATAFORMA)) {
            statement.setDouble(1, carteraPlataforma.getSaldo());
            statement.setInt(2, carteraPlataforma.getPlataformaId());
            statement.executeUpdate();
        }
    }
}
