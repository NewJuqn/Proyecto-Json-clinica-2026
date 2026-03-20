package co.edu.uptc.presentation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import co.edu.uptc.Exceptions.IDNotFoundException;
import co.edu.uptc.model.Cita;
import co.edu.uptc.model.Medico;
import co.edu.uptc.model.Paciente;
import co.edu.uptc.service.CitaService;
import co.edu.uptc.service.MedicoService;
import co.edu.uptc.service.PacienteService;

public class Main {
    private MedicoService medicoService;
    private PacienteService pacienteService;
    private CitaService citaService;
    private Scanner sc;

    public Main() {
        sc = new Scanner(System.in);
        medicoService = new MedicoService();
        pacienteService = new PacienteService();
        citaService = new CitaService();
    }

    public static void main(String[] args) {
        new Main().iniciar();
    }

    private void iniciar() {
        int op = -1;
        while (op != 0) {

            System.out.println("\n--- SISTEMA CLINICA ---");
            System.out.println("""
                    1. Menu Crear
                    2. Menu Editar
                    3. Menu Eliminar
                    4. Menu Listar
                    5. Menu Buscar por ID
                    0. Salir
                    Digite su opcion:
                    """);

            op = sc.nextInt();
            sc.nextLine();

            switch (op) {

                case 1:
                    menuCrear();
                    break;
                case 2:
                    menuEditar();
                    break;

                case 3:
                    menuEliminar();
                    break;

                case 4:
                    menuListar();
                    break;

                case 5:
                    menuBuscar();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    System.out.println("Gracias por usar el sistema de citas de la clínica.");
            }
        }
    }

    private void menuCrear() {
        int m = -1;
        do {
            System.out.println("""
                    1. Crear medico
                    2. Crear paciente
                    3. Crear cita
                    0. Volver al menú principal
                    Digite su opción:
                    """);
            m = sc.nextInt();
            sc.nextLine();
            switch (m) {
                case 1:
                    System.out.println("Nombre:");
                    String nombre = sc.nextLine();

                    System.out.println("Especialidad:");
                    String esp = sc.nextLine();

                    medicoService.crearMedico(new Medico(nombre, esp));
                    break;
                case 2:
                    System.out.println("ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Nombre:");
                    String nombreP = sc.nextLine();

                    System.out.println("Edad:");
                    int edad = sc.nextInt();

                    pacienteService.crear(new Paciente(id, nombreP, edad));
                    break;
                case 3:
                    System.out.println("ID paciente:");
                    int idPaciente = sc.nextInt();
                    sc.nextLine();

                    System.out.println();

                    System.out.println("Año:");
                    int anio = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Mes (1-12):");
                    int mes = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Día:");
                    int dia = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Hora (0-23):");
                    int hora = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Minuto:");
                    int minuto = sc.nextInt();
                    sc.nextLine();

                    LocalDateTime fechaCita = LocalDateTime.of(anio, mes, dia, hora, minuto);
                    List<Medico> medicos = medicoService.listar();
                    for (int i = 0; i < medicos.size(); i++) {
                        System.out.println((i + 1) + ". " + medicos.get(i));
                    }
                    int opMedico = sc.nextInt();
                    sc.nextLine();
                    Medico medico = medicoService.seleccionarMedico(opMedico);
                    Paciente paciente = pacienteService.buscarPorId(idPaciente);
                    if (medico == null || paciente == null) {

                        System.out.println("Medico o paciente no encontrado");

                    } else {
                        citaService.crear(new Cita(medico, paciente, fechaCita));
                        System.out.println("Cita creada correctamente");
                    }
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
            }
        } while (m != 0);
    }

    private void menuEditar() {
        int med = -1;
        do {
            System.out.println("""
                    1. Editar medico
                    2. Editar paciente
                    3. Editar cita
                    0. Volver al menú principal
                    Digite su opción:
                    """);
            med = sc.nextInt();
            sc.nextLine();
            try {
                switch (med) {
                    case 1:
                        System.out.println("ID medico a editar:");
                        int idM = sc.nextInt();
                        sc.nextLine();

                        Medico medico = medicoService.buscarPorId(idM);
                        System.out.println("Nuevo nombre:");
                        String nombre = sc.nextLine();

                        System.out.println("Nueva especialidad:");
                        String esp = sc.nextLine();

                        medico.setNombre(nombre);
                        medico.setEspecialidad(esp);

                        medicoService.actualizar(idM, medico);
                        System.out.println("Medico actualizado correctamente");
                        break;
                    case 2:
                        System.out.println("ID paciente a editar:");
                        int idP = sc.nextInt();
                        sc.nextLine();

                        Paciente paciente = pacienteService.buscarPorId(idP);

                        if (paciente == null) {
                            System.out.println("Paciente no encontrado");
                        } else {
                            System.out.println("Nuevo nombre:");
                            String nombreP = sc.nextLine();

                            System.out.println("Nueva edad:");
                            int edad = sc.nextInt();
                            sc.nextLine();

                            paciente.setNombre(nombreP);
                            paciente.setEdad(edad);

                            pacienteService.actualizar(idP, paciente);
                            System.out.println("Paciente actualizado correctamente");
                        }
                        break;
                    case 3:
                        System.out.println("ID cita a editar:");
                        int idC = sc.nextInt();
                        sc.nextLine();

                        Cita cita = citaService.buscarPorId(idC);

                        if (cita == null) {
                            System.out.println("Cita no encontrada");
                        } else {
                            System.out.println("Año:");
                            int anio = sc.nextInt();
                            sc.nextLine();
                            System.out.println("Mes (1-12):");
                            int mes = sc.nextInt();
                            sc.nextLine();
                            System.out.println("Día:");
                            int dia = sc.nextInt();
                            sc.nextLine();
                            System.out.println("Hora (0-23):");
                            int hora = sc.nextInt();
                            sc.nextLine();
                            System.out.println("Minuto:");
                            int minuto = sc.nextInt();
                            sc.nextLine();

                            LocalDateTime fechaCita = LocalDateTime.of(anio, mes, dia, hora, minuto);
                            cita.setFechaCita(fechaCita);
                            citaService.actualizar(idC, cita);
                            System.out.println("Cita actualizada correctamente");
                        }
                        break;
                    case 0:
                        System.out.println("Volviendo al menú principal...");
                        break;
                }
            } catch (IDNotFoundException e) {
                System.out.println(e.getMessage());
            }

        } while (med != 0);
    }

    private void menuEliminar() {
        int me = -1;
        do {
            System.out.println("""
                    1. Eliminar medico
                    2. Eliminar paciente
                    3. Eliminar cita
                    0. Volver al menú principal
                    Digite su opción:
                    """);
            me = sc.nextInt();
            sc.nextLine();
            try {
                switch (me) {
                    case 1:
                        System.out.println("ID medico a eliminar:");
                        int idM = sc.nextInt();
                        sc.nextLine();
                        medicoService.eliminar(idM);
                        break;
                    case 2:
                        System.out.println("ID paciente a eliminar:");
                        int idP = sc.nextInt();
                        sc.nextLine();
                        pacienteService.eliminar(idP);
                        break;
                    case 3:
                        System.out.println("ID cita a eliminar:");
                        int idC = sc.nextInt();
                        sc.nextLine();
                        citaService.eliminar(idC);
                        break;
                    case 0:
                        System.out.println("Volviendo al menú principal...");
                        break;
                }
            } catch (IDNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } while (me != 0);
    }

    private void menuListar() {
        int ml = -1;
        do {
            System.out.println("""
                    1. Listar medicos
                    2. Listar pacientes
                    3. Listar citas
                    0. Volver al menú principal
                    Digite su opción:
                    """);
            ml = sc.nextInt();
            sc.nextLine();
            switch (ml) {
                case 1:
                    medicoService.listar()
                            .forEach(m -> System.out.println(
                                    m.getId() + " - " + m.getNombre() + " - " + m.getEspecialidad()));
                    break;
                case 2:
                    pacienteService.listar()
                            .forEach(p -> System.out.println(
                                    p.getId() + " - " + p.getNombre() + " - " + p.getEdad()));
                    break;
                case 3:
                    citaService.listar()
                            .forEach(c -> System.out.println(
                                    c.getId() +
                                            " | Medico: " + c.getMedico().getNombre() +
                                            " | Paciente: " + c.getPaciente().getNombre() +
                                            " | Fecha: " + c.getFechaCita()));
                    break;
                case 0:
                    System.out.println("Volviendo al menu principal...");
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        } while (ml != 0);
    }

    private void menuBuscar() {
        int mb = -1;
        do {
            System.out.println("""
                    Por ID
                    1. Buscar medicos
                    2. Buscar pacientes
                    3. Buscar citas
                    0. Salir al menu principal
                    """);
            mb = sc.nextInt();
            sc.nextLine();
            System.out.println("Ingresa el ID a  buscar");
            int id = sc.nextInt();
            sc.nextLine();
            try {
                switch (mb) {
                    case 1:
                        medicoService.buscarPorId(id);
                        break;
                    case 2:
                        pacienteService.buscarPorId(id);
                        break;
                    case 3:
                        citaService.buscarPorId(id);
                        break;
                    case 0:
                        System.out.println("Saliendo al menu principal...");
                        break;
                    default:
                        System.out.println("Opcion invalida");
                        break;
                }
            } catch (IDNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } while (mb != 0);

    }
}