package co.edu.uptc.repository;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import co.edu.uptc.Exceptions.IDNotFoundException;

public class JsonRepository<T> implements Repository<T> {

    private final String fileName;
    private final Gson gson;
    private final Type type;

    public JsonRepository(String fileName, Type type) {
        this.fileName = fileName;
        this.type = type;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        crearArchivoSiNoExiste();
    }

    private void crearArchivoSiNoExiste() {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                File parent = file.getParentFile();
                if (parent != null) {
                    parent.mkdirs();
                }
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("[]");
                }
                System.out.println("Archivo creado: " + fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void save(T entity) {
        List<T> data = findAll();
        data.add(entity);

        guardarEnArchivo(data);
    }

    @Override
    public List<T> findAll() {
        try (FileReader reader = new FileReader(fileName)) {
            List<T> resultado = gson.fromJson(reader, type);
            return resultado != null ? resultado : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void delete(int id) throws IDNotFoundException {
        List<T> data = findAll();
        boolean delete = data.removeIf(entity -> getEntityId(entity) == id);
        if (!delete) {
            throw new IDNotFoundException("ID no encontrado: " + id);
        }
        guardarEnArchivo(data);
    }

    @Override
    public void update(int id, T entity) throws IDNotFoundException {
        List<T> data = findAll();
        boolean encontrado = false;
        for (int i = 0; i < data.size(); i++) {
            if (getEntityId(data.get(i)) == id) {
                data.set(i, entity);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            throw new IDNotFoundException("ID no encontrado: " + id);
        }
        guardarEnArchivo(data);
    }

    private int getEntityId(T entity) {
        JsonObject json = gson.toJsonTree(entity).getAsJsonObject();
        return json.get("id").getAsInt();
    }

    private void guardarEnArchivo(List<T> data) {
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(data, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public T BuscarID(int id) {
        return findAll().stream()
                .filter(e -> getEntityId(e) == id)
                .findFirst()
                .orElseThrow(() -> new IDNotFoundException("No existe un registro con id: " + id));
    }
    @Override
    public int generarID() {
        return findAll().stream()
                .mapToInt(e -> getEntityId(e))
                .max()
                .orElse(0) + 1;
    }
}