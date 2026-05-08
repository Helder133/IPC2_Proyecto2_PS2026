package org.proyecto2.proyecto2.services.proyecto;

import org.proyecto2.proyecto2.db.proyecto.ProyectoHabilidadDAO;
import org.proyecto2.proyecto2.dtos.proyecto.ProyectoHabilidadRequest;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.habilidad.Habilidad;
import org.proyecto2.proyecto2.models.proyecto.ProyectoHabilidad;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProyectoHabilidadService {
    public void insertProyectoHabilidad(ProyectoHabilidadRequest proyectoHabilidadRequest, EnumUsuario rol, int usuarioId, int proyectoId) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Cliente.equals(rol)) {
            throw new UserDataInvalidException("El usuario no tiene permisos para agregar habilidades a un proyecto.");
        }
        ProyectoService proyectoService = new ProyectoService();
        if (!proyectoService.ValidProyectoUsuario(usuarioId, proyectoId)) {
            throw new UserDataInvalidException("El proyecto no existe o no pertenece al usuario.");
        }
        ProyectoHabilidad proyectoHabilidad = new ProyectoHabilidad(proyectoId, proyectoHabilidadRequest);
        ProyectoHabilidadDAO proyectoHabilidadDAO = new ProyectoHabilidadDAO();
        if (proyectoHabilidadDAO.validProyectoHabilidad(proyectoHabilidad)) {
            throw new UserDataInvalidException("La habilidad ya está asociada al proyecto.");
        }
        proyectoHabilidadDAO.insert(proyectoHabilidad);
    }

    public void insertProyectoHabilidad(ProyectoHabilidadRequest proyectoHabilidadRequest, EnumUsuario rol, int usuarioId, int proyectoId, Connection connection) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Cliente.equals(rol)) {
            throw new UserDataInvalidException("El usuario no tiene permisos para agregar habilidades a un proyecto.");
        }
        ProyectoService proyectoService = new ProyectoService();
        if (!proyectoService.ValidProyectoUsuario(usuarioId, proyectoId, connection)) {
            throw new UserDataInvalidException("El proyecto no existe o no pertenece al usuario.");
        }
        ProyectoHabilidad proyectoHabilidad = new ProyectoHabilidad(proyectoId, proyectoHabilidadRequest);
        ProyectoHabilidadDAO proyectoHabilidadDAO = new ProyectoHabilidadDAO();
        if (proyectoHabilidadDAO.validProyectoHabilidad(proyectoHabilidad)) {
            throw new UserDataInvalidException("La habilidad ya está asociada al proyecto.");
        }
        proyectoHabilidadDAO.insert(proyectoHabilidad, connection);
    }

    public void delete(int habilidadId, EnumUsuario rol, int usuarioId, int proyectoId) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Cliente.equals(rol)) {
            throw new UserDataInvalidException("El usuario no tiene permisos para eliminar habilidades de un proyecto.");
        }
        ProyectoService proyectoService = new ProyectoService();
        if (!proyectoService.ValidProyectoUsuario(usuarioId, proyectoId)) {
            throw new UserDataInvalidException("El proyecto no existe o no pertenece al usuario.");
        }
        ProyectoHabilidad proyectoHabilidad = new ProyectoHabilidad(proyectoId, habilidadId);
        ProyectoHabilidadDAO proyectoHabilidadDAO = new ProyectoHabilidadDAO();
        if (!proyectoHabilidadDAO.validProyectoHabilidad(proyectoHabilidad)) {
            throw new UserDataInvalidException("La habilidad no está asociada al proyecto.");
        }
        proyectoHabilidadDAO.delete(proyectoHabilidad);
    }

    public List<Habilidad> getAllHabilidadesByProyecto(int proyectoId, EnumUsuario rol, int usuarioId) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException("El usuario no tiene permisos para ver las habilidades de un proyecto.");
        ProyectoService proyectoService = new ProyectoService();
        if (!proyectoService.ValidProyectoUsuario(usuarioId, proyectoId))
            throw new UserDataInvalidException("El proyecto no existe o no pertenece al usuario.");
        ProyectoHabilidadDAO proyectoHabilidadDAO = new ProyectoHabilidadDAO();
        return proyectoHabilidadDAO.getAllHabilidadByProyecto(proyectoId);
    }

    public List<Habilidad> getAllHabilidadesByProyecto(int proyectoId) throws SQLException, UserDataInvalidException {
        ProyectoHabilidadDAO proyectoHabilidadDAO = new ProyectoHabilidadDAO();
        return proyectoHabilidadDAO.getAllHabilidadByProyecto(proyectoId);
    }
}
