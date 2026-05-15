package org.proyecto2.proyecto2.db.propuesta;

import org.proyecto2.proyecto2.db.config.CRUD;
import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.models.propuesta.EnumPropuesta;
import org.proyecto2.proyecto2.models.propuesta.Propuesta;
import org.proyecto2.proyecto2.models.propuesta.PropuestaDetalle;
import org.proyecto2.proyecto2.models.propuesta.PropuestaHistorial;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class PropuestaDAO implements CRUD<Propuesta> {
    private static final String INSERT_PROPUESTA = "INSERT INTO propuesta(proyecto_id, usuario_id, monto, tiempo_entrega, descripcion, fecha_creacion, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_PROPUESTA = "UPDATE propuesta SET monto = ?, tiempo_entrega = ?, descripcion = ? WHERE propuesta_id = ?";
    private static final String UPDATE_PROPUESTA_ESTADO = "UPDATE propuesta SET estado = ? WHERE propuesta_id = ?";
    private static final String EXISTS_PROPUESTA = "SELECT 1 FROM propuesta WHERE propuesta_id = ? AND usuario_id = ? AND estado <> 'RETIRADO'";
    private static final String EXISTS_PROPUESTA_FREELANCER = "SELECT 1 FROM propuesta WHERE proyecto_id = ? AND usuario_id = ? AND estado <> 'RETIRADO'";
    private static final String GET_BY_ID_PROPUESTA = "SELECT * FROM propuesta WHERE propuesta_id = ?";
    private static final String GET_ALL_PROPUESTA_FOR_A_PROYECTO = "SELECT p.propuesta_id, p.proyecto_id, p.monto, p.tiempo_entrega, p.descripcion, p.estado, p.fecha_creacion, u.usuario_id, u.nombre_completo, u.user_name, COALESCE(cal.promedio_calificacion, 0) AS promedio_calificacion, COALESCE(cal.total_calificaciones, 0) AS total_calificaciones FROM propuesta p INNER JOIN usuario u ON p.usuario_id = u.usuario_id LEFT JOIN (SELECT p2.usuario_id, COUNT(c.calificacion) AS total_calificaciones, AVG(c.calificacion) AS promedio_calificacion FROM propuesta p2 INNER JOIN contrato c ON p2.propuesta_id = c.propuesta_id GROUP BY p2.usuario_id) cal ON p.usuario_id = cal.usuario_id JOIN proyecto p3 on p.proyecto_id = p3.proyecto_id WHERE p.proyecto_id = ? AND p3.usuario_id = ?";
    private static final String GET_ALL_PROPUESTA_FROM_A_FREELANCER = "SELECT * FROM propuesta WHERE usuario_id = ? AND proyecto_id = ?";
    private static final String RECHAZAR_DEMAS_PROPUESTAS = "UPDATE propuesta SET estado = 'RECHAZADA' WHERE proyecto_id = ? AND propuesta_id <> ? AND estado = 'PENDIENTE'";
    private static final String EXISTS_PROPUESTA_EN_PROYECTO = "SELECT 1 FROM propuesta WHERE proyecto_id = ? AND estado <> 'RETIRADO'";

    public boolean existsPropuesta(int propuestaId, int usuarioId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement select = connection.prepareStatement(EXISTS_PROPUESTA)) {
            select.setInt(1, propuestaId);
            select.setInt(2, usuarioId);
            try (ResultSet rs = select.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existsPropuestaEnProyecto(int proyectoId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement select = connection.prepareStatement(EXISTS_PROPUESTA_EN_PROYECTO)) {
            select.setInt(1, proyectoId);
            try (ResultSet rs = select.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existsPropuestaFreelancer(int proyectoId, int usuarioId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement select = connection.prepareStatement(EXISTS_PROPUESTA_FREELANCER)) {
            select.setInt(1, proyectoId);
            select.setInt(2, usuarioId);
            try (ResultSet rs = select.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public void insert(Propuesta propuesta) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement insert = connection.prepareStatement(INSERT_PROPUESTA)) {
            insert.setInt(1, propuesta.getProyectoId());
            insert.setInt(2, propuesta.getUsuarioId());
            insert.setDouble(3, propuesta.getMonto());
            insert.setInt(4, propuesta.getTiempoEntrega());
            insert.setString(5, propuesta.getDescripcion());
            insert.setDate(6, Date.valueOf(propuesta.getFechaCreacion()));
            insert.setString(7, propuesta.getEstado().name());
            insert.executeUpdate();
        }

    }

    @Override
    public void update(Propuesta propuesta) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement update = connection.prepareStatement(UPDATE_PROPUESTA)) {
            update.setDouble(1, propuesta.getMonto());
            update.setInt(2, propuesta.getTiempoEntrega());
            update.setString(3, propuesta.getDescripcion());
            update.setInt(4, propuesta.getPropuestaId());
            update.executeUpdate();
        }
    }

    public void updateEstado(EnumPropuesta estado, int propuestaId, Connection connection) throws SQLException {
        try (PreparedStatement update = connection.prepareStatement(UPDATE_PROPUESTA_ESTADO)) {
            update.setString(1, estado.name());
            update.setInt(2, propuestaId);
            update.executeUpdate();
        }
    }

    public void rechazarDemasPropuestas(int proyectoId, int propuestaId, Connection connection) throws SQLException {
        try (PreparedStatement update = connection.prepareStatement(RECHAZAR_DEMAS_PROPUESTAS)) {
            update.setInt(1, proyectoId);
            update.setInt(2, propuestaId);
            update.executeUpdate();
        }
    }

    public void updateEstado(EnumPropuesta estado, int propuestaId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement update = connection.prepareStatement(UPDATE_PROPUESTA_ESTADO)) {
            update.setString(1, estado.name());
            update.setInt(2, propuestaId);
            update.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public Optional<Propuesta> getById(int id) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement select = connection.prepareStatement(GET_BY_ID_PROPUESTA)) {
            select.setInt(1, id);
            try (ResultSet rs = select.executeQuery()) {
                if (rs.next()) return Optional.of(extraerDatos(rs));
                return Optional.empty();
            }
        }
    }

    public Optional<Propuesta> getById(int id, Connection connection) throws SQLException {
        try (PreparedStatement select = connection.prepareStatement(GET_BY_ID_PROPUESTA)) {
            select.setInt(1, id);
            try (ResultSet rs = select.executeQuery()) {
                if (rs.next()) return Optional.of(extraerDatos(rs));
                return Optional.empty();
            }
        }
    }

    public List<Propuesta> getAllPropuestaFromAFreelancer(int usuarioId, int proyectoId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement select = connection.prepareStatement(GET_ALL_PROPUESTA_FROM_A_FREELANCER)) {
            select.setInt(1, usuarioId);
            select.setInt(2, proyectoId);
            try (ResultSet rs = select.executeQuery()) {
                List<Propuesta> propuestas = new java.util.ArrayList<>();
                while (rs.next())
                    propuestas.add(extraerDatos(rs));
                return propuestas;
            }
        }
    }

    public List<PropuestaDetalle> getAllPropuestaForAProyecto(int proyectoId, int usuarioId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement select = connection.prepareStatement(GET_ALL_PROPUESTA_FOR_A_PROYECTO)) {
            select.setInt(1, proyectoId);
            select.setInt(2, usuarioId);
            try (ResultSet rs = select.executeQuery()) {
                List<PropuestaDetalle> propuestas = new java.util.ArrayList<>();
                while (rs.next()) {
                    propuestas.add(new PropuestaDetalle(
                            rs.getInt("propuesta_id"),
                            EnumPropuesta.valueOf(rs.getString("estado")),
                            rs.getInt("proyecto_id"),
                            rs.getInt("usuario_id"),
                            rs.getDouble("monto"),
                            rs.getInt("tiempo_entrega"),
                            rs.getString("descripcion"),
                            rs.getDate("fecha_creacion").toLocalDate(),
                            rs.getString("nombre_completo"),
                            rs.getString("user_name"),
                            rs.getDouble("promedio_calificacion"),
                            rs.getInt("total_calificaciones")
                    ));
                }
                return propuestas;
            }
        }
    }

    public List<PropuestaHistorial> getHistorialPropuestasFreelancer(int usuarioId, String estadoFiltro) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        StringBuilder query = new StringBuilder("SELECT p.*, pr.titulo AS proyecto_titulo FROM propuesta p INNER JOIN proyecto pr ON p.proyecto_id = pr.proyecto_id WHERE p.usuario_id = ?");
        boolean tieneFiltro = estadoFiltro != null && !estadoFiltro.equalsIgnoreCase("TODAS");
        if (tieneFiltro) {
            query.append(" AND p.estado = ? ");
        }
        query.append(" ORDER BY p.fecha_creacion DESC");
        try (PreparedStatement select = connection.prepareStatement(query.toString())) {
            select.setInt(1, usuarioId);
            if (tieneFiltro) {
                select.setString(2, estadoFiltro);
            }
            try (ResultSet rs = select.executeQuery()) {
                List<PropuestaHistorial> propuestas = new java.util.ArrayList<>();
                while (rs.next()) {
                    propuestas.add(extraerDatosHistorial(rs));
                }
                return propuestas;
            }
        }
    }
    private PropuestaHistorial extraerDatosHistorial(ResultSet rs) throws SQLException {
        return new PropuestaHistorial(
                rs.getInt("propuesta_id"),
                rs.getInt("proyecto_id"),
                rs.getString("proyecto_titulo"), // 🔥 Obtenido del JOIN
                rs.getDouble("monto"),
                rs.getInt("tiempo_entrega"),
                rs.getString("descripcion"),
                EnumPropuesta.valueOf(rs.getString("estado")),
                rs.getDate("fecha_creacion").toLocalDate()
        );
    }

    @Override
    public List<Propuesta> getAll() throws SQLException {
        return List.of();
    }

    private Propuesta extraerDatos(ResultSet rs) throws SQLException {
        return new Propuesta(
                rs.getInt("propuesta_id"),
                rs.getInt("proyecto_id"),
                rs.getInt("usuario_id"),
                rs.getDouble("monto"),
                rs.getInt("tiempo_entrega"),
                rs.getString("descripcion"),
                EnumPropuesta.valueOf(rs.getString("estado")),
                rs.getDate("fecha_creacion").toLocalDate()
        );
    }
}
