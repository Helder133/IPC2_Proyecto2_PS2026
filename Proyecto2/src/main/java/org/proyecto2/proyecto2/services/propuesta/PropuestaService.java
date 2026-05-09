package org.proyecto2.proyecto2.services.propuesta;

import org.proyecto2.proyecto2.db.propuesta.PropuestaDAO;
import org.proyecto2.proyecto2.dtos.propuesta.PropuestaRequest;
import org.proyecto2.proyecto2.dtos.propuesta.PropuestaUpdate;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.propuesta.EnumPropuesta;
import org.proyecto2.proyecto2.models.propuesta.Propuesta;
import org.proyecto2.proyecto2.models.propuesta.PropuestaDetalle;
import org.proyecto2.proyecto2.models.proyecto.Proyecto;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.models.usuario.Usuario;
import org.proyecto2.proyecto2.services.proyecto.ProyectoService;
import org.proyecto2.proyecto2.services.usuario.UsuarioService;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PropuestaService {
    public void insertPropuesta(PropuestaRequest propuestaRequest, int usuarioId, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException("Solo los freelancers pueden crear propuestas");
        Propuesta propuesta = new Propuesta(propuestaRequest);
        propuesta.setUsuarioId(usuarioId);
        if (!propuesta.isValid())
            throw new UserDataInvalidException("Datos de propuesta inválidos");
        ProyectoService proyectoService = new ProyectoService();
        Proyecto proyecto = proyectoService.getById(propuesta.getProyectoId());
        long dias = ChronoUnit.DAYS.between(propuesta.getFechaCreacion(), proyecto.getFechaLimite());
        if (propuesta.getTiempoEntrega() > dias)
            throw new UserDataInvalidException("El tiempo de entrega no puede ser mayor a los días restantes para la fecha límite del proyecto");
        if (propuesta.getMonto() > proyecto.getPresupuesto())
            throw new UserDataInvalidException("El monto de la propuesta no puede ser mayor al presupuesto del proyecto");
        UsuarioService usuarioService = new UsuarioService();
        boolean cuentaConHabilidad = false;
        Usuario usuario = usuarioService.getByUsuarioId(usuarioId);
        for (int i = 0; i < proyecto.getHabilidades().size(); i++) {
            for (int j = 0; j < usuario.getFreelancer().getHabilidades().size(); j++) {
                if (proyecto.getHabilidades().get(i).getHabilidadId() == usuario.getFreelancer().getHabilidades().get(j).getHabilidadId()) {
                    cuentaConHabilidad = true;
                    break;
                }
            }
        }
        if (!cuentaConHabilidad)
            throw new UserDataInvalidException("El freelancer no cuenta con las habilidades requeridas para el proyecto");
        PropuestaDAO propuestaDAO = new PropuestaDAO();
        propuestaDAO.insert(propuesta);
    }

    public void updatePropuesta(PropuestaUpdate propuestaUpdate, int usuarioId, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException("Solo los freelancers pueden actualizar propuestas");
        Propuesta propuesta = new Propuesta(propuestaUpdate);
        PropuestaDAO propuestaDAO = new PropuestaDAO();
        propuesta.setUsuarioId(usuarioId);
        if (!propuestaDAO.existsPropuesta(propuestaUpdate.getPropuestaId(), usuarioId))
            throw new UserDataInvalidException("La propuesta no existe o no pertenece al usuario");
        Propuesta p = propuestaDAO.getById(propuesta.getPropuestaId()).orElseThrow(() -> new UserDataInvalidException("La propuesta no existe"));
        if (!propuesta.isValidUpdate())
            throw new UserDataInvalidException("Datos de propuesta inválidos");
        ProyectoService proyectoService = new ProyectoService();
        Proyecto proyecto = proyectoService.getById(p.getProyectoId());
        long dias = ChronoUnit.DAYS.between(p.getFechaCreacion(), proyecto.getFechaLimite());
        if (propuesta.getTiempoEntrega() > dias)
            throw new UserDataInvalidException("El tiempo de entrega no puede ser mayor a los días restantes para la fecha límite del proyecto");
        if (propuesta.getMonto() > proyecto.getPresupuesto())
            throw new UserDataInvalidException("El monto de la propuesta no puede ser mayor al presupuesto del proyecto");
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

    public List<PropuestaDetalle> getAllPropuestaForAProyecto(int proyectoId, int usuarioId, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException("Solo los clientes pueden ver las propuestas de un proyecto");
        PropuestaDAO propuestaDAO = new PropuestaDAO();
        return propuestaDAO.getAllPropuestaForAProyecto(proyectoId, usuarioId);
    }
}
