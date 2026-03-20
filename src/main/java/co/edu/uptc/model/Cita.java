package co.edu.uptc.model;

import java.time.LocalDateTime;

public class Cita {
    private int  id;
    private LocalDateTime fechaCreacion;
    private Medico medico;
    private Paciente paciente;
    private LocalDateTime fechaCita;

    public Cita(Medico medico, Paciente paciente, LocalDateTime fechaCita) {
        this.medico = medico;
        this.paciente = paciente;
        this.fechaCreacion = LocalDateTime.now(); 
        this.fechaCita = fechaCita;
    }
    public Cita() {
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Medico getMedico() {
        return medico;
    }
    public void setMedico(Medico medico) {
        this.medico = medico;
    }
    public Paciente getPaciente() {
        return paciente;
    }
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public LocalDateTime getFechaCita() {
        return fechaCita;
    }
    public void setFechaCita(LocalDateTime fechaCita) {
        this.fechaCita = fechaCita;
    }
    @Override
    public String toString() {
        return "Cita [id=" + id + ", fechaCreacion=" + fechaCreacion + ", medico=" + medico + ", paciente=" + paciente
                + ", fechaCita=" + fechaCita + "]";
    }
    
    
}
