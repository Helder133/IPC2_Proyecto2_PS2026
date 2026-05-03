package org.proyecto2.proyecto2.services.habilidad;

import org.proyecto2.proyecto2.db.habilidad.HabilidadDAO;
import org.proyecto2.proyecto2.dtos.habilidad.HabilidadRequest;
import org.proyecto2.proyecto2.dtos.habilidad.HabilidadUpdate;
import org.proyecto2.proyecto2.exceptions.EntityAlreadyExistsException;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.habilidad.Habilidad;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;

import java.sql.SQLException;

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

    public void updateHabilidadEstado(int habilidadId, EnumUsuario rol) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        if (!EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("No tienes permisos para actualizar el estado de una habilidad");
        HabilidadDAO habilidadDAO = new HabilidadDAO();
        habilidadDAO.getById(habilidadId).orElseThrow(() -> new UserDataInvalidException("No se encontró la habilidad con ID: " + habilidadId));
        habilidadDAO.updateEstado(habilidadId);
    }
}
