package org.proyecto2.proyecto2.db.proyecto;

import org.proyecto2.proyecto2.db.config.CRUD;
import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.models.categoria.Categoria;
import org.proyecto2.proyecto2.models.proyecto.EnumProyecto;
import org.proyecto2.proyecto2.models.proyecto.Proyecto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProyectoDAO implements CRUD<Proyecto> {
    private static final String INSERT_PROYECTO = "INSERT INTO proyecto(usuario_id, categoria_id, titulo, descripcion, presupuesto, estado, fecha_creacion, fecha_limite) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_PROYECTO = "UPDATE proyecto SET categoria_id = ?, titulo = ?, descripcion = ?, presupuesto = ?, fecha_limite = ? WHERE proyecto_id = ?";
    private static final String UPDATE_PROYECTO_ESTADO = "UPDATE proyecto SET estado = ? WHERE proyecto_id = ?";
    private static final String UPDATE_PROYECTO_ESTADO_CANCELADO = "UPDATE proyecto SET estado = 'CANCELADO' WHERE proyecto_id = ?";
    private static final String GET_ALL_USUARIO_PROYECTO = "SELECT p.*, c.nombre, c.descripcion, c.estado AS estado_categoria FROM proyecto p JOIN categoria c ON p.categoria_id = c.categoria_id WHERE p.usuario_id = ?";
    private static final String GET_ALL_USUARIO_PROYECTO_BY_COINCIDENCE = "SELECT p.*, c.nombre, c.descripcion, c.estado AS estado_categoria FROM proyecto p JOIN categoria c ON p.categoria_id = c.categoria_id WHERE p.titulo like ? AND p.usuario_id = ?";
    private static final String GET_USUARIO_PROYECTO_BY_ID = "SELECT p.*, c.nombre, c.descripcion, c.estado AS estado_categoria FROM proyecto p JOIN categoria c ON p.categoria_id = c.categoria_id WHERE p.proyecto_id = ? AND p.usuario_id = ?";
    private static final String GET_ALL_PROYECTO = "SELECT p.*, c.nombre, c.descripcion, c.estado AS estado_categoria FROM proyecto p JOIN categoria c ON p.categoria_id = c.categoria_id";
    private static final String GET_PROYECTO_BY_ID = "SELECT p.*, c.nombre, c.descripcion, c.estado AS estado_categoria FROM proyecto p JOIN categoria c ON p.categoria_id = c.categoria_id WHERE p.proyecto_id = ?";
    private static final String VALID_PROYECTO_USUARIO = "SELECT 1 FROM proyecto WHERE proyecto_id = ? AND usuario_id = ?";
    private static final String GET_ALL_PROYECTO_BY_CATEGORIA = "SELECT p.*, c.nombre, c.descripcion, c.estado AS estado_categoria FROM proyecto p JOIN categoria c ON p.categoria_id = c.categoria_id WHERE p.categoria_id = ? AND p.estado = 'ABIERTO'";
    private static final String GET_ALL_PROYECTO_BY_PRESUPUESTO = "SELECT p.*, c.nombre, c.descripcion, c.estado AS estado_categoria FROM proyecto p JOIN categoria c ON p.categoria_id = c.categoria_id WHERE p.presupuesto BETWEEN ? AND ? AND p.estado = 'ABIERTO'";
    private static final String GET_ALL_PROYECTO_BY_HABILIDAD = "SELECT p.*, c.nombre, c.descripcion, c.estado AS estado_categoria FROM proyecto p JOIN categoria c ON p.categoria_id = c.categoria_id JOIN proyecto_habilidad ph ON p.proyecto_id = ph.proyecto_id WHERE ph.habilidad_id = ? AND p.estado = 'ABIERTO'";
    private static final String GET_ALL_PROYECTO_IN_ABIERTO_ESTADO = "SELECT p.*, c.nombre, c.descripcion, c.estado AS estado_categoria FROM proyecto p JOIN categoria c ON p.categoria_id = c.categoria_id WHERE p.estado = 'ABIERTO'";

    public boolean existsProyectoUsuario(int proyectoId, int usuarioId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement select = connection.prepareStatement(VALID_PROYECTO_USUARIO)) {
            select.setInt(1, proyectoId);
            select.setInt(2, usuarioId);
            try (ResultSet rs = select.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean existsProyectoUsuario(int proyectoId, int usuarioId, Connection connection) throws SQLException {
        try (PreparedStatement select = connection.prepareStatement(VALID_PROYECTO_USUARIO)) {
            select.setInt(1, proyectoId);
            select.setInt(2, usuarioId);
            try (ResultSet rs = select.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int insert(Proyecto proyecto, Connection connection) throws SQLException {
        try (PreparedStatement insert = connection.prepareStatement(INSERT_PROYECTO, Statement.RETURN_GENERATED_KEYS)) {
            insert.setInt(1, proyecto.getUsuarioId());
            insert.setInt(2, proyecto.getCategoriaId());
            insert.setString(3, proyecto.getTitulo());
            insert.setString(4, proyecto.getDescripcion());
            insert.setDouble(5, proyecto.getPresupuesto());
            insert.setString(6, proyecto.getEstado().name());
            insert.setDate(7, Date.valueOf(proyecto.getFechaCreacion()));
            insert.setDate(8, Date.valueOf(proyecto.getFechaLimite()));
            insert.executeUpdate();

            try (ResultSet generatedKeys = insert.getGeneratedKeys()) {
                if (generatedKeys.next()) return generatedKeys.getInt(1);
                else throw new SQLException("Creating proyecto failed, no ID obtained.");
            }
        }
    }

    @Override
    public void insert(Proyecto proyecto) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement insert = connection.prepareStatement(INSERT_PROYECTO)) {
            insert.setInt(1, proyecto.getUsuarioId());
            insert.setInt(2, proyecto.getCategoriaId());
            insert.setString(3, proyecto.getTitulo());
            insert.setString(4, proyecto.getDescripcion());
            insert.setDouble(5, proyecto.getPresupuesto());
            insert.setString(6, proyecto.getEstado().name());
            insert.setDate(7, Date.valueOf(proyecto.getFechaCreacion()));
            insert.setDate(8, Date.valueOf(proyecto.getFechaLimite()));
            insert.executeUpdate();
        }
    }

    @Override
    public void update(Proyecto proyecto) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement update = connection.prepareStatement(UPDATE_PROYECTO)) {
            update.setInt(1, proyecto.getCategoriaId());
            update.setString(2, proyecto.getTitulo());
            update.setString(3, proyecto.getDescripcion());
            update.setDouble(4, proyecto.getPresupuesto());
            update.setDate(5, Date.valueOf(proyecto.getFechaLimite()));
            update.setInt(6, proyecto.getProyectoId());
            update.executeUpdate();
        }
    }

    public void updateEstado(EnumProyecto estado, int proyectoId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement update = connection.prepareStatement(UPDATE_PROYECTO_ESTADO)) {
            update.setString(1, estado.name());
            update.setInt(2, proyectoId);
            update.executeUpdate();
        }
    }

    public void updateEstado(EnumProyecto estado, int proyectoId, Connection connection) throws SQLException {
        try (PreparedStatement update = connection.prepareStatement(UPDATE_PROYECTO_ESTADO)) {
            update.setString(1, estado.name());
            update.setInt(2, proyectoId);
            update.executeUpdate();
        }
    }

    public void updateEstadoCancelado(int proyectoId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement update = connection.prepareStatement(UPDATE_PROYECTO_ESTADO_CANCELADO)) {
            update.setInt(1, proyectoId);
            update.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public Optional<Proyecto> getById(int id) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement select = connection.prepareStatement(GET_PROYECTO_BY_ID)) {
            select.setInt(1, id);
            try (ResultSet rs = select.executeQuery()) {
                if (rs.next()) return Optional.of(extraerDatos(rs));
                return Optional.empty();
            }

        }
    }

    @Override
    public List<Proyecto> getAll() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Proyecto> proyectos = new ArrayList<>();
        try (PreparedStatement select = connection.prepareStatement(GET_ALL_PROYECTO);
             ResultSet rs = select.executeQuery()) {
            while (rs.next()) proyectos.add(extraerDatos(rs));
            return proyectos;
        }
    }

    public List<Proyecto> getAllUsuarioProyecto(int usuarioId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Proyecto> proyectos = new ArrayList<>();
        try (PreparedStatement select = connection.prepareStatement(GET_ALL_USUARIO_PROYECTO)) {
            select.setInt(1, usuarioId);
            try (ResultSet rs = select.executeQuery()) {
                while (rs.next())
                    proyectos.add(extraerDatos(rs));
                return proyectos;
            }
        }
    }

    public List<Proyecto> getAllUsuarioProyectoByCategoria(int usuarioId, String titulo) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Proyecto> proyectos = new ArrayList<>();
        try (PreparedStatement select = connection.prepareStatement(GET_ALL_USUARIO_PROYECTO_BY_COINCIDENCE)) {
            select.setString(1, "%" + titulo + "%");
            select.setInt(2, usuarioId);
            try (ResultSet rs = select.executeQuery()) {
                while (rs.next()) proyectos.add(extraerDatos(rs));
                return proyectos;
            }
        }
    }

    public Optional<Proyecto> getUsuarioProyectoById(int proyectoId, int usuarioId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement select = connection.prepareStatement(GET_USUARIO_PROYECTO_BY_ID)) {
            select.setInt(1, proyectoId);
            select.setInt(2, usuarioId);
            try (ResultSet rs = select.executeQuery()) {
                if (rs.next()) return Optional.of(extraerDatos(rs));
                return Optional.empty();
            }
        }
    }

    public List<Proyecto> getAllProyectoByCategoria(int categoriaId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Proyecto> proyectos = new ArrayList<>();
        try (PreparedStatement select = connection.prepareStatement(GET_ALL_PROYECTO_BY_CATEGORIA)) {
            select.setInt(1, categoriaId);
            try (ResultSet rs = select.executeQuery()) {
                while (rs.next()) proyectos.add(extraerDatos(rs));
                return proyectos;
            }
        }
    }

    public List<Proyecto> getAllProyectoByPresupuesto(double precioInicial, double precioFinal) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Proyecto> proyectos = new ArrayList<>();
        try (PreparedStatement select = connection.prepareStatement(GET_ALL_PROYECTO_BY_PRESUPUESTO)) {
            select.setDouble(1, precioInicial);
            select.setDouble(2, precioFinal);
            try (ResultSet rs = select.executeQuery()) {
                while (rs.next()) proyectos.add(extraerDatos(rs));
                return proyectos;
            }

        }
    }

    public List<Proyecto> getAllProyectoByHabilidad(int habilidadId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Proyecto> proyectos = new ArrayList<>();
        try (PreparedStatement select = connection.prepareStatement(GET_ALL_PROYECTO_BY_HABILIDAD)) {
            select.setInt(1, habilidadId);
            try (ResultSet rs = select.executeQuery()) {
                while (rs.next()) proyectos.add(extraerDatos(rs));
                return proyectos;
            }
        }
    }

    public List<Proyecto> getAllProyectoInAbiertoEstado() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Proyecto> proyectos = new ArrayList<>();
        try (PreparedStatement select = connection.prepareStatement(GET_ALL_PROYECTO_IN_ABIERTO_ESTADO);
             ResultSet rs = select.executeQuery()) {
            while (rs.next()) proyectos.add(extraerDatos(rs));
            return proyectos;
        }
    }

    private Proyecto extraerDatos(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria(rs.getInt("categoria_id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getBoolean("estado_categoria"));


        return new Proyecto(rs.getInt("proyecto_id"),
                rs.getInt("usuario_id"),
                rs.getInt("categoria_id"),
                rs.getString("titulo"),
                rs.getString("descripcion"),
                rs.getDouble("presupuesto"),
                EnumProyecto.valueOf(rs.getString("estado")),
                rs.getDate("fecha_creacion").toLocalDate(),
                rs.getDate("fecha_limite").toLocalDate()
                , categoria);
    }
}
