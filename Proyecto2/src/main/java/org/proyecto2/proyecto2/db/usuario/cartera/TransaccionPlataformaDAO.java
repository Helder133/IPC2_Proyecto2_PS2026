package org.proyecto2.proyecto2.db.usuario.cartera;

import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.models.usuario.cartera.TransaccionPlataforma;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaccionPlataformaDAO {
    private static final String INSERT_TRANSACCION = "INSERT INTO transaccion_plataforma (plataforma_id, contrato_id, porcentaje_aplicado, monto_comision, fecha) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_ALL_TRANSACCIONS = "SELECT * FROM transaccion_plataforma";

    public void insert(TransaccionPlataforma transaccionPlataforma, Connection connection) throws SQLException {
        try (PreparedStatement insert = connection.prepareStatement(INSERT_TRANSACCION)) {
            insert.setInt(1, transaccionPlataforma.getPlataformaId());
            insert.setInt(2, transaccionPlataforma.getContratoId());
            insert.setDouble(3, transaccionPlataforma.getPorcentajeAplicado());
            insert.setDouble(4, transaccionPlataforma.getMonto_comision());
            insert.setDate(5, Date.valueOf(transaccionPlataforma.getFecha()));
            insert.executeUpdate();
        }
    }

    public List<TransaccionPlataforma> getAllTransaccionPlataformas() throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<TransaccionPlataforma> transaccionPlataformas = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_TRANSACCIONS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next())
                transaccionPlataformas.add(extraerDatos(resultSet));
            return  transaccionPlataformas;
        }
    }
    private TransaccionPlataforma extraerDatos(ResultSet resultSet) throws SQLException {
        return new TransaccionPlataforma(
                resultSet.getInt("transaccion_id"),
                resultSet.getInt("plataforma_id"),
                resultSet.getInt("contrato_id"),
                resultSet.getDouble("porcentaje_aplicado"),
                resultSet.getDouble("monto_comision"),
                resultSet.getDate("fecha").toLocalDate()
        );
    }
}
