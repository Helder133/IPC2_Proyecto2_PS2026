package org.proyecto2.proyecto2.services.usuario.freelancer;

import org.proyecto2.proyecto2.db.usuario.freelancer.FreelancerHabilidadDAO;
import org.proyecto2.proyecto2.dtos.usuario.freelancer.FreelancerHabilidadRequest;
import org.proyecto2.proyecto2.exceptions.EntityAlreadyExistsException;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.habilidad.Habilidad;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.models.usuario.freelancer.FreelancerHabilidad;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FreelancerHabilidadService {
    public void insert(FreelancerHabilidadRequest freelancerHabilidadRequest, EnumUsuario rol, int usuarioId, Connection connection) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException("No tienes permisos para agregar una habilidad a un freelancer");
        FreelancerHabilidad freelancerHabilidad = new FreelancerHabilidad(freelancerHabilidadRequest, usuarioId);
        if (!freelancerHabilidad.isValid())
            throw new UserDataInvalidException("Los datos de la habilidad del freelancer no son válidos");
        FreelancerHabilidadDAO freelancerHabilidadDAO = new FreelancerHabilidadDAO();
        if (freelancerHabilidadDAO.validaHabilidad(freelancerHabilidad))
            throw new EntityAlreadyExistsException("El freelancer ya tiene asignada esta habilidad");
        freelancerHabilidadDAO.insert(freelancerHabilidad, connection);
    }
    public void insert(FreelancerHabilidadRequest freelancerHabilidadRequest, EnumUsuario rol, int usuarioId) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException("No tienes permisos para agregar una habilidad a un freelancer");
        FreelancerHabilidad freelancerHabilidad = new FreelancerHabilidad(freelancerHabilidadRequest, usuarioId);
        if (!freelancerHabilidad.isValid())
            throw new UserDataInvalidException("Los datos de la habilidad del freelancer no son válidos");
        FreelancerHabilidadDAO freelancerHabilidadDAO = new FreelancerHabilidadDAO();
        if (freelancerHabilidadDAO.validaHabilidad(freelancerHabilidad))
            throw new EntityAlreadyExistsException("El freelancer ya tiene asignada esta habilidad");
        freelancerHabilidadDAO.insert(freelancerHabilidad);
    }

    public void delete(int habilidadId, EnumUsuario rol, int usuarioId) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException("No tienes permisos para eliminar una habilidad de un freelancer");
        FreelancerHabilidad freelancerHabilidad = new FreelancerHabilidad(habilidadId, usuarioId);
        if (!freelancerHabilidad.isValid())
            throw new UserDataInvalidException("Los datos de la habilidad del freelancer no son válidos");
        FreelancerHabilidadDAO freelancerHabilidadDAO = new FreelancerHabilidadDAO();
        if (!freelancerHabilidadDAO.validaHabilidad(freelancerHabilidad))
            throw new UserDataInvalidException("El freelancer no tiene asignada esta habilidad");
        freelancerHabilidadDAO.delete(freelancerHabilidad);
    }

    public List<Habilidad> getHabilidadesByUsuario(int usuarioId) throws SQLException {
        FreelancerHabilidadDAO freelancerHabilidadDAO = new FreelancerHabilidadDAO();
        return freelancerHabilidadDAO.getAllHabilidadByUsuario(usuarioId);
    }
}
