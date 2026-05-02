package org.proyecto2.proyecto2.services.usuario.login;

import org.proyecto2.proyecto2.db.usuario.UsuarioDAO;
import org.proyecto2.proyecto2.dtos.usuario.login.LoginRequest;
import org.proyecto2.proyecto2.dtos.usuario.login.LoginResponse;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.usuario.Usuario;
import org.proyecto2.proyecto2.models.usuario.login.Login;
import org.proyecto2.proyecto2.utils.JwtUtil;

import java.sql.SQLException;
import java.util.Optional;

public class LoginService {

    public LoginResponse login(LoginRequest loginRequest) throws SQLException, UserDataInvalidException {
        Login login = new Login(loginRequest);
        if (!login.isValid()) throw new UserDataInvalidException("Username o email y contreseña son requeridos");
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Optional<Usuario> usuario = usuarioDAO.login(login);
        if (usuario.isEmpty()) throw new UserDataInvalidException("Credenciales incorrectas");
        String token = JwtUtil.generarToken(usuario.get());
        return new LoginResponse(token, usuario.get());
    }

}
