package org.proyecto2.proyecto2.services.configuracionSistema;

import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.db.configuracionSistema.ConfiguracionSistemaDAO;
import org.proyecto2.proyecto2.dtos.configuracionSistema.ConfiguracionSistemaRequest;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.configuracionSistema.ConfiguracionSistema;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ConfiguracionSistemaService {
    public void insert(ConfiguracionSistemaRequest configuracionSistemaRequest, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("Solo los administradores pueden modificar la configuración del sistema");
        ConfiguracionSistema configuracionSistema = new ConfiguracionSistema(configuracionSistemaRequest);
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        ConfiguracionSistemaDAO configuracionSistemaDAO = new ConfiguracionSistemaDAO();
        try {
            int ultimaConfiguracion = configuracionSistemaDAO.getUltimaConfiguracionId(connection);
            configuracionSistemaDAO.update(configuracionSistema.getFechaInicio(), ultimaConfiguracion, connection);
            configuracionSistemaDAO.insert(configuracionSistema, connection);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public ConfiguracionSistema getUltimaConfiguracionSistema(EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("No tienes permiso para ver la configuración del sistema");
        ConfiguracionSistemaDAO configuracionSistemaDAO = new ConfiguracionSistemaDAO();
        return configuracionSistemaDAO.getUltimaConfiguracion().orElseThrow(() -> new UserDataInvalidException("El sistema todavía no cuenta con una configuración"));
    }

    public ConfiguracionSistema getUltimaConfiguracionSistema(Connection connection) throws SQLException, UserDataInvalidException {
        ConfiguracionSistemaDAO configuracionSistemaDAO = new ConfiguracionSistemaDAO();
        return configuracionSistemaDAO.getUltimaConfiguracion(connection).orElseThrow(() -> new UserDataInvalidException("El sistema todavía no cuenta con una configuración"));
    }

    public List<ConfiguracionSistema> getAllConfiguracionSistema(EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("No tienes permiso para ver la configuración del sistema");
        ConfiguracionSistemaDAO configuracionSistemaDAO = new ConfiguracionSistemaDAO();
        return configuracionSistemaDAO.getAllConfiguracionSistema();
    }
}
