package co.edu.uptc.service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.reflect.TypeToken;

import co.edu.uptc.Exceptions.IDNotFoundException;
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
    
    public List<Cita> listarPorPaciente(int idPaciente) {
        return repository.findAll().stream()
                .filter(c -> c.getPaciente().getId() == idPaciente)
                .collect(Collectors.toList());
    }

    public List<Cita> listarPorMedico(int idMedico) {
        return repository.findAll().stream()
                .filter(c -> c.getMedico().getId() == idMedico)
                .collect(Collectors.toList());
    }

    public void cancelarCita(int idCita, int idPaciente) {
        Cita cita = repository.BuscarID(idCita);
        if (cita.getPaciente().getId() != idPaciente) {
            throw new IDNotFoundException("No puedes cancelar una cita que no es tuya");
        }
        repository.delete(idCita);
    }

    public void modificarFechaCita(int idCita, int idPaciente, LocalDateTime nuevaFecha) {
        Cita cita = repository.BuscarID(idCita);
        if (cita.getPaciente().getId() != idPaciente) {
            throw new IDNotFoundException("No puedes modificar una cita que no es tuya");
        }
        cita.setFechaCita(nuevaFecha);
        repository.update(idCita, cita);
    }
}
