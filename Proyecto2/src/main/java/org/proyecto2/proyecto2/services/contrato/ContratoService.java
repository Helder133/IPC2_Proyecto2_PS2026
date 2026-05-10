package org.proyecto2.proyecto2.services.contrato;

import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.db.contrato.ContratoDAO;
import org.proyecto2.proyecto2.dtos.contrato.ContratoCancelado;
import org.proyecto2.proyecto2.dtos.contrato.ContratoFinalizado;
import org.proyecto2.proyecto2.exceptions.EntityAlreadyExistsException;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.contrato.Contrato;
import org.proyecto2.proyecto2.models.propuesta.Propuesta;
import org.proyecto2.proyecto2.models.proyecto.Proyecto;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.models.usuario.cartera.Cartera;
import org.proyecto2.proyecto2.services.propuesta.PropuestaService;
import org.proyecto2.proyecto2.services.proyecto.ProyectoService;
import org.proyecto2.proyecto2.services.usuario.cartera.CarteraService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ContratoService {
    public void insertContrato(Contrato contrato, Connection connection) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        if (!contrato.isValid())
            throw new UserDataInvalidException("Los datos del contrato no son válidos");
        ContratoDAO contratoDAO = new ContratoDAO();
        if (contratoDAO.existsContrato(contrato.getPropuestaId(), connection))
            throw new EntityAlreadyExistsException("La propuesta ya cuenta con un contrato existente");
        contratoDAO.insert(contrato, connection);
    }

    public Contrato getContractById(int contratoId) throws SQLException, UserDataInvalidException {
        ContratoDAO contratoDAO = new ContratoDAO();
        return contratoDAO.getById(contratoId).orElseThrow(() -> new UserDataInvalidException("La contrato no existe"));
    }

    public Contrato getContractById(int contratoId, Connection connection) throws SQLException, UserDataInvalidException {
        ContratoDAO contratoDAO = new ContratoDAO();
        return contratoDAO.getById(contratoId, connection).orElseThrow(() -> new UserDataInvalidException("La contrato no existe"));
    }

    public List<Contrato> getAllContractsFromAFreelancer(int usuarioId, EnumUsuario ro) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Freelancer.equals(ro))
            throw new UserDataInvalidException("El rol del usuario no es válido para esta operación");
        ContratoDAO contratoDAO = new ContratoDAO();
        return contratoDAO.getAllContractsFromAFreelancer(usuarioId);
    }

    public List<Contrato> getAllContractsFromACliente(int usuarioId, EnumUsuario ro) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Cliente.equals(ro))
            throw new UserDataInvalidException("El rol del usuario no es válido para esta operación");
        ContratoDAO contratoDAO = new ContratoDAO();
        return contratoDAO.getAllContractsFromAClient(usuarioId);
    }

    public void updateCanceladoContrato(ContratoCancelado contratoCancelado, EnumUsuario rol, int usuarioId) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException("Solo los clientes pueden cancelar contratos");
        Contrato contrato = new Contrato(contratoCancelado);
        if (!contrato.isValidCancelado())
            throw new UserDataInvalidException("Los datos para cancelar el contrato no son válidos");
        ContratoDAO contratoDAO = new ContratoDAO();
        int proyectoId = contratoDAO.existsContratoFromAClient(usuarioId, contrato.getContratoId());
        if (proyectoId == -1)
            throw new UserDataInvalidException("El contrato no existe o no pertenece al cliente");
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        try {
            contratoDAO.updateCancelacion(contrato, connection);
            ProyectoService proyectoService = new ProyectoService();
            proyectoService.updateProyectoEstadoCancelacion(proyectoId, connection);
            CarteraService carteraService =  new CarteraService();
            Cartera cartera = carteraService.getCarteraById(usuarioId);
            PropuestaService propuestaService = new PropuestaService();
            Contrato contrato1 = getContractById(contrato.getContratoId(),connection);
            Propuesta propuesta = propuestaService.getPropuestaById(contrato1.getPropuestaId(), connection);
            if (cartera.getSaldoBloqueado() < propuesta.getMonto())
                throw new UserDataInvalidException("El cliente no tiene saldo bloqueado suficiente para cancelar el contrato");
            cartera.setSaldoBloqueado(cartera.getSaldoBloqueado() - propuesta.getMonto());
            cartera.setSaldo(cartera.getSaldo() + propuesta.getMonto());
            carteraService.devolucionCartera(connection, cartera, propuesta.getMonto());
            connection.commit();
        }catch (SQLException | UserDataInvalidException | EntityAlreadyExistsException e){
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void updateFinalizarContrato(ContratoFinalizado contratoFinalizado, Connection connection) throws SQLException {
        ContratoDAO contratoDAO = new ContratoDAO();
        Contrato contrato = new Contrato(contratoFinalizado);
        if (!contrato.isValidFinalizado())
            throw new UserDataInvalidException("Los datos para finalizar el contrato no son válidos");
        contratoDAO.updateFinalizado(contrato, connection);
    }

}