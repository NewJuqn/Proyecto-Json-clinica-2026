package co.edu.uptc.repository;

import java.util.List;

public interface Repository<T> {

    void save(T entity);
    List<T> findAll();
    void delete(int id);
    void update(int id, T entity);
    T BuscarID(int id);
    int generarID();
}
