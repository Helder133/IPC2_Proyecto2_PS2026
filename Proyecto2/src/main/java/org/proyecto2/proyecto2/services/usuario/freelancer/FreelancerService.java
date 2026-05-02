package org.proyecto2.proyecto2.services.usuario.freelancer;

import org.proyecto2.proyecto2.db.usuario.freelancer.FreelancerDAO;
import org.proyecto2.proyecto2.dtos.usuario.freelancer.FreelancerRequest;
import org.proyecto2.proyecto2.dtos.usuario.freelancer.FreelancerUpdate;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.models.usuario.freelancer.Freelancer;

import java.sql.SQLException;
import java.util.Optional;

public class FreelancerService {
    public void insertComplemento(FreelancerRequest freelancerRequest, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        Freelancer freelancer = new Freelancer(freelancerRequest);
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException(String.format("Solo un usuario con el rol: %s, puede agregar un complemento de freelancer.", EnumUsuario.Freelancer));
        if (!freelancer.isValid()) throw new UserDataInvalidException("Descripción, experiencia y tarifa por hora son requeridos");
        FreelancerDAO freelancerDAO = new FreelancerDAO();
        freelancerDAO.insert(freelancer);
    }

    public void updateComplemento(FreelancerUpdate freelancerUpdate, EnumUsuario rol, int usuarioId) throws SQLException, UserDataInvalidException {
        Freelancer  freelancer = new Freelancer(freelancerUpdate);
        if (!freelancer.isValid()) throw new UserDataInvalidException("Descripción, experiencia y tarifa por hora son requeridos");
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException(String.format("Solo un usuario con el rol: %s, puede actualizar su complemento de freelancer.", EnumUsuario.Freelancer));
        if (freelancer.getUsuarioId() != usuarioId)
            throw new UserDataInvalidException("No cuenta con el permiso para actualizar el complemento de freelancer de otro usuario.");
        FreelancerDAO freelancerDAO = new FreelancerDAO();
        freelancerDAO.update(freelancer);
    }

    public Optional<Freelancer> getComplemento(int usuarioId) throws SQLException {
        FreelancerDAO freelancerDAO = new FreelancerDAO();
        return freelancerDAO.getById(usuarioId);
    }
}
