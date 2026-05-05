package org.proyecto2.proyecto2.db.habilidad;

import org.proyecto2.proyecto2.db.config.CRUD;
import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.models.habilidad.Habilidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HabilidadDAO implements CRUD<Habilidad> {
    private static final String INSERT_HABILIDAD = "INSERT INTO habilidad(nombre, descripcion) VALUES (?, ?)";
    private static final String UPDATE_HABILIDAD = "UPDATE habilidad SET nombre = ?, descripcion = ? WHERE habilidad_id = ?";
    private static final String UPDATE_HABILIDAD_ESTADO = "UPDATE habilidad SET estado = NOT estado WHERE habilidad_id = ?";
    private static final String GET_HABILIDAD_BY_ID = "SELECT * FROM habilidad WHERE habilidad_id = ?";
    private static final String GET_HABILIDAD_BY_COINCIDENCE = "SELECT * FROM habilidad WHERE nombre like ?";
    private static final String GET_ALL_HABILIDAD = "SELECT * FROM habilidad";
    private static final String VALID_NOMBRE = "SELECT 1 FROM habilidad WHERE nombre = ?";
    private static final String VALID_NOMBRE_UPDATE = "SELECT 1 FROM habilidad WHERE nombre = ? AND habilidad_id <> ?";
    private static final String GET_ALL_HABILIDAD_ACTIVADA = "SELECT * FROM habilidad WHERE estado = 1";

    public boolean validNombre(String nombre) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement valid = connection.prepareStatement(VALID_NOMBRE)) {
            valid.setString(1, nombre);
            try (ResultSet rs = valid.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean validNombreUpdate(String nombre, int usuarioId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement valid = connection.prepareStatement(VALID_NOMBRE_UPDATE)) {
            valid.setString(1, nombre);
            valid.setInt(2, usuarioId);
            try (ResultSet rs = valid.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public void insert(Habilidad habilidad) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement insert = connection.prepareStatement(INSERT_HABILIDAD)) {
            insert.setString(1, habilidad.getNombre());
            insert.setString(2, habilidad.getDescripcion());
            insert.executeUpdate();
        }
    }

    @Override
    public void update(Habilidad habilidad) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement update = connection.prepareStatement(UPDATE_HABILIDAD)) {
            update.setString(1, habilidad.getNombre());
            update.setString(2, habilidad.getDescripcion());
            update.setInt(3, habilidad.getHabilidadId());
            update.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
    }

    @Override
    public Optional<Habilidad> getById(int id) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement select = connection.prepareStatement(GET_HABILIDAD_BY_ID)) {
            select.setInt(1, id);
            try (ResultSet rs = select.executeQuery()) {
                if (rs.next()) return Optional.of(extraerDatos(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Habilidad> getAll() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Habilidad> habilidades = new ArrayList<>();
        try (PreparedStatement select = connection.prepareStatement(GET_ALL_HABILIDAD);
             ResultSet rs = select.executeQuery()) {
            while (rs.next()) habilidades.add(extraerDatos(rs));
            return habilidades;
        }
    }

    public List<Habilidad> getByCoincidence(String nombre) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Habilidad> habilidades = new ArrayList<>();
        try (PreparedStatement select = connection.prepareStatement(GET_HABILIDAD_BY_COINCIDENCE)) {
            select.setString(1, "%" + nombre + "%");
            try (ResultSet rs = select.executeQuery()) {
                while (rs.next()) habilidades.add(extraerDatos(rs));
                return habilidades;
            }
        }
    }

    public List<Habilidad> getAllHabilidadActivada() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Habilidad> habilidades = new ArrayList<>();
        try (PreparedStatement select = connection.prepareStatement(GET_ALL_HABILIDAD_ACTIVADA);
             ResultSet rs = select.executeQuery()) {
            while (rs.next()) habilidades.add(extraerDatos(rs));
            return habilidades;
        }
    }

    public void updateEstado(int usuarioId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement update = connection.prepareStatement(UPDATE_HABILIDAD_ESTADO)) {
            update.setInt(1, usuarioId);
            update.executeUpdate();
        }
    }

    public Habilidad extraerDatos(ResultSet rs) throws SQLException {
        Habilidad habilidad = new Habilidad(
                rs.getString("nombre"),
                rs.getString("descripcion")
        );
        habilidad.setHabilidadId(rs.getInt("habilidad_id"));
        habilidad.setEstado(rs.getBoolean("estado"));
        return habilidad;
    }

}
