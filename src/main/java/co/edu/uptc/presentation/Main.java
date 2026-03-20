package co.edu.uptc.presentation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import co.edu.uptc.Exceptions.IDNotFoundException;
import co.edu.uptc.Util.SesionActual;
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

    private static final String ADMIN_USER = "Admin";
    private static final String ADMIN_PASS = "Admin 123";

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
            System.out.println("""

                    ---CLINICAL SYSTEM ---
                    1. Login
                    2. Registration
                    0. Salir
                    Enter your option:
                    """);
            op = sc.nextInt();
            sc.nextLine();
            switch (op) {
                case 1:
                    login();
                    break;
                case 2:
                    registro();
                    break;
                case 0:
                    System.out.println("See you later.");
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
    private void login() {
        System.out.println("ID:");
        String usuario = sc.nextLine();
        System.out.println("Password:");
        String contrasena = sc.nextLine();

        //Admin
        if (usuario.equals(ADMIN_USER) && contrasena.equals(ADMIN_PASS)) {
            SesionActual.iniciarSesionAdmin();
            System.out.println("Welcome Admin");
            menuAdmin();
            return;
        }

        //Paciente o Medico
        try {
            int id = Integer.parseInt(usuario);
            try {
                Paciente p = pacienteService.buscarPorId(id);
                if (p.getContrasena().equals(contrasena)) {
                    SesionActual.iniciarSesionPaciente(p);
                    System.out.println("Welcome patient " + p.getNombre());
                    menuPaciente();
                    return;
                }
            } catch (IDNotFoundException e) {
                System.out.println(e.getMessage());
            }
            try {
                Medico m = medicoService.buscarPorId(id);
                if (m.getContrasena().equals(contrasena)) {
                    SesionActual.iniciarSesionMedico(m);
                    System.out.println("Welcome Dr. " + m.getNombre());
                    menuMedico();
                    return;
                }
            } catch (IDNotFoundException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("Incorrect credentials");

        } catch (NumberFormatException e) {
            System.out.println("invalid user");
        }
    }
    private void registro() {
        System.out.println("Id:");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.println("Name:");
        String nombre = sc.nextLine();

        System.out.println("Age:");
        int edad = sc.nextInt();
        sc.nextLine();

        System.out.println("Password:");
        String contrasena = sc.nextLine();

        pacienteService.crear(new Paciente(id, nombre, edad, contrasena));
        System.out.println("Successful registration. You can now log in.");
    }
    //Menu paciente
    private void menuPaciente() {
        int op = -1;
        Paciente yo = SesionActual.getPacienteActual();
        do {
            System.out.println("""

                    ---PATIENT MENU ---
                    1. Create appointment
                    2. View my appointments
                    3. Cancel my appointment
                    4. Modify my appointment date
                    5. See list of doctors
                    6. Modify my data
                    0. Log out
                    Enter your option:
                    """);
            op = sc.nextInt();
            sc.nextLine();
            try {
                switch (op) {
                    case 1:
                        crearCitaPaciente(yo);
                        break;
                    case 2:
                        List<Cita> misCitas = citaService.listarPorPaciente(yo.getId());
                        if (misCitas.isEmpty()) {
                            System.out.println("You do not have registered appointments");
                        } else {
                            misCitas.forEach(System.out::println);
                        }
                        break;
                    case 3:
                        System.out.println("Appointment ID to cancel:");
                        int idCita = sc.nextInt();
                        sc.nextLine();
                        citaService.cancelarCita(idCita, yo.getId());
                        System.out.println("Successfully canceled appointment");
                        break;
                    case 4:
                        modificarFechaCitaPaciente(yo);
                        break;
                    case 5:
                        medicoService.listar().forEach(System.out::println);
                        break;
                    case 6:
                        modificarDatosPaciente(yo);
                        break;
                    case 0:
                        SesionActual.cerrarSesion();
                        System.out.println("Closed session");
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            } catch (IDNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } while (op != 0);
    }

    private void crearCitaPaciente(Paciente yo) {
        System.out.println("Year:");
        int anio = sc.nextInt(); sc.nextLine();
        System.out.println("Mes (1-12):");
        int mes = sc.nextInt(); sc.nextLine();
        System.out.println("Travel:");
        int dia = sc.nextInt(); sc.nextLine();
        System.out.println("Hora (0-23):");
        int hora = sc.nextInt(); sc.nextLine();
        System.out.println("Minutes:");
        int minuto = sc.nextInt(); sc.nextLine();

        LocalDateTime fechaCita = LocalDateTime.of(anio, mes, dia, hora, minuto);

        List<Medico> medicos = medicoService.listar();
        if (medicos.isEmpty()) {
            System.out.println("There are no doctors available");
            return;
        }
        System.out.println("Select a doctor:");
        for (int i = 0; i < medicos.size(); i++) {
            System.out.println((i + 1) + ". " + medicos.get(i));
        }
        int opMedico = sc.nextInt(); sc.nextLine();

        if (opMedico < 1 || opMedico > medicos.size()) {
            System.out.println("Invalid option");
            return;
        }

        Medico medico = medicoService.seleccionarMedico(opMedico);
        citaService.crear(new Cita(medico, yo, fechaCita));
        System.out.println("Appointment created successfully");
    }

    private void modificarFechaCitaPaciente(Paciente yo) {
        System.out.println("Appointment ID to modify:");
        int idCita = sc.nextInt(); sc.nextLine();

        System.out.println("New year:");
        int anio = sc.nextInt(); sc.nextLine();
        System.out.println("Nuevo mes (1-12):");
        int mes = sc.nextInt(); sc.nextLine();
        System.out.println("New day:");
        int dia = sc.nextInt(); sc.nextLine();
        System.out.println("Nueva hora (0-23):");
        int hora = sc.nextInt(); sc.nextLine();
        System.out.println("New minute:");
        int minuto = sc.nextInt(); sc.nextLine();

        LocalDateTime nuevaFecha = LocalDateTime.of(anio, mes, dia, hora, minuto);
        citaService.modificarFechaCita(idCita, yo.getId(), nuevaFecha);
        System.out.println("Date modified successfully");
    }

    private void modificarDatosPaciente(Paciente yo) {
        System.out.println("Nuevo nombre (actual: " + yo.getNombre() + "):");
        String nombre = sc.nextLine();
        System.out.println("Nueva edad (actual: " + yo.getEdad() + "):");
        int edad = sc.nextInt(); sc.nextLine();
        System.out.println("New password:");
        String contrasena = sc.nextLine();

        yo.setNombre(nombre);
        yo.setEdad(edad);
        yo.setContrasena(contrasena);

        pacienteService.actualizar(yo.getId(), yo);
        SesionActual.iniciarSesionPaciente(yo);
        System.out.println("Data updated correctly");
    }
    //Menu medico
    private void menuMedico() {
        int op = -1;
        Medico yo = SesionActual.getMedicoActual();
        do {
            System.out.println("""

                    ---MEDICAL MENU ---
                    1. View my appointments
                    2. List patients
                    3. Search by ID
                    4. Modify my data
                    0. Log out
                    Enter your option:
                    """);
            op = sc.nextInt(); sc.nextLine();
            try {
                switch (op) {
                    case 1:
                        List<Cita> misCitas = citaService.listarPorMedico(yo.getId());
                        if (misCitas.isEmpty()) {
                            System.out.println("You do not have assigned appointments");
                        } else {
                            misCitas.forEach(System.out::println);
                        }
                        break;
                    case 2:
                        pacienteService.listar().forEach(System.out::println);
                        break;
                    case 3:
                        menuBuscar();
                        break;
                    case 4:
                        modificarDatosMedico(yo);
                        break;
                    case 0:
                        SesionActual.cerrarSesion();
                        System.out.println("Closed session");
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            } catch (IDNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } while (op != 0);
    }

    private void modificarDatosMedico(Medico yo) {
        System.out.println("New name (actual: " + yo.getNombre() + "):");
        String nombre = sc.nextLine();
        System.out.println("New especiality (actual: " + yo.getEspecialidad() + "):");
        String esp = sc.nextLine();
        System.out.println("New password:");
        String contrasena = sc.nextLine();

        yo.setNombre(nombre);
        yo.setEspecialidad(esp);
        yo.setContrasena(contrasena);

        medicoService.actualizar(yo.getId(), yo);
        SesionActual.iniciarSesionMedico(yo); 
        System.out.println("Data updated correctly");
    }
    //Menu admin
    private void menuAdmin() {
        int op = -1;
        do {
            System.out.println("""

                    ---ADMIN MENU ---
                    1. Create
                    2. Edit
                    3. Delete
                    4. List
                    5. Search by ID
                    0. Log out
                    Enter your option:
                    """);
            op = sc.nextInt(); sc.nextLine();
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
                    SesionActual.cerrarSesion();
                    System.out.println("Closed session");
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        } while (op != 0);
    }
    private void menuCrear() {
        int m = -1;
        do {
            System.out.println("""

                    ---CREATE ---
                    1. Create doctor
                    2. Create patient
                    3. Create appointment
                    0. Back
                    Enter your option:
                    """);
            m = sc.nextInt(); sc.nextLine();
            switch (m) {
                case 1:
                    System.out.println("Name:");
                    String nombre = sc.nextLine();
                    System.out.println("Specialty:");
                    String esp = sc.nextLine();
                    System.out.println("Password:");
                    String contrasena = sc.nextLine();
                    medicoService.crearMedico(new Medico(nombre, esp, contrasena));
                    System.out.println("Doctor created correctly");
                    break;
                case 2:
                    System.out.println("Id:");
                    int id = sc.nextInt(); sc.nextLine();
                    System.out.println("Name:");
                    String nombreP = sc.nextLine();
                    System.out.println("Age:");
                    int edad = sc.nextInt(); sc.nextLine();
                    System.out.println("Password:");
                    String contrasenaP = sc.nextLine();
                    pacienteService.crear(new Paciente(id, nombreP, edad, contrasenaP));
                    System.out.println("Patient created successfully");
                    break;
                case 3:
                    List<Medico> medicos = medicoService.listar();
                    if (medicos.isEmpty()) {
                        System.out.println("There are no doctors available");
                        break;
                    }
                    System.out.println("Select a doctor:");
                    for (int i = 0; i < medicos.size(); i++) {
                        System.out.println((i + 1) + ". " + medicos.get(i));
                    }
                    int opMedico = sc.nextInt(); sc.nextLine();

                    List<Paciente> pacientes = pacienteService.listar();
                    if (pacientes.isEmpty()) {
                        System.out.println("There are no patients available");
                        break;
                    }
                    System.out.println("Select a patient:");
                    for (int i = 0; i < pacientes.size(); i++) {
                        System.out.println((i + 1) + ". " + pacientes.get(i));
                    }
                    int opPaciente = sc.nextInt(); sc.nextLine();

                    System.out.println("Year:"); int anio = sc.nextInt(); sc.nextLine();
                    System.out.println("Mes (1-12):"); int mes = sc.nextInt(); sc.nextLine();
                    System.out.println("Travel:"); int dia = sc.nextInt(); sc.nextLine();
                    System.out.println("Hora (0-23):"); int hora = sc.nextInt(); sc.nextLine();
                    System.out.println("Minutes:"); int minuto = sc.nextInt(); sc.nextLine();

                    Medico medico = medicoService.seleccionarMedico(opMedico);
                    Paciente paciente = pacientes.get(opPaciente - 1);
                    LocalDateTime fechaCita = LocalDateTime.of(anio, mes, dia, hora, minuto);

                    citaService.crear(new Cita(medico, paciente, fechaCita));
                    System.out.println("Appointment created successfully");
                    break;
                case 0:
                    System.out.println("Volviendo...");
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        } while (m != 0);
    }
    private void menuEditar() {
        int med = -1;
        do {
            System.out.println("""

                    ---EDIT ---
                    1. Edit doctor
                    2. Edit patient
                    3. Edit appointment
                    0. Back
                    Enter your option:
                    """);
            med = sc.nextInt(); sc.nextLine();
            try {
                switch (med) {
                    case 1:
                        System.out.println("Medical ID to edit:");
                        int idM = sc.nextInt(); sc.nextLine();
                        Medico medico = medicoService.buscarPorId(idM);
                        System.out.println("New name:");
                        String nombre = sc.nextLine();
                        System.out.println("New specialty:");
                        String esp = sc.nextLine();
                        medico.setNombre(nombre);
                        medico.setEspecialidad(esp);
                        medicoService.actualizar(idM, medico);
                        System.out.println("Medical updated correctly");
                        break;
                    case 2:
                        System.out.println("Patient ID to edit:");
                        int idP = sc.nextInt(); sc.nextLine();
                        Paciente paciente = pacienteService.buscarPorId(idP);
                        System.out.println("New name:");
                        String nombreP = sc.nextLine();
                        System.out.println("New age:");
                        int edad = sc.nextInt(); sc.nextLine();
                        paciente.setNombre(nombreP);
                        paciente.setEdad(edad);
                        pacienteService.actualizar(idP, paciente);
                        System.out.println("Patient successfully updated");
                        break;
                    case 3:
                        System.out.println("Appointment ID to edit:");
                        int idC = sc.nextInt(); sc.nextLine();
                        Cita cita = citaService.buscarPorId(idC);
                        System.out.println("New year:"); int anio = sc.nextInt(); sc.nextLine();
                        System.out.println("Nuevo mes (1-12):"); int mes = sc.nextInt(); sc.nextLine();
                        System.out.println("New day:"); int dia = sc.nextInt(); sc.nextLine();
                        System.out.println("Nueva hora (0-23):"); int hora = sc.nextInt(); sc.nextLine();
                        System.out.println("New minute:"); int minuto = sc.nextInt(); sc.nextLine();
                        cita.setFechaCita(LocalDateTime.of(anio, mes, dia, hora, minuto));
                        citaService.actualizar(idC, cita);
                        System.out.println("Appointment updated successfully");
                        break;
                    case 0:
                        System.out.println("Back...");
                        break;
                    default:
                        System.out.println("Invalid option");
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

                    ---DELETE ---
                    1. Delete doctor
                    2. Delete patient
                    3. Delete appointment
                    0. Back
                    Enter your option:
                    """);
            me = sc.nextInt(); sc.nextLine();
            try {
                switch (me) {
                    case 1:
                        System.out.println("Medical ID to delete:");
                        int idM = sc.nextInt(); sc.nextLine();
                        medicoService.eliminar(idM);
                        System.out.println("Doctor removed correctly");
                        break;
                    case 2:
                        System.out.println("Patient ID to delete:");
                        int idP = sc.nextInt(); sc.nextLine();
                        pacienteService.eliminar(idP);
                        System.out.println("Patient removed successfully");
                        break;
                    case 3:
                        System.out.println("Appointment ID to delete:");
                        int idC = sc.nextInt(); sc.nextLine();
                        citaService.eliminar(idC);
                        System.out.println("Appointment successfully deleted");
                        break;
                    case 0:
                        System.out.println("Volviendo...");
                        break;
                    default:
                        System.out.println("Invalid option");
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

                    ---LIST ---
                    1. List doctors
                    2. List patients
                    3. List quotes
                    0. Return
                    Enter your option:
                    """);
            ml = sc.nextInt(); sc.nextLine();
            switch (ml) {
                case 1:
                    medicoService.listar().forEach(System.out::println);
                    break;
                case 2:
                    pacienteService.listar().forEach(System.out::println);
                    break;
                case 3:
                    citaService.listar().forEach(System.out::println);
                    break;
                case 0:
                    System.out.println("Volviendo...");
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        } while (ml != 0);
    }
    private void menuBuscar() {
        int mb = -1;
        do {
            System.out.println("""

                    ---SEARCH BY ID ---
                    1. Find a doctor
                    2. Search patient
                    3. Find an appointment
                    0. Back
                    Enter your option:
                    """);
            mb = sc.nextInt(); sc.nextLine();
            if (mb == 0) {
                System.out.println("Volviendo...");
                break;
            }
            System.out.println("Enter the ID to search:");
            int id = sc.nextInt(); sc.nextLine();
            try {
                switch (mb) {
                    case 1:
                        System.out.println(medicoService.buscarPorId(id));
                        break;
                    case 2:
                        System.out.println(pacienteService.buscarPorId(id));
                        break;
                    case 3:
                        System.out.println(citaService.buscarPorId(id));
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            } catch (IDNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } while (mb != 0);
    }
}