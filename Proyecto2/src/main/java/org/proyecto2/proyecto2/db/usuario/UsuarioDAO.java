package org.proyecto2.proyecto2.db.usuario;

import org.proyecto2.proyecto2.db.config.CRUD;
import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.models.usuario.Usuario;
import org.proyecto2.proyecto2.models.usuario.login.Login;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAO implements CRUD<Usuario> {
    private static final String LOGIN = "SELECT * FROM usuario WHERE (user_name = ? OR email = ?) AND password = ? AND estado = 1";
    private static final String INSERT_USUARIO = "INSERT INTO usuario (nombre_completo,user_name,password,email,telefono,direccion,cui,fecha_nacimiento,rol) VALUES (?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_USUARIO = "UPDATE usuario SET nombre_completo = ?, user_name = ?, password = COALESCE(NULLIF(TRIM(?), ''), password), email = ?, telefono = ?, direccion = ?, cui = ?, fecha_nacimiento = ? WHERE usuario_id = ?";
    private static final String UPDATE_ESTADO = "UPDATE usuario SET estado = NOT estado WHERE usuario_id = ?";
    private static final String GET_USUARIO = "SELECT * FROM usuario WHERE usuario_id = ?";
    private static final String GET_ALL_USUARIOS = "SELECT * FROM usuario";
    private static final String VALID_USERNAME = "SELECT usuario_id FROM usuario WHERE user_name = ?";
    private static final String VALID_EMAIL = "SELECT usuario_id FROM usuario WHERE email = ?";
    private static final String VALID_TELEFONO = "SELECT usuario_id FROM usuario WHERE telefono = ?";
    private static final String VALID_CUI = "SELECT usuario_id FROM usuario WHERE cui = ?";
    private static final String VALID_USERNAME_UPDATE = "SELECT usuario_id FROM usuario WHERE user_name = ? AND usuario_id <> ?";
    private static final String VALID_EMAIL_UPDATE = "SELECT usuario_id FROM usuario WHERE email = ? AND usuario_id <> ?";
    private static final String VALID_TELEFONO_UPDATE = "SELECT usuario_id FROM usuario WHERE telefono = ? AND usuario_id <> ?";
    private static final String VALID_CUI_UPDATE = "SELECT usuario_id FROM usuario WHERE cui = ? AND usuario_id <> ?";

    public Optional<Usuario> login(Login login) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(LOGIN)) {
            statement.setString(1, login.getUsername());
            statement.setString(2, login.getUsername());
            statement.setString(3, login.getPassword());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) return Optional.of(extraerDatos(resultSet));
                return Optional.empty();
            }
        }
    }

    public boolean validUsername(String username) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(VALID_USERNAME)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean validEmail(String email) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(VALID_EMAIL)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean validTelefono(String telefono) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(VALID_TELEFONO)) {
            statement.setString(1, telefono);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean validCUI(String cui) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(VALID_CUI)) {
            statement.setString(1, cui);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean validUsernameUpdate(String username, int usuarioId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(VALID_USERNAME_UPDATE)) {
            statement.setString(1, username);
            statement.setInt(2, usuarioId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean validEmailUpdate(String email, int usuarioId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(VALID_EMAIL_UPDATE)) {
            statement.setString(1, email);
            statement.setInt(2, usuarioId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean validTelefonoUpdate(String telefono, int usuarioId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(VALID_TELEFONO_UPDATE)) {
            statement.setString(1, telefono);
            statement.setInt(2, usuarioId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public boolean validCUIUpdate(String cui, int usuarioId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(VALID_CUI_UPDATE)) {
            statement.setString(1, cui);
            statement.setInt(2, usuarioId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public void insert(Usuario usuario) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT_USUARIO)) {
            statement.setString(1, usuario.getNombreCompleto());
            statement.setString(2, usuario.getUserName());
            statement.setString(3, usuario.getPassword());
            statement.setString(4, usuario.getEmail());
            statement.setString(5, usuario.getTelefono());
            statement.setString(6, usuario.getDireccion());
            statement.setString(7, usuario.getCui());
            statement.setDate(8, Date.valueOf(usuario.getFechaNacimiento()));
            statement.setString(9, usuario.getRol().name());
            statement.execute();
        }
    }

    @Override
    public void update(Usuario usuario) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_USUARIO)) {
            statement.setString(1, usuario.getNombreCompleto());
            statement.setString(2, usuario.getUserName());
            statement.setString(3, usuario.getPassword());
            statement.setString(4, usuario.getEmail());
            statement.setString(5, usuario.getTelefono());
            statement.setString(6, usuario.getDireccion());
            statement.setString(7, usuario.getCui());
            statement.setDate(8, Date.valueOf(usuario.getFechaNacimiento()));
            statement.setInt(9, usuario.getUsuarioId());
            statement.execute();
        }
    }

    public void updateEstado(int usuarioId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_ESTADO)) {
            statement.setInt(1, usuarioId);
            statement.execute();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
    }

    @Override
    public Optional<Usuario> getById(int id) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(GET_USUARIO)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) return Optional.of(extraerDatos(resultSet));
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Usuario> getAll() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Usuario> usuarios = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(GET_ALL_USUARIOS);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) usuarios.add(extraerDatos(resultSet));
            return usuarios;
        }
    }

    public Usuario extraerDatos(ResultSet resultSet) throws SQLException {
        Usuario usuario = new Usuario(resultSet.getString("nombre_completo"),
                resultSet.getString("user_name"),
                "",
                resultSet.getString("email"),
                resultSet.getString("telefono"),
                resultSet.getString("direccion"),
                resultSet.getString("cui"),
                EnumUsuario.valueOf(resultSet.getString("rol")),
                resultSet.getDate("fecha_nacimiento").toLocalDate());
        usuario.setUsuarioId(resultSet.getInt("usuario_id"));
        usuario.setEstado(resultSet.getBoolean("estado"));
        return usuario;
    }

}
