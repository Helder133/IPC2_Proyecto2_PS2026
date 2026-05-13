package org.proyecto2.proyecto2.services.usuario.cartera;

import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.db.usuario.cartera.CarteraDAO;
import org.proyecto2.proyecto2.exceptions.EntityAlreadyExistsException;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.usuario.cartera.Cartera;
import org.proyecto2.proyecto2.models.usuario.cartera.EnumTransaccion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class CarteraService {
    public void createCartera(Connection connection, int usuarioId) throws SQLException, EntityAlreadyExistsException {
        Cartera cartera = new Cartera(usuarioId);
        CarteraDAO carteraDAO = new CarteraDAO();
        if (carteraDAO.validCartera(connection, usuarioId))
            throw new EntityAlreadyExistsException("La cartera ya existe para el usuario con ID: " + usuarioId);
        carteraDAO.insert(connection, cartera);
    }

    public Cartera getCarteraById(int usuarioId) throws SQLException, UserDataInvalidException {
        CarteraDAO carteraDAO = new CarteraDAO();
        Optional<Cartera> carteraOptional = carteraDAO.getById(usuarioId);
        if (carteraOptional.isEmpty())
            throw new UserDataInvalidException("No se encontró una cartera para el usuario con ID: " + usuarioId);
        return carteraOptional.get();
    }

    public void recargarCartera(int usuarioId, double monto) throws SQLException, UserDataInvalidException {
        if (monto <= 0) throw new UserDataInvalidException("El monto a recargar debe ser mayor a cero.");
        CarteraDAO carteraDAO = new CarteraDAO();
        Optional<Cartera> carteraOptional = carteraDAO.getById(usuarioId);
        if (carteraOptional.isEmpty())
            throw new UserDataInvalidException("No se encontró una cartera para el usuario con ID: " + usuarioId);
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        try {
            Cartera cartera = carteraOptional.get();
            cartera.setSaldo(cartera.getSaldo() + monto);
            TransaccionService transaccionService = new TransaccionService();
            transaccionService.createTransaccion(connection, usuarioId, EnumTransaccion.Recarga, monto);
            carteraDAO.update(connection, cartera);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void bloquearCartera(Connection connection, Cartera cartera, double monto) throws SQLException {
        CarteraDAO carteraDAO = new CarteraDAO();
        carteraDAO.update(connection, cartera);
        TransaccionService transaccionService = new TransaccionService();
        transaccionService.createTransaccion(connection, cartera.getUsuarioId(), EnumTransaccion.Bloqueo, monto);
    }

    public void devolucionCartera(Connection connection, Cartera cartera, double monto) throws SQLException {
        CarteraDAO carteraDAO = new CarteraDAO();
        carteraDAO.update(connection, cartera);
        TransaccionService transaccionService = new TransaccionService();
        transaccionService.createTransaccion(connection, cartera.getUsuarioId(), EnumTransaccion.Devolucion, monto);
    }

    public void pagoCartera(Connection connection, Cartera cartera, double monto) throws SQLException {
        CarteraDAO carteraDAO = new CarteraDAO();
        carteraDAO.update(connection, cartera);
        TransaccionService transaccionService = new TransaccionService();
        transaccionService.createTransaccion(connection, cartera.getUsuarioId(), EnumTransaccion.Pago, monto);
    }

}
