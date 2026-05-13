package org.proyecto2.proyecto2.services.usuario.cartera;

import org.proyecto2.proyecto2.db.usuario.cartera.TransaccionPlataformaDAO;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.models.usuario.cartera.TransaccionPlataforma;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TransaccionPlataformaService {
    public void createTransaccion(TransaccionPlataforma transaccionPlataforma, Connection connection) throws SQLException, UserDataInvalidException {
        if (!transaccionPlataforma.isValid())
            throw new UserDataInvalidException("La transacción de plataforma no es válida. Verifique los datos ingresados.");
        TransaccionPlataformaDAO  transaccionPlataformaDAO = new TransaccionPlataformaDAO();
        transaccionPlataformaDAO.insert(transaccionPlataforma, connection);
    }

    public List<TransaccionPlataforma> getAllTransaccionPlataformas(EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("No tiene permisos para acceder a esta información.");
        TransaccionPlataformaDAO transaccionPlataformaDAO = new TransaccionPlataformaDAO();
        return transaccionPlataformaDAO.getAllTransaccionPlataformas();
    }

    public List<TransaccionPlataforma> getAllTransaccionPlataformas() throws SQLException, UserDataInvalidException {
        TransaccionPlataformaDAO transaccionPlataformaDAO = new TransaccionPlataformaDAO();
        return transaccionPlataformaDAO.getAllTransaccionPlataformas();
    }

}
