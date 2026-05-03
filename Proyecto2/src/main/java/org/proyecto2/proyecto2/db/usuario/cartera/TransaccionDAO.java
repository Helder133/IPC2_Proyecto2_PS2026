package org.proyecto2.proyecto2.db.usuario.cartera;

import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.models.usuario.cartera.EnumTransaccion;
import org.proyecto2.proyecto2.models.usuario.cartera.Transaccion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaccionDAO {
    private static final String INSERT_TRANSACCION = "INSERT INTO transaccion (usuario_id, tipo, monto, fecha) VALUES (?, ?, ?, ?)";
    private static final String GET_TRANSACCION_BY_ID = "SELECT * FROM transaccion WHERE usuario_id = ?";

    public void insert(Connection connection, Transaccion transaccion) throws SQLException {
        try (PreparedStatement insert = connection.prepareStatement(INSERT_TRANSACCION)) {
            insert.setInt(1, transaccion.getUsuarioId());
            insert.setString(2, transaccion.getTipo().name());
            insert.setDouble(3, transaccion.getMonto());
            insert.setDate(4, Date.valueOf(transaccion.getFecha()));
            insert.executeUpdate();
        }
    }

    public List<Transaccion> getAll(int usuarioId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Transaccion> transacciones = new ArrayList<>();
        try (PreparedStatement select = connection.prepareStatement(GET_TRANSACCION_BY_ID)) {
            select.setInt(1, usuarioId);
            try (ResultSet resultSet = select.executeQuery()) {
                while (resultSet.next()) {
                    transacciones.add(extraerDatos(resultSet));
                }
                return transacciones;
            }
        }
    }

    private Transaccion extraerDatos(ResultSet resultSet) throws SQLException {
        Transaccion transaccion = new Transaccion(
                resultSet.getInt("usuario_id"),
                EnumTransaccion.valueOf(resultSet.getString("tipo")),
                resultSet.getDouble("monto"));
        transaccion.setTransaccionId(resultSet.getInt("transaccion"));
        transaccion.setFecha(resultSet.getDate("fecha").toLocalDate());
        return transaccion;
    }
}
