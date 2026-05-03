package org.proyecto2.proyecto2.services.usuario.cartera;

import org.proyecto2.proyecto2.db.usuario.cartera.TransaccionDAO;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.usuario.cartera.EnumTransaccion;
import org.proyecto2.proyecto2.models.usuario.cartera.Transaccion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TransaccionService {
    public void createTransaccion(Connection connection, int usuarioId, EnumTransaccion transaccion, double monto) throws SQLException {
        Transaccion transaccion1 = new Transaccion(usuarioId, transaccion, monto);
        TransaccionDAO transaccionDAO = new TransaccionDAO();
        transaccionDAO.insert(connection, transaccion1);
    }

    public List<Transaccion> getTransaccionesByUsuarioId(int usuarioId) throws SQLException {
        TransaccionDAO transaccionDAO = new TransaccionDAO();
        return transaccionDAO.getAll(usuarioId);
    }

}
