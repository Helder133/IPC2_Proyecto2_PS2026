package org.proyecto2.proyecto2.services.propuesta;

import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.db.propuesta.PropuestaDAO;
import org.proyecto2.proyecto2.dtos.propuesta.PropuestaRequest;
import org.proyecto2.proyecto2.dtos.propuesta.PropuestaUpdate;
import org.proyecto2.proyecto2.exceptions.EntityAlreadyExistsException;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.contrato.Contrato;
import org.proyecto2.proyecto2.models.propuesta.EnumPropuesta;
import org.proyecto2.proyecto2.models.propuesta.Propuesta;
import org.proyecto2.proyecto2.models.propuesta.PropuestaDetalle;
import org.proyecto2.proyecto2.models.propuesta.PropuestaHistorial;
import org.proyecto2.proyecto2.models.proyecto.Proyecto;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.models.usuario.Usuario;
import org.proyecto2.proyecto2.models.usuario.cartera.Cartera;
import org.proyecto2.proyecto2.services.contrato.ContratoService;
import org.proyecto2.proyecto2.services.proyecto.ProyectoService;
import org.proyecto2.proyecto2.services.usuario.UsuarioService;
import org.proyecto2.proyecto2.services.usuario.cartera.CarteraService;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PropuestaService {
    public void insertPropuesta(PropuestaRequest propuestaRequest, int usuarioId, EnumUsuario rol) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException("Solo los freelancers pueden crear propuestas");
        Propuesta propuesta = new Propuesta(propuestaRequest);
        propuesta.setUsuarioId(usuarioId);
        PropuestaDAO propuestaDAO = new PropuestaDAO();
        if (propuestaDAO.existsPropuestaFreelancer(propuesta.getProyectoId(), usuarioId))
            throw new EntityAlreadyExistsException("El freelancer ya ha creado una propuesta para este proyecto");
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

    public void updatePropuestaEstado(int propuestaId, EnumPropuesta estado, EnumUsuario rol, Connection connection) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException("Solo los clientes pueden actualizar el estado de las propuestas");
        PropuestaDAO propuestaDAO = new PropuestaDAO();
        propuestaDAO.updateEstado(estado, propuestaId, connection);
    }

    public void updatePropuestaEstadoRetirado(int propuestaId, int usuarioId, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException("Solo los freelancers pueden retirar propuestas");
        PropuestaDAO propuestaDAO = new PropuestaDAO();
        if (!propuestaDAO.existsPropuesta(propuestaId, usuarioId))
            throw new UserDataInvalidException("La propuesta no existe o no pertenece al usuario");
        propuestaDAO.updateEstado(EnumPropuesta.RETIRADO,propuestaId);
    }

    public void updatePropuestaEstadoRechazado(int propuestaId, int usuarioId, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException("Solo los clientes pueden rechazar propuestas");
        PropuestaDAO propuestaDAO = new PropuestaDAO();
        getPropuestaById(propuestaId);
        propuestaDAO.updateEstado(EnumPropuesta.RECHAZADA,propuestaId);
    }

    public void updatePropuestaEstadoAceptar(int propuestaId, EnumUsuario rol, int usuarioId) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException("Solo los clientes pueden actualizar el estado de las propuestas");
        PropuestaDAO propuestaDAO = new PropuestaDAO();
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        try {
            propuestaDAO.updateEstado(EnumPropuesta.ACEPTADA, propuestaId, connection);
            Contrato contrato = new Contrato(propuestaId);
            ContratoService contratoService = new ContratoService();
            contratoService.insertContrato(contrato, connection);
            Propuesta propuesta = getPropuestaById(propuestaId, connection);
            ProyectoService proyectoService = new ProyectoService();
            proyectoService.updateProyectoEstadoEnProgreso(propuesta.getProyectoId(), connection);
            CarteraService carteraService = new CarteraService();
            Cartera cartera = carteraService.getCarteraById(usuarioId);
            if (cartera.getSaldo() < propuesta.getMonto())
                throw new UserDataInvalidException("El cliente no tiene saldo suficiente para aceptar la propuesta");
            cartera.setSaldo(cartera.getSaldo() - propuesta.getMonto());
            cartera.setSaldoBloqueado(cartera.getSaldoBloqueado() + propuesta.getMonto());
            carteraService.bloquearCartera(connection, cartera, propuesta.getMonto());
            connection.commit();
        } catch (SQLException | UserDataInvalidException | EntityAlreadyExistsException ex) {
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public Propuesta getPropuestaById(int propuestaId) throws SQLException, UserDataInvalidException {
        PropuestaDAO propuestaDAO = new PropuestaDAO();
        return propuestaDAO.getById(propuestaId).orElseThrow(() -> new UserDataInvalidException("La propuesta no existe"));
    }

    public Propuesta getPropuestaById(int propuestaId,Connection connection) throws SQLException, UserDataInvalidException {
        PropuestaDAO propuestaDAO = new PropuestaDAO();
        return propuestaDAO.getById(propuestaId, connection).orElseThrow(() -> new UserDataInvalidException("La propuesta no existe"));
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

    public List<PropuestaHistorial> getHistorialPropuestasFreelancer(int usuarioId, String estado, EnumUsuario rol) throws SQLException {
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException("Solo los freelancers pueden ver el historial de sus propuestas");
        PropuestaDAO propuestaDAO = new PropuestaDAO();
        return propuestaDAO.getHistorialPropuestasFreelancer(usuarioId, estado);
    }
}
