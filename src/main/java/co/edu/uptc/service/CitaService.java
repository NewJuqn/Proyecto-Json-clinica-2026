package co.edu.uptc.service;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import co.edu.uptc.Util.Rutas;
import co.edu.uptc.model.Cita;
import co.edu.uptc.repository.JsonRepository;

public class CitaService {
    private JsonRepository<Cita> repository;

    public CitaService() {

        Type type = new TypeToken<List<Cita>>() {
        }.getType();
        repository = new JsonRepository<>(Rutas.CITAS, type);
    }

    public void crear(Cita cita) {
        cita.setId(repository.generarID());
        repository.save(cita);
    }

    public List<Cita> listar() {
        return repository.findAll();
    }

    public void eliminar(int id) {
        repository.delete(id);
    }

    public void actualizar(int id, Cita cita) {
        repository.update(id, cita);
    }

    public Cita buscarPorId(int id) {

        return repository.BuscarID(id);
    }

}
