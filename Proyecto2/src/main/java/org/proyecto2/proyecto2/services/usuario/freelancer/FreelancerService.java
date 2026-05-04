package org.proyecto2.proyecto2.services.usuario.freelancer;

import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.db.usuario.freelancer.FreelancerDAO;
import org.proyecto2.proyecto2.dtos.usuario.freelancer.FreelancerRequest;
import org.proyecto2.proyecto2.dtos.usuario.freelancer.FreelancerUpdate;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.models.usuario.freelancer.Freelancer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class FreelancerService {
    public void insertComplemento(FreelancerRequest freelancerRequest, EnumUsuario rol, int usuarioId) throws SQLException, UserDataInvalidException {
        Freelancer freelancer = new Freelancer(freelancerRequest);
        freelancer.setUsuarioId(usuarioId);
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException(String.format("Solo un usuario con el rol: %s, puede agregar un complemento de freelancer.", EnumUsuario.Freelancer));
        if (!freelancer.isValid()) throw new UserDataInvalidException("Descripción, experiencia, tarifa por hora y mínimo una habilidad son requeridos");
        FreelancerDAO freelancerDAO = new FreelancerDAO();
        if (freelancerDAO.validComplemento(freelancer.getUsuarioId()))
            throw new UserDataInvalidException("El complemento de freelancer ya està registrado para este usuario.");
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        /*try {
           logica para registrar todas sus habilidades, si falla alguna, se hace rollback y no se registra el complemento de freelancer
        }*/
        freelancerDAO.insert(freelancer);
    }

    public void updateComplemento(Connection connection, FreelancerUpdate freelancerUpdate, EnumUsuario rol, int usuarioId) throws SQLException, UserDataInvalidException {
        Freelancer  freelancer = new Freelancer(freelancerUpdate);
        if (!freelancer.isValid()) throw new UserDataInvalidException("Descripción, experiencia y tarifa por hora son requeridos");
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException(String.format("Solo un usuario con el rol: %s, puede actualizar su complemento de freelancer.", EnumUsuario.Freelancer));
        if (freelancer.getUsuarioId() != usuarioId)
            throw new UserDataInvalidException("No cuenta con el permiso para actualizar el complemento de freelancer de otro usuario.");
        FreelancerDAO freelancerDAO = new FreelancerDAO();
        freelancerDAO.update(connection, freelancer);
    }

    public Optional<Freelancer> getComplemento(int usuarioId) throws SQLException {
        FreelancerDAO freelancerDAO = new FreelancerDAO();
        Optional<Freelancer> freelancer = freelancerDAO.getById(usuarioId);
        if (freelancer.isEmpty()) return Optional.empty();
        FreelancerHabilidadService freelancerHabilidadService = new FreelancerHabilidadService();
        freelancer.get().setHabilidades(freelancerHabilidadService.getHabilidadesByUsuario(usuarioId));
        return freelancer;
    }
}
