package co.edu.uptc.service;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import co.edu.uptc.Util.Rutas;
import co.edu.uptc.model.Paciente;
import co.edu.uptc.repository.JsonRepository;

public class PacienteService {
    private JsonRepository<Paciente> repository;

    public PacienteService() {
        Type type = new TypeToken<List<Paciente>>() {
        }.getType();
        repository = new JsonRepository<>(Rutas.PACIENTES, type);
    }

    public void crear(Paciente paciente) {
        repository.save(paciente);
    }

    public List<Paciente> listar() {
        return repository.findAll();
    }

    public Paciente buscarPorId(int id) {

        return repository.BuscarID(id);
    }
    public void eliminar(int id) {
        repository.delete(id);
    }
    public void actualizar(int id, Paciente paciente) {
        repository.update(id, paciente);
    }
    
}
