package org.proyecto2.proyecto2.db.configuracionSistema;

import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.models.configuracionSistema.ConfiguracionSistema;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConfiguracionSistemaDAO {
    private static final String INSERT_CONFIGURACION = "INSERT INTO configuracion_sistema(comision, fecha_inicio) VALUES (?, ?)";
    private static final String GET_ALL_CONFIGURACION = "SELECT * FROM configuracion_sistema";
    private static final String GET_ULTIMA_CONFIGURACION = "SELECT * FROM configuracion_sistema ORDER BY configuracion_id DESC LIMIT 1";
    private static final String GET_ULTIMA_CONFIGURACION_ID = "SELECT configuracion_id FROM configuracion_sistema ORDER BY configuracion_id DESC LIMIT 1";
    private static final String UPDATE_CONFIGURACION = "UPDATE configuracion_sistema SET fecha_fin = ? WHERE configuracion_id = ?";

    public int getUltimaConfiguracionId(Connection connection) throws SQLException {
        try (PreparedStatement select = connection.prepareStatement(GET_ULTIMA_CONFIGURACION_ID);
             ResultSet resultSet = select.executeQuery()) {
            if (resultSet.next()) return resultSet.getInt("configuracion_id");
            else return -1;
        }
    }

    public void insert(ConfiguracionSistema configuracionSistema, Connection connection) throws SQLException {
        try (PreparedStatement insert = connection.prepareStatement(INSERT_CONFIGURACION)) {
            insert.setDouble(1, configuracionSistema.getComision());
            insert.setDate(2, Date.valueOf(configuracionSistema.getFechaInicio()));
            insert.executeUpdate();
        }
    }

    public void update(LocalDate fecha, int configuracionId, Connection connection) throws SQLException {
        try (PreparedStatement update = connection.prepareStatement(UPDATE_CONFIGURACION)) {
            update.setDate(1, Date.valueOf(fecha));
            update.setInt(2, configuracionId);
            update.executeUpdate();
        }
    }

    public List<ConfiguracionSistema> getAllConfiguracionSistema() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<ConfiguracionSistema> configuracionSistemas = new ArrayList<>();
        try (PreparedStatement select = connection.prepareStatement(GET_ALL_CONFIGURACION);
             ResultSet resultSet = select.executeQuery()) {
            while (resultSet.next()) configuracionSistemas.add(extraerDatos(resultSet));
            return configuracionSistemas;
        }
    }

    public Optional<ConfiguracionSistema> getUltimaConfiguracion() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement select = connection.prepareStatement(GET_ULTIMA_CONFIGURACION);
             ResultSet resultSet = select.executeQuery()) {
            if (resultSet.next()) return Optional.of(extraerDatos(resultSet));
            return Optional.empty();
        }
    }

    public Optional<ConfiguracionSistema> getUltimaConfiguracion(Connection connection) throws SQLException {
        try (PreparedStatement select = connection.prepareStatement(GET_ULTIMA_CONFIGURACION);
             ResultSet resultSet = select.executeQuery()) {
            if (resultSet.next()) return Optional.of(extraerDatos(resultSet));
            return Optional.empty();
        }
    }

    private ConfiguracionSistema extraerDatos(ResultSet resultSet) throws SQLException {
        return new ConfiguracionSistema(
                resultSet.getInt("configuracion_id"),
                resultSet.getDouble("comision"),
                resultSet.getDate("fecha_inicio").toLocalDate(),
                resultSet.getDate("fecha_fin") != null ? resultSet.getDate("fecha_fin").toLocalDate() : null
        );
    }
}
