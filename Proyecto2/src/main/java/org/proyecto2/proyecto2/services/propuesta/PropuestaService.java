package org.proyecto2.proyecto2.services.propuesta;

import org.proyecto2.proyecto2.db.propuesta.PropuestaDAO;
import org.proyecto2.proyecto2.dtos.propuesta.PropuestaRequest;
import org.proyecto2.proyecto2.dtos.propuesta.PropuestaUpdate;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.propuesta.EnumPropuesta;
import org.proyecto2.proyecto2.models.propuesta.Propuesta;
import org.proyecto2.proyecto2.models.propuesta.PropuestaDetalle;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PropuestaService {
    public void insertPropuesta(PropuestaRequest propuestaRequest, int usuarioId, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException("Solo los freelancers pueden crear propuestas");
        Propuesta propuesta = new Propuesta(propuestaRequest);
        propuesta.setUsuarioId(usuarioId);
        if (!propuesta.isValid())
            throw new UserDataInvalidException("Datos de propuesta inválidos");
        PropuestaDAO propuestaDAO = new PropuestaDAO();
        propuestaDAO.insert(propuesta);
    }

    public void updatePropuesta(PropuestaUpdate propuestaUpdate, int usuarioId, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException("Solo los freelancers pueden actualizar propuestas");
        Propuesta propuesta = new Propuesta(propuestaUpdate);
        PropuestaDAO propuestaDAO = new PropuestaDAO();
        if (propuestaDAO.existsPropuesta(propuestaUpdate.getPropuestaId(), usuarioId))
            throw new UserDataInvalidException("La propuesta no existe o no pertenece al usuario");
        if (!propuesta.isValidUpdate())
            throw new UserDataInvalidException("Datos de propuesta inválidos");
        propuestaDAO.update(propuesta);
    }

    public void updatePropuestaEstado(int proyectoId, EnumPropuesta estado, EnumUsuario rol, Connection connection) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException("Solo los clientes pueden actualizar el estado de las propuestas");
        PropuestaDAO propuestaDAO = new PropuestaDAO();
        propuestaDAO.updateEstado(estado, proyectoId, connection);
    }

    public Propuesta getPropuestaById(int propuestaId) throws SQLException {
        PropuestaDAO propuestaDAO = new PropuestaDAO();
        return propuestaDAO.getById(propuestaId).orElse(null);
    }

    public List<Propuesta> getAllPropuestaFromAFreelancer(int usuarioId, int proyectoId) throws SQLException {
        PropuestaDAO propuestaDAO = new PropuestaDAO();
        return propuestaDAO.getAllPropuestaFromAFreelancer(usuarioId, proyectoId);
    }

    public List<PropuestaDetalle> getAllPropuestaForAProyecto(int proyectoId, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException("Solo los clientes pueden ver las propuestas de un proyecto");
        PropuestaDAO propuestaDAO = new PropuestaDAO();
        return propuestaDAO.getAllPropuestaForAProyecto(proyectoId);
    }
}
