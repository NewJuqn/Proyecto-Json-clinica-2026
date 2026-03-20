package co.edu.uptc.service;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import co.edu.uptc.Exceptions.IDNotFoundException;
import co.edu.uptc.Util.Rutas;
import co.edu.uptc.model.Medico;
import co.edu.uptc.repository.JsonRepository;

public class MedicoService {
    private JsonRepository<Medico> repository;

    public MedicoService() {

        Type type = new TypeToken<List<Medico>>() {
        }.getType();
        repository = new JsonRepository<>(Rutas.MEDICOS, type);
    }

    public void crearMedico(Medico medico) {
        medico.setId(repository.generarID());
        repository.save(medico);
    }

    public List<Medico> listar() {
        return repository.findAll();
    }

    public Medico buscarPorId(int id) throws IDNotFoundException {
        return repository.BuscarID(id);
    }

    public void eliminar(int id) throws IDNotFoundException {
        repository.delete(id);
    }

    public void actualizar(int id, Medico medico) throws IDNotFoundException {
        repository.update(id, medico);
    }

    public Medico seleccionarMedico(int opcion) {
        return repository.findAll().get(opcion - 1);
    }
}
