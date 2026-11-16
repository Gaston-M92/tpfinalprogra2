package modelo;

import Enums.Especialidad;
import Exceptions.HorarioNoDisponibleException;
import Exceptions.PacienteNoEncontradoException;
import Exceptions.TurnoNoCancelableException;
import Exceptions.TurnoSuperpuestoException;
import gestor.GestorTurnos;
import modelo.Consultorio;
import modelo.HorarioAtencion;
import modelo.Medico;
import modelo.Paciente;
import modelo.Turno;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Programa principal del consultorio.
 * Muestra un menú por consola y delega cada opción en el gestor,
 * capturando las excepciones para mostrar su mensaje al usuario.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final GestorTurnos gestor = new GestorTurnos();
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    public static void main(String[] args) throws SQLException {
        cargarDatosDePrueba();

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Elegí una opción: ");
            ejecutarOpcion(opcion);
        } while (opcion != 0);

        System.out.println("Hasta luego.");
    }

    private static void mostrarMenu() {
        System.out.println("\n===== CONSULTORIO =====");
        System.out.println("1. Registrar paciente");
        System.out.println("2. Listar pacientes");
        System.out.println("3. Listar médicos");
        System.out.println("4. Buscar horarios libres por especialidad y fecha");
        System.out.println("5. Asignar turno");
        System.out.println("6. Cancelar turno");
        System.out.println("7. Ver turnos de un médico en una fecha");
        System.out.println("8. Ver turnos de un paciente");
        System.out.println("9. Ver reporte estadístico");
        System.out.println("0. Salir");
    }

    private static void ejecutarOpcion(int opcion) {
        try {
            switch (opcion) {
                case 1:
                    registrarPaciente();
                    break;
                case 2:
                    listarPacientes();
                    break;
                case 3:
                    listarMedicos();
                    break;
                case 4:
                    buscarHorariosLibres();
                    break;
                case 5:
                    asignarTurno();
                    break;
                case 6:
                    cancelarTurno();
                    break;
                case 7:
                    verTurnosDeMedico();
                    break;
                case 8:
                    verTurnosDePaciente();
                    break;
                case 9:
                    System.out.println(gestor.generarReporteEstadistico());
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } catch (PacienteNoEncontradoException | TurnoNoCancelableException
                 | HorarioNoDisponibleException | TurnoSuperpuestoException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("Error: formato de fecha u hora inválido.");
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

    // ---------- Opciones del menú ----------

    private static void registrarPaciente() throws SQLException {
        String dni = leerTexto("DNI: ");

        // Evita registrar dos pacientes con el mismo DNI
        try {
            gestor.buscarPaciente(dni);
            System.out.println("Ya existe un paciente con DNI " + dni);
            return;
        } catch (PacienteNoEncontradoException e) {
            // No existe: se puede registrar
        }

        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String telefono = leerTexto("Teléfono: ");
        int historiaClinica = leerEntero("Número de historia clínica: ");
        String obraSocial = leerTexto("Obra social: ");
        LocalDate fechaNacimiento = leerFecha("Fecha de nacimiento (dd/MM/aaaa): ");

        gestor.registrarPaciente(new Paciente(nombre, apellido, dni, telefono,
                historiaClinica, obraSocial, fechaNacimiento));
        System.out.println("Paciente registrado correctamente.");
    }

    private static void listarPacientes() {
        ArrayList<Paciente> pacientes = gestor.listarPacientes();
        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }
        for (Paciente p : pacientes) {
            System.out.println(p.generarFicha());
        }
    }

    private static void listarMedicos() {
        ArrayList<Medico> medicos = gestor.listarMedicos();
        if (medicos.isEmpty()) {
            System.out.println("No hay médicos registrados.");
            return;
        }
        for (Medico m : medicos) {
            System.out.println(m.generarFicha());
        }
    }

    private static void buscarHorariosLibres() {
        Especialidad especialidad = elegirEspecialidad();
        LocalDate fecha = leerFecha("Fecha (dd/MM/aaaa): ");

        ArrayList<LocalDateTime> libres = gestor.buscarHorariosLibres(especialidad, fecha);
        if (libres.isEmpty()) {
            System.out.println("No hay horarios libres para " + especialidad + " el " + fecha.format(FORMATO_FECHA));
            return;
        }
        System.out.println("Horarios libres de " + especialidad + " el " + fecha.format(FORMATO_FECHA) + ":");
        for (LocalDateTime h : libres) {
            System.out.println(" - " + h.format(FORMATO_HORA));
        }
    }

    private static void asignarTurno() throws PacienteNoEncontradoException,
            TurnoSuperpuestoException, HorarioNoDisponibleException, SQLException {
        String dniPaciente = leerTexto("DNI del paciente: ");
        String dniMedico = leerTexto("DNI del médico: ");

        Medico medico = gestor.buscarMedico(dniMedico);
        if (medico == null) {
            System.out.println("No existe un médico con DNI " + dniMedico);
            return;
        }

        LocalDateTime fechaHora = leerFechaHora("Fecha y hora (dd/MM/aaaa HH:mm): ");
        String motivo = leerTexto("Motivo de consulta: ");

        Turno turno = gestor.asignarTurno(dniPaciente, medico, fechaHora, motivo);
        System.out.println("Turno asignado correctamente. ID: " + turno.getId());
    }

    private static void cancelarTurno() throws TurnoNoCancelableException, SQLException {
        String id = leerTexto("ID del turno: ");
        gestor.cancelarTurno(id);
        System.out.println("Turno cancelado correctamente.");
    }

    private static void verTurnosDeMedico() {
        String dniMedico = leerTexto("DNI del médico: ");
        Medico medico = gestor.buscarMedico(dniMedico);
        if (medico == null) {
            System.out.println("No existe un médico con DNI " + dniMedico);
            return;
        }
        LocalDate fecha = leerFecha("Fecha (dd/MM/aaaa): ");
        mostrarTurnos(gestor.listarTurnosPorMedico(medico, fecha));
    }

    private static void verTurnosDePaciente() throws PacienteNoEncontradoException {
        String dni = leerTexto("DNI del paciente: ");
        gestor.buscarPaciente(dni); // lanza la excepción si no existe
        mostrarTurnos(gestor.listarTurnosPorPaciente(dni));
    }

    // ---------- Auxiliares ----------

    private static void mostrarTurnos(List<Turno> turnos) {
        if (turnos.isEmpty()) {
            System.out.println("No hay turnos para mostrar.");
            return;
        }
        for (Turno t : turnos) {
            System.out.println(t.getFechaHora().format(FORMATO_FECHA_HORA)
                    + " | Paciente: " + t.getPaciente().getNombreCompleto()
                    + " | Médico: " + t.getMedico().getNombreCompleto()
                    + " | " + t.getEstado()
                    + " | Motivo: " + t.getMotivoConsulta()
                    + " | ID: " + t.getId());
        }
    }

    private static Especialidad elegirEspecialidad() {
        Especialidad[] valores = Especialidad.values();
        System.out.println("Especialidades:");
        for (int i = 0; i < valores.length; i++) {
            System.out.println((i + 1) + ". " + valores[i]);
        }
        int eleccion;
        do {
            eleccion = leerEntero("Elegí una especialidad: ");
        } while (eleccion < 1 || eleccion > valores.length);
        return valores[eleccion - 1];
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Ingresá un número válido.");
            }
        }
    }

    private static LocalDate leerFecha(String mensaje) {
        return LocalDate.parse(leerTexto(mensaje), FORMATO_FECHA);
    }

    private static LocalDateTime leerFechaHora(String mensaje) {
        return LocalDateTime.parse(leerTexto(mensaje), FORMATO_FECHA_HORA);
    }

    // ---------- Datos de prueba ----------

    private static void cargarDatosDePrueba() throws SQLException {
        Consultorio c1 = new Consultorio(1, 1);
        Consultorio c2 = new Consultorio(2, 1);
        Consultorio c3 = new Consultorio(3, 2);

        ArrayList<HorarioAtencion> horariosClinica = new ArrayList<>(List.of(
                new HorarioAtencion(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(13, 0), c1),
                new HorarioAtencion(DayOfWeek.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(13, 0), c1)));
        ArrayList<HorarioAtencion> horariosPediatria = new ArrayList<>(List.of(
                new HorarioAtencion(DayOfWeek.TUESDAY, LocalTime.of(14, 0), LocalTime.of(18, 0), c2),
                new HorarioAtencion(DayOfWeek.THURSDAY, LocalTime.of(14, 0), LocalTime.of(18, 0), c2)));
        ArrayList<HorarioAtencion> horariosCardiologia = new ArrayList<>(List.of(
                new HorarioAtencion(DayOfWeek.FRIDAY, LocalTime.of(8, 0), LocalTime.of(12, 0), c3)));

        gestor.registrarMedico(new Medico("Laura", "Gómez", "20111222", "2234111111",
                "MP-1001", Especialidad.CLINICA_MEDICA, 30, horariosClinica));
        gestor.registrarMedico(new Medico("Carlos", "Pérez", "20333444", "2234222222",
                "MP-1002", Especialidad.PEDIATRIA, 20, horariosPediatria));
        gestor.registrarMedico(new Medico("Ana", "Rodríguez", "20555666", "2234333333",
                "MP-1003", Especialidad.CARDIOLOGIA, 30, horariosCardiologia));

        gestor.registrarPaciente(new Paciente("Juan", "Martínez", "30111222", "2235444444",
                1, "OSDE", LocalDate.of(1990, 5, 12)));
        gestor.registrarPaciente(new Paciente("María", "López", "32333444", "2235555555",
                2, "Swiss Medical", LocalDate.of(1985, 11, 3)));
    }
}