package org.proyecto2.proyecto2.services.habilidad;

import org.proyecto2.proyecto2.db.habilidad.HabilidadDAO;
import org.proyecto2.proyecto2.dtos.habilidad.HabilidadRequest;
import org.proyecto2.proyecto2.dtos.habilidad.HabilidadUpdate;
import org.proyecto2.proyecto2.exceptions.EntityAlreadyExistsException;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.habilidad.Habilidad;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class HabilidadService {
    public void createHabilidad(HabilidadRequest habilidadRequest, EnumUsuario rol) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        if (!EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("No tienes permisos para crear una habilidad");
        Habilidad habilidad = new Habilidad(habilidadRequest);
        if (!habilidad.isValid()) throw new UserDataInvalidException("Los datos de la habilidad no son válidos");
        HabilidadDAO habilidadDAO = new HabilidadDAO();
        if (habilidadDAO.validNombre(habilidad.getNombre()))
            throw new EntityAlreadyExistsException("Ya existe una habilidad con el mismo nombre");
        habilidadDAO.insert(habilidad);
    }

    public void createHabilidad(Habilidad habilidad, Connection connection) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        if (!habilidad.isValid()) throw new UserDataInvalidException("Los datos de la habilidad no son válidos");
        HabilidadDAO habilidadDAO = new HabilidadDAO();
        if (validNombre(habilidad.getNombre(), connection))
            throw new EntityAlreadyExistsException("Ya existe una habilidad con el mismo nombre");
        habilidadDAO.insert(habilidad, connection);
    }

    public void updateHabilidad(HabilidadUpdate habilidadUpdate, EnumUsuario rol) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        if (!EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("No tienes permisos para actualizar una habilidad");
        Habilidad habilidad = new Habilidad(habilidadUpdate);
        if (!habilidad.isValid()) throw new UserDataInvalidException("Los datos de la habilidad no son válidos");
        HabilidadDAO habilidadDAO = new HabilidadDAO();
        if (habilidadDAO.validNombreUpdate(habilidad.getNombre(), habilidad.getHabilidadId()))
            throw new EntityAlreadyExistsException("Ya existe una habilidad con el mismo nombre");
        habilidadDAO.update(habilidad);
    }

    public void updateHabilidadEstado(int habilidadId, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("No tienes permisos para actualizar el estado de una habilidad");
        HabilidadDAO habilidadDAO = new HabilidadDAO();
        habilidadDAO.getById(habilidadId).orElseThrow(() -> new UserDataInvalidException("No se encontró la habilidad con ID: " + habilidadId));
        habilidadDAO.updateEstado(habilidadId);
    }

    public Habilidad getHabilidadById(int habilidadId) throws SQLException, UserDataInvalidException {
        HabilidadDAO habilidadDAO = new HabilidadDAO();
        Optional<Habilidad> habilidadOptional = habilidadDAO.getById(habilidadId);
        if (habilidadOptional.isEmpty())
            throw new UserDataInvalidException("No se encontró la habilidad con ID: " + habilidadId);
        return habilidadOptional.get();
    }

    public List<Habilidad> getAllHabilidad() throws SQLException {
        HabilidadDAO habilidadDAO = new HabilidadDAO();
        return habilidadDAO.getAll();
    }

    public List<Habilidad> getAllHabilidadByCoincidence(String nombre) throws SQLException {
        HabilidadDAO habilidadDAO = new HabilidadDAO();
        return habilidadDAO.getByCoincidence(nombre);
    }

    public List<Habilidad> getAllHabilidadActivada() throws SQLException {
        HabilidadDAO habilidadDAO = new HabilidadDAO();
        return habilidadDAO.getAllHabilidadActivada();
    }

    public List<Habilidad> getAllHabilidadesNoRegistradosEnUsuario(EnumUsuario rol, int usuarioId) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException("No tienes permisos para obtener las habilidades no registradas en un usuario");
        HabilidadDAO habilidadDAO = new HabilidadDAO();
        return habilidadDAO.getAllHabilidadesNoRegistradosEnUsuario(usuarioId);
    }

    public boolean validNombre(String nombre, Connection connection) throws SQLException {
        HabilidadDAO habilidadDAO = new HabilidadDAO();
        return habilidadDAO.validNombre(nombre, connection);
    }

    public boolean validNombre(String nombre) throws SQLException {
        HabilidadDAO habilidadDAO = new HabilidadDAO();
        return habilidadDAO.validNombre(nombre);
    }

}
