package org.proyecto2.proyecto2.services.proyecto;

import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.db.proyecto.ProyectoDAO;
import org.proyecto2.proyecto2.dtos.proyecto.ProyectoHabilidadRequest;
import org.proyecto2.proyecto2.dtos.proyecto.ProyectoRequest;
import org.proyecto2.proyecto2.dtos.proyecto.ProyectoUpdate;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.habilidad.Habilidad;
import org.proyecto2.proyecto2.models.proyecto.EnumProyecto;
import org.proyecto2.proyecto2.models.proyecto.Proyecto;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.services.propuesta.PropuestaService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProyectoService {
    public void createProyecto(ProyectoRequest proyectoRequest, EnumUsuario rol, int usuarioId) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException("El usuario no tiene permisos para crear un proyecto.");
        Proyecto proyecto = new Proyecto(proyectoRequest);
        proyecto.setUsuarioId(usuarioId);
        if (!proyecto.isValid()) throw new UserDataInvalidException("Los datos del proyecto no son válidos.");
        if (proyectoRequest.getProyectoHabilidadRequest().isEmpty())
            throw new UserDataInvalidException("El proyecto debe tener al menos una habilidad.");
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        try {
            int proyectoId = proyectoDAO.insert(proyecto, connection);
            for (ProyectoHabilidadRequest proyectoHabilidadRequest : proyectoRequest.getProyectoHabilidadRequest()) {
                ProyectoHabilidadService proyectoHabilidadService = new ProyectoHabilidadService();
                proyectoHabilidadService.insertProyectoHabilidad(proyectoHabilidadRequest, rol, usuarioId, proyectoId, connection);
            }
            connection.commit();
        } catch (SQLException | UserDataInvalidException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void updateProyecto(ProyectoUpdate proyectoUpdate, EnumUsuario rol, int usuarioId) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException("El usuario no tiene permisos para actualizar un proyecto.");
        Proyecto proyecto = new Proyecto(proyectoUpdate);
        if (!proyecto.isValidUpdate()) throw new UserDataInvalidException("Los datos del proyecto no son válidos.");
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        if (!proyectoDAO.existsProyectoUsuario(proyecto.getProyectoId(), usuarioId))
            throw new UserDataInvalidException("El proyecto no existe o no pertenece al usuario.");
        proyectoDAO.update(proyecto);
    }

    public void updateProyectoEstadoCancelacion(int proyectoId, Connection connection) throws SQLException {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        proyectoDAO.updateEstado(EnumProyecto.CANCELADO, proyectoId, connection);
    }

    public void updateProyectoEstadoEnProgreso(int proyectoId, Connection connection) throws SQLException {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        proyectoDAO.updateEstado(EnumProyecto.EN_PROGRESO, proyectoId, connection);
    }

    public void updateProyectoEstadoEntregaPendiente(int proyectoId, Connection connection) throws SQLException {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        proyectoDAO.updateEstado(EnumProyecto.ENTREGA_PENDIENTE, proyectoId, connection);
    }

    public void updateProyectoEstadoCompletado(int proyectoId, Connection connection) throws SQLException {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        proyectoDAO.updateEstado(EnumProyecto.COMPLETADO, proyectoId, connection);
    }

    public void updateProyectoEstadoCancelacion(int proyectoId, int usuarioId, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException("El usuario no tiene permisos para actualizar el estado de un proyecto.");
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        if (!proyectoDAO.existsProyectoUsuario(proyectoId, usuarioId))
            throw new UserDataInvalidException("El proyecto no existe o no pertenece al usuario.");
        proyectoDAO.updateEstadoCancelado(proyectoId);
    }

    public void updateProyectoEstadoEnRevision(int proyectoId, int usuarioId, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException("El usuario no tiene permisos para actualizar el estado de un proyecto.");
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        Proyecto proyecto = proyectoDAO.getById(proyectoId).orElseThrow(() -> new UserDataInvalidException("El proyecto no existe."));
        PropuestaService propuestaService = new PropuestaService();
        if (proyecto.getEstado().equals(EnumProyecto.ABIERTO) && propuestaService.existsPropuestaEnProyecto(proyectoId))
            proyectoDAO.updateEstado(EnumProyecto.EN_REVISION, proyectoId);
    }

    private List<Habilidad> extraerHabilidades(int proyectoId) throws SQLException, UserDataInvalidException {
        ProyectoHabilidadService proyectoHabilidadService = new ProyectoHabilidadService();
        return proyectoHabilidadService.getAllHabilidadesByProyecto(proyectoId);
    }

    public Proyecto getById(int proyectoId) throws SQLException, UserDataInvalidException {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        Optional<Proyecto> proyecto = proyectoDAO.getById(proyectoId);
        if (proyecto.isEmpty()) throw new UserDataInvalidException("El proyecto no existe.");
        proyecto.get().setHabilidades(extraerHabilidades(proyecto.get().getProyectoId()));
        return proyecto.get();
    }

    public List<Proyecto> getAll() throws SQLException {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        List<Proyecto> proyectos = proyectoDAO.getAll();
        for (Proyecto proyecto : proyectos) {
            proyecto.setHabilidades(extraerHabilidades(proyecto.getProyectoId()));
        }
        return proyectos;
    }

    public List<Proyecto> getAllUsuarioProyecto(int usuarioId) throws SQLException {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        List<Proyecto> proyectos = proyectoDAO.getAllUsuarioProyecto(usuarioId);
        for (Proyecto proyecto : proyectos) {
            proyecto.setHabilidades(extraerHabilidades(proyecto.getProyectoId()));
        }
        return proyectos;
    }

    public List<Proyecto> getAllUsuarioProyectoByCoincidence(int usuarioId, String titulo) throws SQLException {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        List<Proyecto> proyectos = proyectoDAO.getAllUsuarioProyectoByCategoria(usuarioId, titulo);
        for (Proyecto proyecto : proyectos) {
            proyecto.setHabilidades(extraerHabilidades(proyecto.getProyectoId()));
        }
        return proyectos;
    }

    public Proyecto getUsuarioProyectoById(int usuarioId, int proyectoId) throws SQLException, UserDataInvalidException {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        Optional<Proyecto> proyecto = proyectoDAO.getUsuarioProyectoById(proyectoId, usuarioId);
        if (proyecto.isEmpty()) throw new UserDataInvalidException("El proyecto no existe o no pertenece al usuario.");
        proyecto.get().setHabilidades(extraerHabilidades(proyecto.get().getProyectoId()));
        return proyecto.get();
    }

    public List<Proyecto> getAllProyectoByCategoria(int categoriaId, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("El usuario no tiene permisos para obtener los proyectos por categoría.");
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        List<Proyecto> proyectos = proyectoDAO.getAllProyectoByCategoria(categoriaId);
        for (Proyecto proyecto : proyectos) {
            proyecto.setHabilidades(extraerHabilidades(proyecto.getProyectoId()));
        }
        return proyectos;
    }

    public List<Proyecto> getAllProyectoByPresupuesto(double precioInicial, double precioFinal, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException("El usuario no tiene permisos para obtener los proyectos por presupuesto.");
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        List<Proyecto> proyectos = proyectoDAO.getAllProyectoByPresupuesto(precioInicial, precioFinal);
        for (Proyecto proyecto : proyectos) {
            proyecto.setHabilidades(extraerHabilidades(proyecto.getProyectoId()));
        }
        return proyectos;
    }

    public List<Proyecto> getAllProyectoByHabilidad(int habilidadId, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("El usuario no tiene permisos para obtener los proyectos por habilidad.");
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        List<Proyecto> proyectos = proyectoDAO.getAllProyectoByHabilidad(habilidadId);
        for (Proyecto proyecto : proyectos) {
            proyecto.setHabilidades(extraerHabilidades(proyecto.getProyectoId()));
        }
        return proyectos;
    }

    public List<Proyecto> getAllProyectoInAbiertoEstado(EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException("El usuario no tiene permisos para obtener los proyectos en estado abierto.");
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        List<Proyecto> proyectos = proyectoDAO.getAllProyectoInAbiertoEstado();
        for (Proyecto proyecto : proyectos) {
            proyecto.setHabilidades(extraerHabilidades(proyecto.getProyectoId()));
        }
        return proyectos;
    }

    public void deleteProyectoUsuario(int usuarioId, int proyectoId, int habilidadId, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        ProyectoHabilidadService proyectoHabilidadService = new ProyectoHabilidadService();
        proyectoHabilidadService.delete(habilidadId, rol, usuarioId, proyectoId);
    }

    public void insertProyectoHabilidad(ProyectoHabilidadRequest proyectoHabilidadRequest, EnumUsuario rol, int usuarioId, int proyectoId) throws SQLException, UserDataInvalidException {
        ProyectoHabilidadService proyectoHabilidadService = new ProyectoHabilidadService();
        proyectoHabilidadService.insertProyectoHabilidad(proyectoHabilidadRequest, rol, usuarioId, proyectoId);
    }

    public boolean ValidProyectoUsuario(int usuarioId, int proyectoId, Connection connection) throws SQLException {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        return proyectoDAO.existsProyectoUsuario(proyectoId, usuarioId, connection);
    }

    public boolean ValidProyectoUsuario(int usuarioId, int proyectoId) throws SQLException {
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        return proyectoDAO.existsProyectoUsuario(proyectoId, usuarioId);
    }
}
