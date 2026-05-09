package org.proyecto2.proyecto2.db.contrato;

import org.proyecto2.proyecto2.db.config.CRUD;
import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.models.contrato.Contrato;
import org.proyecto2.proyecto2.models.contrato.EnumContrato;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContratoDAO {
    private static final String INSERT_CONTRATO = "INSERT INTO contrato (propuesta_id, fecha_creacion) VALUES (?, ?)";
    private static final String UPDATE_ESTADO_FINALIZACION = "UPDATE contrato SET estado = 'FINALIZADO', fecha_finalizacion = ?, comentario = ?, calificacion = ? WHERE contrato_id = ?";
    private static final String UPDATE_ESTADO_CANCELACION = "UPDATE contrato SET estado = 'CANCELADO', motivo_cancelacion = ?, fecha_finalizacion = ? WHERE contrato_id = ?";
    private static final String GET_BY_ID_CONTRATO = "SELECT * FROM contrato WHERE contrato_id = ?";
    private static final String GET_ALL_CONTRACTS_FROM_A_FREELANCER = "SELECT c.* FROM contrato c JOIN propuesta p on c.propuesta_id = p.propuesta_id WHERE p.usuario_id = ?";

    public void insert(Contrato contrato) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CONTRATO)) {
            preparedStatement.setInt(1, contrato.getPropuestaId());
            preparedStatement.setDate(2, Date.valueOf(contrato.getFechaCreacion()));
            preparedStatement.executeUpdate();

        }
    }

    public void updateFinalizado(Contrato contrato) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ESTADO_FINALIZACION)) {
            preparedStatement.setDate(1, Date.valueOf(contrato.getFechaFinalizacion()));
            preparedStatement.setString(2, contrato.getComentario());
            preparedStatement.setInt(3, contrato.getCalificacion());
            preparedStatement.setInt(4, contrato.getContratoId());
            preparedStatement.executeUpdate();
        }
    }

    public void updateCancelacion(Contrato contrato) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ESTADO_CANCELACION)) {
            preparedStatement.setString(1, contrato.getMotivoCancelacion());
            preparedStatement.setDate(2, Date.valueOf(contrato.getFechaFinalizacion()));
            preparedStatement.setInt(3, contrato.getContratoId());
            preparedStatement.executeUpdate();
        }
    }

    public Optional<Contrato> getById(int id) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID_CONTRATO)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) return Optional.of(extraerDato(resultSet));
                return Optional.empty();
            }
        }
    }

    public List<Contrato> getAllContractsFromAFreelancer(int usuarioId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        List<Contrato> contratos = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(GET_ALL_CONTRACTS_FROM_A_FREELANCER)) {
            statement.setInt(1, usuarioId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) contratos.add(extraerDato(resultSet));
                return contratos;
            }
        }
    }

    private Contrato extraerDato(ResultSet resultSet) throws SQLException {
        return new Contrato(
                resultSet.getInt("contrato_id"),
                resultSet.getInt("propuesta_id"),
                EnumContrato.valueOf(resultSet.getString("estado")),
                resultSet.getString("motivo_cancelacion"),
                resultSet.getDate("fecha_creacion").toLocalDate(),
                resultSet.getDate("fecha_finalizacion") != null ? resultSet.getDate("fecha_finalizacion").toLocalDate() : null,
                resultSet.getString("comentario"),
                resultSet.getInt("calificacion")
        );
    }
}
