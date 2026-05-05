package org.proyecto2.proyecto2.services.categoria;

import org.proyecto2.proyecto2.db.categoria.CategoriaDAO;
import org.proyecto2.proyecto2.dtos.categoria.CategoriaRequest;
import org.proyecto2.proyecto2.dtos.categoria.CategoriaUpdate;
import org.proyecto2.proyecto2.exceptions.EntityAlreadyExistsException;
import org.proyecto2.proyecto2.exceptions.UserDataInvalidException;
import org.proyecto2.proyecto2.models.categoria.Categoria;
import org.proyecto2.proyecto2.models.usuario.EnumUsuario;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CategoriaService {
    public void insert(CategoriaRequest categoriaRequest, EnumUsuario rol) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        if (!EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("No tiene el permiso para crear una categoria");
        Categoria categoria = new Categoria(categoriaRequest);
        if (!categoria.isValid())
            throw new UserDataInvalidException("El nombre y la descripción son obligatorios");
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        if (categoriaDAO.validNombre(categoria.getNombre()))
            throw new EntityAlreadyExistsException("El nombre ya existe");
        categoriaDAO.insert(categoria);
    }

    public void update(CategoriaUpdate categoriaUpdate, EnumUsuario rol) throws SQLException, UserDataInvalidException, EntityAlreadyExistsException {
        if (!EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("No tiene el permiso para actualizar una categoria");
        Categoria categoria = new Categoria(categoriaUpdate);
        if (!categoria.isValidUpdate())
            throw new UserDataInvalidException("El nombre y la descripción son obligatorios");
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        if (categoriaDAO.validNombreUpdate(categoria.getNombre(), categoria.getCategoriaId()))
            throw new EntityAlreadyExistsException("El nombre ya existe");
        categoriaDAO.update(categoria);
    }

    public void updateEstado(int categoriaId, EnumUsuario rol) throws SQLException, UserDataInvalidException {
        if (!EnumUsuario.Administrador.equals(rol))
            throw new UserDataInvalidException("No tiene permiso para actualizar el estado de una categoria");
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        if (categoriaDAO.getById(categoriaId).isEmpty())
            throw new UserDataInvalidException("La categoria seleccionada no se encontró");
        categoriaDAO.updateEstado(categoriaId);
    }

    public Categoria getById(int categoriaId) throws SQLException, UserDataInvalidException {
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        Optional<Categoria> categoria = categoriaDAO.getById(categoriaId);
        if (categoria.isEmpty()) throw new UserDataInvalidException("La categoria seleccionada no existe");
        return categoria.get();
    }

    public List<Categoria> getAll() throws SQLException {
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        return categoriaDAO.getAll();
    }

    public List<Categoria> getCategoriaByCoincidence(String nombre) throws SQLException {
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        return categoriaDAO.getCategoriaByCoincidence(nombre);
    }

    public List<Categoria> getAllCategoriaActivadas() throws SQLException {
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        return categoriaDAO.getAllCategoriaActivada();
    }
}
