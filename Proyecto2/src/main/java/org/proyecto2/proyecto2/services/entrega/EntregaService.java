package org.proyecto2.proyecto2.services.entrega;

import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.db.entrega.EntregaDAO;
import org.proyecto2.proyecto2.dtos.contrato.ContratoFinalizado;
import org.proyecto2.proyecto2.dtos.entrega.EntregaRechazada;
import org.proyecto2.proyecto2.dtos.entrega.EntregaRequest;
import org.proyecto2.proyecto2.dtos.entrega.EntregaUpdate;
import org.proyecto2.proyecto2.exceptions.EntityAlreadyExistsException;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.entrega.Entrega;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.services.proyecto.ProyectoService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EntregaService {
    public void createEntrega(EntregaRequest entregaRequest, EnumUsuario rol) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException("Solo los freelancers pueden crear entregas");
        Entrega entrega = new Entrega(entregaRequest);
        if (!entrega.isValid())
            throw new UserDataInvalidException("Los datos de la entrega no son válidos");
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        EntregaDAO entregaDAO = new EntregaDAO();
        try {
            if (entregaDAO.existsEntregaPendiente(entrega.getContratoId(), connection))
                throw new EntityAlreadyExistsException("Ya existe una entrega pendiente para este contrato");
            entregaDAO.insert(entrega, connection);
            int proyectoId = entregaDAO.getTheProyectoId(entrega.getContratoId(), connection);
            ProyectoService proyectoService = new ProyectoService();
            proyectoService.updateProyectoEstadoEntregaPendiente(proyectoId, connection);
            connection.commit();
        } catch (SQLException | UserDataInvalidException | EntityAlreadyExistsException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void updateEntrega(EntregaUpdate entregaUpdate, EnumUsuario rol) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException("Solo los freelancers pueden actualizar entregas");
        Entrega entrega = new Entrega(entregaUpdate);
        if (!entrega.isValidUpdate())
            throw new UserDataInvalidException("Los datos de la entrega no son válidos para actualizar");
        EntregaDAO entregaDAO = new EntregaDAO();
        entregaDAO.update(entrega);
    }

    public void rechazarEntrega(EntregaRechazada entregaRechazada, EnumUsuario rol) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException("Solo los clientes pueden rechazar entregas");
        Entrega entrega = new Entrega(entregaRechazada);
        if (!entrega.isValidRechazo())
            throw new UserDataInvalidException("Los datos para rechazar la entrega no son válidos");
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        EntregaDAO entregaDAO = new EntregaDAO();
        try {
            entregaDAO.updateEntregaEstadoRechazado(entrega, connection);
            int proyectoId = entregaDAO.getTheProyectoId(entrega.getContratoId(), connection);
            ProyectoService proyectoService = new ProyectoService();
            proyectoService.updateProyectoEstadoEnProgreso(proyectoId, connection);
            connection.commit();
        } catch (SQLException | UserDataInvalidException | EntityAlreadyExistsException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void aprobarEntrega(int entregaId, EnumUsuario rol, ContratoFinalizado contratoFinalizado) throws SQLException {
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException("Solo los clientes pueden aprobar entregas");
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        EntregaDAO entregaDAO = new EntregaDAO();
        try {
            entregaDAO.updateEntregaEstadoAprobado(entregaId, connection);
            int proyectoId = entregaDAO.getTheProyectoId(entregaId, connection);
            ProyectoService proyectoService = new ProyectoService();
            proyectoService.updateProyectoEstadoCompletado(proyectoId, connection);
            //logica para pagar al freelancer y para restar la comicion de la plataforma
            connection.commit();
        } catch (SQLException | UserDataInvalidException | EntityAlreadyExistsException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public Entrega getEntregaById(int entregaId) throws SQLException, UserDataInvalidException {
        EntregaDAO entregaDAO = new EntregaDAO();
        return entregaDAO.getEntregaById(entregaId).orElseThrow(() -> new UserDataInvalidException("La entrega selecionada no existe"));
    }

    public List<Entrega> getAllEntregasOfAContract(int contratoId) throws SQLException {
        EntregaDAO entregaDAO = new EntregaDAO();
        return entregaDAO.getAllEntregasOfAContract(contratoId);
    }
}
