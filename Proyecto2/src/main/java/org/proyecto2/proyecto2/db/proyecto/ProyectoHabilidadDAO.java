package org.proyecto2.proyecto2.db.proyecto;

import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.models.habilidad.Habilidad;
import org.proyecto2.proyecto2.models.proyecto.ProyectoHabilidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProyectoHabilidadDAO {
    private static final String INSERT_PROYECTO_HABILIDAD = "INSERT INTO proyecto_habilidad (proyecto_id, habilidad_id) VALUES (?, ?)";
    private static final String DELETE_PROYECTO_HABILIDAD = "DELETE FROM proyecto_habilidad WHERE proyecto_id = ? AND habilidad_id = ?";
    private static final String GET_BY_PROYECTO_ID = "SELECT h.* FROM proyecto_habilidad ph JOIN habilidad h ON ph.habilidad_id = h.habilidad_id WHERE ph.proyecto_id = ?";
    private static final String VALID_PROYECTO_HABILIDAD = "SELECT 1 FROM proyecto_habilidad WHERE proyecto_id = ? AND habilidad_id = ?";

    public boolean validProyectoHabilidad(ProyectoHabilidad proyectoHabilidad) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(VALID_PROYECTO_HABILIDAD)) {
            preparedStatement.setInt(1, proyectoHabilidad.getProyectoId());
            preparedStatement.setInt(2, proyectoHabilidad.getHabilidadId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public void insert(ProyectoHabilidad proyectoHabilidad) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROYECTO_HABILIDAD)) {
            preparedStatement.setInt(1, proyectoHabilidad.getProyectoId());
            preparedStatement.setInt(2, proyectoHabilidad.getHabilidadId());
            preparedStatement.executeUpdate();
        }
    }

    public void insert(ProyectoHabilidad proyectoHabilidad, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROYECTO_HABILIDAD)) {
            preparedStatement.setInt(1, proyectoHabilidad.getProyectoId());
            preparedStatement.setInt(2, proyectoHabilidad.getHabilidadId());
            preparedStatement.executeUpdate();
        }
    }

    public void delete(ProyectoHabilidad proyectoHabilidad) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PROYECTO_HABILIDAD)) {
            preparedStatement.setInt(1, proyectoHabilidad.getProyectoId());
            preparedStatement.setInt(2, proyectoHabilidad.getHabilidadId());
            preparedStatement.executeUpdate();
        }
    }

    public List<Habilidad> getAllHabilidadByProyecto(int proyectoId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Habilidad> habilidades = new java.util.ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_PROYECTO_ID)) {
            preparedStatement.setInt(1, proyectoId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Habilidad habilidad = new Habilidad(
                            resultSet.getString("nombre"),
                            resultSet.getString("descripcion")
                    );
                    habilidad.setEstado(resultSet.getBoolean("estado"));
                    habilidad.setHabilidadId(resultSet.getInt("habilidad_id"));
                    habilidades.add(habilidad);
                }
            }
        }
        return habilidades;
    }
}
