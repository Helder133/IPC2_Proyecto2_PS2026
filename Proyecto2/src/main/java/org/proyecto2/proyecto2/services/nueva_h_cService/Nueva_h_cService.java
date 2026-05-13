package org.proyecto2.proyecto2.services.nueva_h_cService;

import org.proyecto2.proyecto2.db.config.DBConnection;
import org.proyecto2.proyecto2.db.nueva_h_c.Nueva_h_cDAO;
import org.proyecto2.proyecto2.dtos.nueva_h_c.Nueva_h_cRequest;
import org.proyecto2.proyecto2.dtos.nueva_h_c.Nueva_h_cUpdate;
import org.proyecto2.proyecto2.exceptions.EntityAlreadyExistsException;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.categoria.Categoria;
import org.proyecto2.proyecto2.models.habilidad.Habilidad;
import org.proyecto2.proyecto2.models.nueva_h_c.EnumNueva_h_cEstado;
import org.proyecto2.proyecto2.models.nueva_h_c.EnumNueva_h_cTipo;
import org.proyecto2.proyecto2.models.nueva_h_c.Nueva_h_c;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;
import org.proyecto2.proyecto2.services.categoria.CategoriaService;
import org.proyecto2.proyecto2.services.habilidad.HabilidadService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Nueva_h_cService {
    public void createNueva_h_c(Nueva_h_cRequest nueva_h_cRequest, EnumUsuario rol) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        Nueva_h_c nuevaHC = new Nueva_h_c(nueva_h_cRequest);
        if (!nuevaHC.isValid())
            throw new UserDataInvalidException("Los datos de la solicitud no son válidos. Verifique que el nombre y la descripción que estén correctamente ingresados.");
        if (EnumNueva_h_cTipo.CATEGORIA.equals(nuevaHC.getTipo())) {
            createSolicitudDeCategoria(nuevaHC, rol);
        } else if (EnumNueva_h_cTipo.HABILIDAD.equals(nuevaHC.getTipo())) {
            createSolicitudDeHabilidad(nuevaHC, rol);
        }
    }

    private void createSolicitudDeHabilidad(Nueva_h_c nuevaHC, EnumUsuario rol) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        if (!EnumUsuario.Freelancer.equals(rol))
            throw new UserDataInvalidException("No tienes permisos para crear una solicitud de nueva habilidad");
        HabilidadService habilidadService = new HabilidadService();
        Nueva_h_cDAO nuevaHCDAO = new Nueva_h_cDAO();
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        try {
            if (habilidadService.validNombre(nuevaHC.getNombre(), connection))
                throw new EntityAlreadyExistsException("Ya existe una habilidad con el mismo nombre");
            nuevaHCDAO.insert(nuevaHC, connection);
            connection.commit();
        } catch (SQLException | EntityAlreadyExistsException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private void createSolicitudDeCategoria(Nueva_h_c nuevaHC, EnumUsuario rol) throws SQLException, EntityAlreadyExistsException, UserDataInvalidException {
        if (!EnumUsuario.Cliente.equals(rol))
            throw new UserDataInvalidException("No tienes permisos para crear una solicitud de nueva categoría");
        CategoriaService categoriaService = new CategoriaService();
        Nueva_h_cDAO nuevaHCDAO = new Nueva_h_cDAO();
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);
        try {
            if (categoriaService.validNombre(nuevaHC.getNombre(), connection))
                throw new EntityAlreadyExistsException("Ya existe una categoría con el mismo nombre");
            nuevaHCDAO.insert(nuevaHC, connection);
            connection.commit();
        } catch (SQLException | EntityAlreadyExistsException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void updateNuevo(Nueva_h_cUpdate nueva_h_cUpdate) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        Nueva_h_c nuevaHC = new Nueva_h_c(nueva_h_cUpdate);
        if (nuevaHC.isValidUpdate())
            throw new UserDataInvalidException("Los nuevos datos de la solicitud no son válidos. Verifique que el nombre y la descripción que estén correctamente ingresados.");
        Nueva_h_cDAO nuevaHCDAO = new Nueva_h_cDAO();
        Nueva_h_c nuevaHC1 = nuevaHCDAO.getById(nuevaHC.getSolicitudId()).orElseThrow(() -> new UserDataInvalidException("La solicitud no existe"));

        if (EnumNueva_h_cTipo.CATEGORIA.equals(nuevaHC1.getTipo())) {
            CategoriaService categoriaService = new CategoriaService();
            if (categoriaService.validNombre(nuevaHC.getNombre()))
                throw new EntityAlreadyExistsException("Ya existe una categoría con el mismo nombre");
        } else if (EnumNueva_h_cTipo.HABILIDAD.equals(nuevaHC1.getTipo())) {
            HabilidadService habilidadService = new HabilidadService();
            if (habilidadService.validNombre(nuevaHC.getNombre()))
                throw new EntityAlreadyExistsException("Ya existe una habilidad con el mismo nombre");
        }

        nuevaHCDAO.update(nuevaHC);
    }

    public void UpdateEstadoRechazar(EnumUsuario rol, int solicitudId) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("No tienes permisos para actualizar el estado de la solicitud");
        Nueva_h_cDAO nuevaHCDAO = new Nueva_h_cDAO();
        nuevaHCDAO.getById(solicitudId).orElseThrow(() -> new UserDataInvalidException("La solicitud no existe"));
        nuevaHCDAO.update(EnumNueva_h_cEstado.RECHAZADA, solicitudId);
    }

    public void updateEstadoAceptada(EnumUsuario rol, int solicitudId) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        if (!EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("No tienes permisos para actualizar el estado de la solicitud");
        Nueva_h_cDAO nuevaHCDAO = new Nueva_h_cDAO();
        Nueva_h_c nuevaHC = nuevaHCDAO.getById(solicitudId).orElseThrow(() -> new UserDataInvalidException("La solicitud no existe"));
        Connection conexion = DBConnection.getInstance().getConnection();
        conexion.setAutoCommit(false);
        try {
            if (EnumNueva_h_cTipo.CATEGORIA.equals(nuevaHC.getTipo())) {
                createCategoria(nuevaHC, conexion);
            } else if (EnumNueva_h_cTipo.HABILIDAD.equals(nuevaHC.getTipo())) {
                createHabilidad(nuevaHC, conexion);
            }
            nuevaHCDAO.update(EnumNueva_h_cEstado.ACEPTADA, solicitudId, conexion);
            conexion.commit();
        } catch (SQLException | UserDataInvalidException | EntityAlreadyExistsException e) {
            conexion.rollback();
            throw e;
        } finally {
            conexion.setAutoCommit(true);
        }
    }

    private void createHabilidad(Nueva_h_c nuevaHC, Connection connection) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        HabilidadService habilidadService = new HabilidadService();
        Habilidad habilidad = new Habilidad(nuevaHC.getNombre(), nuevaHC.getDescripcion());
        habilidadService.createHabilidad(habilidad, connection);
    }

    private void createCategoria(Nueva_h_c nuevaHC, Connection connection) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        CategoriaService categoriaService = new CategoriaService();
        Categoria categoria = new Categoria(nuevaHC.getNombre(), nuevaHC.getDescripcion());
        categoriaService.createCategoria(categoria, connection);
    }

    public Nueva_h_c getById(int solicitudId) throws SQLException, UserDataInvalidException {
        Nueva_h_cDAO nuevaHCDAO = new Nueva_h_cDAO();
        return nuevaHCDAO.getById(solicitudId).orElseThrow(() -> new UserDataInvalidException("La solicitud no existe"));
    }

    public List<Nueva_h_c> getAll(EnumUsuario rol) throws SQLException {
        if (!EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("No tienes permisos para ver las solicitudes");
        Nueva_h_cDAO nuevaHCDAO = new Nueva_h_cDAO();
        return nuevaHCDAO.getAll();
    }

    public List<Nueva_h_c> getAllByUsuario(int usuarioId) throws SQLException {
        Nueva_h_cDAO nuevaHCDAO = new Nueva_h_cDAO();
        return nuevaHCDAO.getAllNuevoByUsuario(usuarioId);
    }

}
