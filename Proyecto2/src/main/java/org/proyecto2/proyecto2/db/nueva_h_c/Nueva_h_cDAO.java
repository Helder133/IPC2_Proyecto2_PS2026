package org.proyecto2.proyecto2.db.nueva_h_c;

import org.proyecto2.proyecto2.db.config.CRUD;
import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.models.nueva_h_c.EnumNueva_h_cEstado;
import org.proyecto2.proyecto2.models.nueva_h_c.EnumNueva_h_cTipo;
import org.proyecto2.proyecto2.models.nueva_h_c.Nueva_h_c;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Nueva_h_cDAO implements CRUD<Nueva_h_c> {
    private static final String INSERT_NUEVO = "INSERT INTO nueva_h_c (usuario_id, nombre, descripcion, tipo, estado, fecha_creacion) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_NUEVO = "UPDATE nueva_h_c SET nombre = ?, descripcion = ? WHERE solicitud_id = ?";
    private static final String UPDATE_ESTADO = "UPDATE nueva_h_c SET estado = ? WHERE solicitud_id = ?";
    private static final String GET_NUEVO_BY_ID = "SELECT n.*, u.nombre_completo, u.user_name FROM nueva_h_c n JOIN usuario u ON n.usuario_id = u.usuario_id WHERE n.solicitud_id = ?";
    private static final String GET_ALL_NUEVO = "SELECT n.*, u.nombre_completo, u.user_name FROM nueva_h_c n JOIN usuario u ON n.usuario_id = u.usuario_id";
    private static final String GET_ALL_NUEVO_BY_USUARIO = "SELECT n.*, u.nombre_completo, u.user_name FROM nueva_h_c n JOIN usuario u ON n.usuario_id = u.usuario_id WHERE n.usuario_id = ?";

    @Override
    public void insert(Nueva_h_c nuevaHC) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement insertCategoria = connection.prepareStatement(INSERT_NUEVO)) {
            insertCategoria.setInt(1, nuevaHC.getUsuarioId());
            insertCategoria.setString(2, nuevaHC.getNombre());
            insertCategoria.setString(3, nuevaHC.getDescripcion());
            insertCategoria.setString(4, nuevaHC.getTipo().name());
            insertCategoria.setString(5, nuevaHC.getEstado().name());
            insertCategoria.setDate(6, Date.valueOf(nuevaHC.getFechaCreacion()));
            insertCategoria.executeUpdate();
        }
    }

    public void insert(Nueva_h_c nuevaHC, Connection connection) throws SQLException {
        try (PreparedStatement insertCategoria = connection.prepareStatement(INSERT_NUEVO)) {
            insertCategoria.setInt(1, nuevaHC.getUsuarioId());
            insertCategoria.setString(2, nuevaHC.getNombre());
            insertCategoria.setString(3, nuevaHC.getDescripcion());
            insertCategoria.setString(4, nuevaHC.getTipo().name());
            insertCategoria.setString(5, nuevaHC.getEstado().name());
            insertCategoria.setDate(6, Date.valueOf(nuevaHC.getFechaCreacion()));
            insertCategoria.executeUpdate();
        }
    }

    @Override
    public void update(Nueva_h_c nuevaHC) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement update = connection.prepareStatement(UPDATE_NUEVO)) {
            update.setString(1, nuevaHC.getNombre());
            update.setString(2, nuevaHC.getDescripcion());
            update.setInt(3, nuevaHC.getSolicitudId());
            update.executeUpdate();
        }
    }

    public void update(EnumNueva_h_cEstado estado, int solicitudId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement update = connection.prepareStatement(UPDATE_ESTADO)) {
            update.setString(1, estado.name());
            update.setInt(2, solicitudId);
            update.executeUpdate();
        }
    }
    public void update(EnumNueva_h_cEstado estado, int solicitudId, Connection connection) throws SQLException {
        try (PreparedStatement update = connection.prepareStatement(UPDATE_ESTADO)) {
            update.setString(1, estado.name());
            update.setInt(2, solicitudId);
            update.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
    }

    @Override
    public Optional<Nueva_h_c> getById(int id) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement select = connection.prepareStatement(GET_NUEVO_BY_ID)) {
            select.setInt(1, id);
            try (ResultSet rs = select.executeQuery()) {
                if (rs.next()) return Optional.of(extraerDatos(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Nueva_h_c> getAll() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Nueva_h_c> list = new ArrayList<>();
        try (PreparedStatement select = connection.prepareStatement(GET_ALL_NUEVO);
             ResultSet rs = select.executeQuery()) {
            while (rs.next()) list.add(extraerDatos(rs));
            return list;
        }
    }

    public List<Nueva_h_c> getAllNuevoByUsuario(int usuarioId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Nueva_h_c> list = new ArrayList<>();
        try (PreparedStatement select = connection.prepareStatement(GET_ALL_NUEVO_BY_USUARIO)) {
            select.setInt(1, usuarioId);
            try (ResultSet rs = select.executeQuery()) {
                while (rs.next()) list.add(extraerDatos(rs));
                return list;
            }
        }
    }

    private Nueva_h_c extraerDatos(ResultSet rs) throws SQLException {
        Nueva_h_c nuevaHC = new Nueva_h_c(
                rs.getInt("solicitud_id"),
                rs.getInt("usuario_id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                EnumNueva_h_cTipo.valueOf(rs.getString("tipo")),
                EnumNueva_h_cEstado.valueOf(rs.getString("estado")),
                rs.getDate("fecha_creacion").toLocalDate()
        );

        nuevaHC.setNombreCompleto(rs.getString("nombre_completo"));
        nuevaHC.setUserName(rs.getString("user_name"));
        return nuevaHC;
    }

}
