package org.proyecto2.proyecto2.db.usuario.freelancer;

import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.models.habilidad.Habilidad;
import org.proyecto2.proyecto2.models.usuario.freelancer.FreelancerHabilidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FreelancerHabilidadDAO {
    private static final String INSERT = "INSERT INTO freelancer_habilidad(usuario_id, habilidad_id) VALUES (?, ?)";
    private static final String GET_BY_USUARIO_ID = "SELECT h.* FROM freelancer_habilidad fh JOIN habilidad h ON fh.habilidad_id = h.habilidad_id WHERE fh.usuario_id = ?";
    private static final String VALID = "SELECT 1 FROM freelancer_habilidad WHERE usuario_id = ? AND habilidad_id = ?";
    private static final String DELETE = "DELETE FROM freelancer_habilidad WHERE usuario_id = ? AND  habilidad_id = ?";

    public boolean validaHabilidad(FreelancerHabilidad freelancerHabilidad) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement valid = connection.prepareStatement(VALID)) {
            valid.setInt(1, freelancerHabilidad.getUsuarioId());
            valid.setInt(2, freelancerHabilidad.getHabilidadId());
            try (ResultSet rs = valid.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void insert(FreelancerHabilidad freelancerHabilidad) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement insert = connection.prepareStatement(INSERT)) {
            insert.setInt(1, freelancerHabilidad.getUsuarioId());
            insert.setInt(2, freelancerHabilidad.getHabilidadId());
            insert.executeUpdate();
        }
    }

    public List<Habilidad> getAllHabilidadByUsuario(int usuarioId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Habilidad> habilidades = new ArrayList<>();
        try (PreparedStatement select = connection.prepareStatement(GET_BY_USUARIO_ID)) {
            select.setInt(1, usuarioId);
            try (ResultSet rs = select.executeQuery()) {
                while (rs.next()) {
                    Habilidad habilidad = new Habilidad(
                            rs.getString("nombre"),
                            rs.getString("descripcion")
                    );
                    habilidad.setEstado(rs.getBoolean("estado"));
                    habilidad.setHabilidadId(rs.getInt("habilidad_id"));
                    habilidades.add(habilidad);
                }
                return habilidades;
            }
        }
    }

    public void delete(FreelancerHabilidad freelancerHabilidad) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement delete = connection.prepareStatement(DELETE)) {
            delete.setInt(1, freelancerHabilidad.getUsuarioId());
            delete.setInt(2, freelancerHabilidad.getHabilidadId());
            delete.executeUpdate();
        }
    }

}
