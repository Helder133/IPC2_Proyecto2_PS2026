package org.proyecto2.proyecto2.db.config;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CRUD <T>{
    void insert(T t) throws SQLException;
    void update(T t) throws SQLException;
    void delete(int id) throws SQLException;
    Optional<T> getById(int id) throws SQLException;
    List<T> getAll() throws SQLException;
}
