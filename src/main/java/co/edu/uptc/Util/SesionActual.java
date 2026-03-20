package co.edu.uptc.Util;

import co.edu.uptc.model.Medico;
import co.edu.uptc.model.Paciente;

public class SesionActual {
    private static Paciente pacienteActual;
    private static Medico medicoActual;
    private static Rol rolActual;

    private SesionActual() {

    }

    public static void iniciarSesionPaciente(Paciente paciente) {
        pacienteActual = paciente;
        medicoActual = null;
        rolActual = Rol.PACIENTE;
    }

    public static void iniciarSesionMedico(Medico medico) {
        medicoActual = medico;
        pacienteActual = null;
        rolActual = Rol.MEDICO;
    }

    public static void iniciarSesionAdmin() {
        pacienteActual = null;
        medicoActual = null;
        rolActual = Rol.ADMIN;
    }

    public static void cerrarSesion() {
        pacienteActual = null;
        medicoActual = null;
        rolActual = null;
    }

    public static Paciente getPacienteActual() {
        return pacienteActual;
    }

    public static Medico getMedicoActual() {
        return medicoActual;
    }

    public static Rol getRolActual() {
        return rolActual;
    }
}
