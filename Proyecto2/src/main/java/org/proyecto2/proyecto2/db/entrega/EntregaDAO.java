package org.proyecto2.proyecto2.db.entrega;

import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.models.entrega.Entrega;
import org.proyecto2.proyecto2.models.entrega.EnumEntrega;

import java.security.PublicKey;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntregaDAO {
    private static final String INSERT_ENTREGA = "INSERT INTO entrega (contrato_id, descripcion, archivo, fecha) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_ENTREGA = "UPDATE entrega SET descripcion = ?, archivo = ? WHERE entrega_id = ?";
    private static final String UPDATE_ENTREGA_ESTADO_RECHAZADO = "UPDATE entrega SET estado = 'RECHAZADA', motivo_rechazo = ? WHERE entrega_id = ?";
    private static final String UPDATE_ENTREGA_ESTADO_APROBADO = "UPDATE entrega SET estado = 'APROBADA' WHERE entrega_id = ?";
    private static final String GET_ENTREGA_BY_ID = "SELECT * FROM entrega WHERE entrega_id = ?";
    private static final String GET_ALL_ENTREGAS_OF_A_CONTRACT = "SELECT * FROM entrega WHERE contrato_id = ?";
    private static final String GET_THE_PROYECTO_ID_BY_CONTRATO_ID = "SELECT p.proyecto_id FROM contrato c JOIN propuesta p ON c.propuesta_id = p.propuesta_id WHERE c.contrato_id = ?";
    private static final String GET_THE_PROYECTO_ID_BY_ENTREGA_ID = "SELECT p.proyecto_id FROM entrega e JOIN contrato c ON e.contrato_id = c.contrato_id JOIN propuesta p ON c.propuesta_id = p.propuesta_id WHERE e.entrega_id = ?";
    private static final String GET_THE_PROPUESTA_ID_BY_ENTREGA_ID = "SELECT c.propuesta_id FROM entrega e JOIN contrato c ON e.contrato_id = c.contrato_id WHERE e.entrega_id = ?";
    private static final String GET_THE_CONTRATO_ID_BY_ENTREGA_ID = "SELECT contrato_id FROM entrega WHERE entrega_id = ?";
    private static final String EXISTS_ENTREGA_PENDIENTE = "SELECT 1 FROM entrega WHERE contrato_id = ? AND estado = 'PENDIENTE'";

    public int getTheContratoIdByEntregaId(int entregaId, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_THE_CONTRATO_ID_BY_ENTREGA_ID)) {
            preparedStatement.setInt(1, entregaId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("contrato_id");
                } else {
                    throw new SQLException("No se encontró el contrato para el entrega ID: " + entregaId);
                }
            }
        }
    }

    public int getTheProyectoIdByContratoId(int contratoId, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_THE_PROYECTO_ID_BY_CONTRATO_ID)) {
            preparedStatement.setInt(1, contratoId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("proyecto_id");
                } else {
                    throw new SQLException("No se encontró el proyecto para el contrato ID: " + contratoId);
                }
            }
        }
    }

    public int getTheProyectoIdByEntregaId(int entregaId, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_THE_PROYECTO_ID_BY_ENTREGA_ID)) {
            preparedStatement.setInt(1, entregaId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("proyecto_id");
                } else {
                    throw new SQLException("No se encontró el proyecto para el contrato ID: " + entregaId);
                }
            }
        }
    }

    public int getThePropuestaIdByEntregaId(int contratoId, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_THE_PROPUESTA_ID_BY_ENTREGA_ID)) {
            preparedStatement.setInt(1, contratoId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("propuesta_id");
                } else {
                    throw new SQLException("No se encontró el proyecto para el contrato ID: " + contratoId);
                }
            }
        }
    }

    public boolean existsEntregaPendiente(int contratoId, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(EXISTS_ENTREGA_PENDIENTE)) {
            preparedStatement.setInt(1, contratoId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public void insert(Entrega entrega, Connection connection) throws SQLException {
        try (PreparedStatement insert = connection.prepareStatement(INSERT_ENTREGA)) {
            insert.setInt(1, entrega.getContratoId());
            insert.setString(2, entrega.getDescripcion());
            insert.setString(3, entrega.getArchivo());
            insert.setDate(4, Date.valueOf(entrega.getFecha()));
            insert.executeUpdate();
        }
    }

    public void update(Entrega entrega) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement update = connection.prepareStatement(UPDATE_ENTREGA)) {
            update.setString(1, entrega.getDescripcion());
            update.setString(2, entrega.getArchivo());
            update.setInt(3, entrega.getEntregaId());
            update.executeUpdate();
        }
    }

    public void updateEntregaEstadoRechazado(Entrega entrega, Connection connection) throws SQLException {
        try (PreparedStatement update = connection.prepareStatement(UPDATE_ENTREGA_ESTADO_RECHAZADO)) {
            update.setString(1, entrega.getMotivo_rechazo());
            update.setInt(2, entrega.getEntregaId());
            update.executeUpdate();

        }
    }

    public void updateEntregaEstadoAprobado(int entregaId, Connection connection) throws SQLException {
        try (PreparedStatement update = connection.prepareStatement(UPDATE_ENTREGA_ESTADO_APROBADO)) {
            update.setInt(1, entregaId);
            update.executeUpdate();

        }
    }

    public Optional<Entrega> getEntregaById(int entregaId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement select = connection.prepareStatement(GET_ENTREGA_BY_ID)) {
            select.setInt(1, entregaId);
            try (ResultSet rs = select.executeQuery()) {
                if (rs.next()) return Optional.of(extraerDatos(rs));
                else return Optional.empty();
            }
        }
    }

    public List<Entrega> getAllEntregasOfAContract(int contratoId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Entrega> entregas = new ArrayList<>();
        try (PreparedStatement select = connection.prepareStatement(GET_ALL_ENTREGAS_OF_A_CONTRACT)) {
            select.setInt(1, contratoId);
            try (ResultSet rs = select.executeQuery()) {
                while (rs.next()) entregas.add(extraerDatos(rs));
                return entregas;
            }
        }
    }

    private Entrega extraerDatos(ResultSet rs) throws SQLException {
        return new Entrega(
                rs.getInt("entrega_id"),
                rs.getInt("contrato_id"),
                rs.getString("descripcion"),
                rs.getString("archivo"),
                EnumEntrega.valueOf(rs.getString("estado")),
                rs.getString("motivo_rechazo"),
                rs.getDate("fecha").toLocalDate()
        );
    }
}
