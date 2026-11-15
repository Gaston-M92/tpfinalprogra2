package Interfaces;

import java.sql.SQLException;
import java.util.List;

/**
 * Operaciones de acceso a datos para una entidad del sistema.
 */

public interface DAO<T> {
    void insertar(T objeto) throws SQLException;
    void actualizar(T objeto) throws SQLException;
    void eliminar(String id) throws SQLException;
    T buscarPorId(String id) throws SQLException;
    List<T> listarTodos() throws SQLException;
}