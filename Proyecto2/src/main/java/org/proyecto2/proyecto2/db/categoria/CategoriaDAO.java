package org.proyecto2.proyecto2.db.categoria;

import org.proyecto2.proyecto2.db.config.CRUD;
import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.models.categoria.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriaDAO implements CRUD<Categoria> {
    private static final String INSERT_CATEGORIA = "INSERT INTO categoria(nombre, descripcion) VALUES (?, ?)";
    private static final String UPDATE_CATEGORIA = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE categoria_id = ?";
    private static final String UPDATE_CATEGORIA_ESTADO = "UPDATE categoria SET estado = NOT estado WHERE categoria_id = ?";
    private static final String GET_ALL_CATEGORIA = "SELECT * FROM categoria";
    private static final String GET_ALL_CATEGORIA_BY_COINCIDENCE = "SELECT * FROM categoria WHERE nombre LIKE ?";
    private static final String GET_CATEGORIA_BY_ID = "SELECT * FROM categoria WHERE categoria_id = ?";
    private static final String GET_ALL_CATEGORIA_ACTIVADA = "SELECT * FROM categoria WHERE estado = 1";
    private static final String VALID_NOMBRE = "SELECT 1 FROM categoria WHERE nombre = ?";
    private static final String VALID_NOMBRE_UPDATE = "SELECT 1 FROM categoria WHERE nombre = ? AND categoria_id <> ?";

    public boolean validNombre(String nombre) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement validNombre = connection.prepareStatement(VALID_NOMBRE)) {
            validNombre.setString(1, nombre);
            try (ResultSet rs = validNombre.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean validNombre(String nombre, Connection connection) throws SQLException {
        try (PreparedStatement validNombre = connection.prepareStatement(VALID_NOMBRE)) {
            validNombre.setString(1, nombre);
            try (ResultSet rs = validNombre.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean validNombreUpdate(String nombre, int categoriaId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement validNombreUpdate = connection.prepareStatement(VALID_NOMBRE_UPDATE)) {
            validNombreUpdate.setString(1, nombre);
            validNombreUpdate.setInt(2, categoriaId);
            try (ResultSet rs = validNombreUpdate.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public void insert(Categoria categoria) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement insertCategoria = connection.prepareStatement(INSERT_CATEGORIA)) {
            insertCategoria.setString(1, categoria.getNombre());
            insertCategoria.setString(2, categoria.getDescripcion());
            insertCategoria.executeUpdate();
        }
    }

    public void insert(Categoria categoria, Connection connection) throws SQLException {
        try (PreparedStatement insertCategoria = connection.prepareStatement(INSERT_CATEGORIA)) {
            insertCategoria.setString(1, categoria.getNombre());
            insertCategoria.setString(2, categoria.getDescripcion());
            insertCategoria.executeUpdate();
        }
    }

    @Override
    public void update(Categoria categoria) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement updateCategoria = connection.prepareStatement(UPDATE_CATEGORIA)) {
            updateCategoria.setString(1, categoria.getNombre());
            updateCategoria.setString(2, categoria.getDescripcion());
            updateCategoria.setInt(3, categoria.getCategoriaId());
            updateCategoria.executeUpdate();
        }
    }

    public void updateEstado(int categoriaId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement updateEstado = connection.prepareStatement(UPDATE_CATEGORIA_ESTADO)) {
            updateEstado.setInt(1, categoriaId);
            updateEstado.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public Optional<Categoria> getById(int id) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement getCategoriaById = connection.prepareStatement(GET_CATEGORIA_BY_ID)) {
            getCategoriaById.setInt(1, id);
            try (ResultSet rs = getCategoriaById.executeQuery()) {
                if (rs.next()) return Optional.of(extraerDatos(rs));
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Categoria> getAll() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Categoria> categorias = new ArrayList<>();
        try (PreparedStatement getAllCategorias = connection.prepareStatement(GET_ALL_CATEGORIA);
             ResultSet rs = getAllCategorias.executeQuery()) {
            while (rs.next()) categorias.add(extraerDatos(rs));
            return categorias;
        }
    }

    public List<Categoria> getCategoriaByCoincidence(String nombre) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Categoria> categorias = new ArrayList<>();
        try (PreparedStatement getCategoriaByCoincidence = connection.prepareStatement(GET_ALL_CATEGORIA_BY_COINCIDENCE)) {
            getCategoriaByCoincidence.setString(1, "%" + nombre + "%");
            try (ResultSet rs = getCategoriaByCoincidence.executeQuery()) {
                while (rs.next()) categorias.add(extraerDatos(rs));
                return categorias;
            }
        }
    }

    public List<Categoria> getAllCategoriaActivada() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Categoria> categorias = new ArrayList<>();
        try (PreparedStatement getAllCategoriaActivada = connection.prepareStatement(GET_ALL_CATEGORIA_ACTIVADA);
             ResultSet rs = getAllCategoriaActivada.executeQuery()) {
            while (rs.next()) categorias.add(extraerDatos(rs));
            return categorias;
        }
    }

    private Categoria extraerDatos(ResultSet rs) throws SQLException {
        return new Categoria(
                rs.getInt("categoria_id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getBoolean("estado")
        );
    }
}
