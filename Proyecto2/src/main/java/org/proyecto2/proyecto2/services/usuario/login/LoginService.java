package org.proyecto2.proyecto2.services.usuario.login;

import org.proyecto2.proyecto2.db.usuario.UsuarioDAO;
import org.proyecto2.proyecto2.dtos.usuario.login.LoginRequest;
import org.proyecto2.proyecto2.dtos.usuario.login.LoginResponse;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.models.usuario.Usuario;
import org.proyecto2.proyecto2.models.usuario.login.Login;
import org.proyecto2.proyecto2.services.usuario.cliente.ClienteService;
import org.proyecto2.proyecto2.services.usuario.freelancer.FreelancerService;
import org.proyecto2.proyecto2.utils.JwtUtil;

import java.sql.SQLException;
import java.util.Optional;

public class LoginService {

    public LoginResponse login(LoginRequest loginRequest) throws SQLException, UserDataInvalidException {
        Login login = new Login(loginRequest);
        if (!login.isValid()) throw new UserDataInvalidException("Username o email y contreseña son requeridos");
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Optional<Usuario> usuarioOptional = usuarioDAO.login(login);
        if (usuarioOptional.isEmpty()) throw new UserDataInvalidException("Credenciales incorrectas");
        Usuario usuario = usuarioOptional.get();
        String token = JwtUtil.generarToken(usuario);
        if (EnumUsuario.Cliente.equals(usuario.getRol())) {
            ClienteService clienteService = new ClienteService();
            usuario.setCliente(clienteService.getComplemento(usuario.getUsuarioId()).orElse(null));
        } else if (EnumUsuario.Freelancer.equals(usuario.getRol())) {
            FreelancerService freelancerService = new FreelancerService();
            usuario.setFreelancer(freelancerService.getComplemento(usuario.getUsuarioId()).orElse(null));
        }
        return new LoginResponse(token, usuario);
    }
}
