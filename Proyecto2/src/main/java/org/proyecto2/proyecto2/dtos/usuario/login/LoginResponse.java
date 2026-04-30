package org.proyecto2.proyecto2.dtos.usuario.login;

import org.proyecto2.proyecto2.dtos.usuario.UsuarioResponse;
import org.proyecto2.proyecto2.models.usuario.Usuario;

public class LoginResponse {
    private String token;
    private UsuarioResponse usuario;

    public LoginResponse(String token, Usuario usuario) {
        this.token = token;
        this.usuario = new UsuarioResponse(usuario);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UsuarioResponse getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioResponse usuario) {
        this.usuario = usuario;
    }
}
