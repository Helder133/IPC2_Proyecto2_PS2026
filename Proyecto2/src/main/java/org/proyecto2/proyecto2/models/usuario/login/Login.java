package org.proyecto2.proyecto2.models.usuario.login;

import org.apache.commons.lang3.StringUtils;
import org.proyecto2.proyecto2.dtos.usuario.login.LoginRequest;
import org.proyecto2.proyecto2.utils.HashUtil;

public class Login {
    private String username;
    private String password;

    public Login(LoginRequest loginRequest) {
        this.username = loginRequest.getUsername();
        this.password = HashUtil.sha256(loginRequest.getPassword());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValid() {
        return StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password);
    }
}
