package co.edu.uptc.model;

public class Medico {
    private int id;
    private String nombre;
    private String especialidad;
    private String contrasena;

    public Medico(String nombre, String especialidad, String contrasena) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.contrasena = contrasena;
    }

    public Medico() {
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getEspecialidad() {
        return especialidad;
    }
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public String toString() {
        return "Medico [id=" + id + ", nombre=" + nombre + ", especialidad=" + especialidad + "]";
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    
}
