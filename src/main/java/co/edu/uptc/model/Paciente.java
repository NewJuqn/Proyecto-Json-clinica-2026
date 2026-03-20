package co.edu.uptc.model;

public class Paciente {
    private int id;
    private String nombre;
    private int edad;
    private String contrasena;
    
    public Paciente(int id,String nombre, int edad, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.contrasena = contrasena;
    }
    public Paciente() {
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
    public int getEdad() {
        return edad;
    }
    public void setEdad(int edad) {
        this.edad = edad;
    }
    
    @Override
    public String toString() {
        return "Paciente [id=" + id + ", nombre=" + nombre + ", edad=" + edad + "]";
    }
    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

}
