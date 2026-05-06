package org.proyecto2.proyecto2.services.proyecto;

import org.proyecto2.proyecto2.db.proyecto.ProyectoDAO;
import org.proyecto2.proyecto2.dtos.proyecto.ProyectoRequest;
import org.proyecto2.proyecto2.dtos.proyecto.ProyectoUpdate;
import org.proyecto2.proyecto2.exceptions.EntityAlreadyExistsException;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.proyecto.EnumProyecto;
import org.proyecto2.proyecto2.models.proyecto.Proyecto;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProyectoService {
    public void createProyecto(ProyectoRequest proyectoRequest, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException("El usuario no tiene permisos para crear un proyecto.");
        Proyecto proyecto = new Proyecto(proyectoRequest);
        if (!proyecto.isValid()) throw new UserDataInvalidException("Los datos del proyecto no son válidos.");
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        proyectoDAO.insert(proyecto);
    }

    public void updateProyecto(ProyectoUpdate proyectoUpdate, EnumUsuario rol, int usuarioId) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException("El usuario no tiene permisos para actualizar un proyecto.");
        Proyecto proyecto = new Proyecto(proyectoUpdate);
        if (!proyecto.isValidUpdate()) throw new UserDataInvalidException("Los datos del proyecto no son válidos.");
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        if (!proyectoDAO.existsProyecto(proyecto.getProyectoId(), usuarioId))
            throw new UserDataInvalidException("El proyecto no existe o no pertenece al usuario.");
        proyectoDAO.update(proyecto);
    }

    public void updateProyectoEstado(EnumProyecto estado, int usuarioId, int proyectoId, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException("El usuario no tiene permisos para actualizar el estado de un proyecto.");
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        if (!proyectoDAO.existsProyecto(proyectoId, usuarioId))
            throw new UserDataInvalidException("El proyecto no existe o no pertenece al usuario.");
        proyectoDAO.updateEstado(estado, proyectoId);
    }

    public Proyecto getById(int proyectoId) throws SQLException, UserDataInvalidException {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        Optional<Proyecto> proyecto = proyectoDAO.getById(proyectoId);
        if (proyecto.isEmpty()) throw new UserDataInvalidException("El proyecto no existe.");
        return proyecto.get();
    }

    public List<Proyecto> getAll() throws SQLException {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        return proyectoDAO.getAll();
    }

    public List<Proyecto> getAllUsuarioProyecto(int usuarioId) throws SQLException {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        return proyectoDAO.getAllUsuarioProyecto(usuarioId);
    }

    public List<Proyecto> getAllUsuarioProyectoByCoincidence(int usuarioId, String titulo) throws SQLException {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        return proyectoDAO.getAllUsuarioProyectoByCategoria(usuarioId, titulo);
    }

    public Proyecto getUsuarioProyectoById(int usuarioId, int proyectoId) throws SQLException, UserDataInvalidException {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        Optional<Proyecto> proyecto = proyectoDAO.getUsuarioProyectoById(usuarioId, proyectoId);
        if (proyecto.isEmpty()) throw new UserDataInvalidException("El proyecto no existe o no pertenece al usuario.");
        return proyecto.get();
    }
}
