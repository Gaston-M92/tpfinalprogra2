package Interfaces;

import java.util.List;

public interface DAO<T>{
    void insertar(T objeto);
    void actualizar(T objeto);
    void eliminar(String id);
    T buscarPorId(String id);
    List<T> listarTodos(T objeto);
}
