package gestor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.sql.SQLException;
import java.time.DayOfWeek;
import dao.MedicoDAO;
import dao.PacienteDAO;
import dao.TurnoDAO;

import Enums.Especialidad;
import Enums.EstadoTurno;
import Exceptions.HorarioNoDisponibleException;
import Exceptions.PacienteNoEncontradoException;
import Exceptions.TurnoNoCancelableException;
import Exceptions.TurnoSuperpuestoException;
import Interfaces.Reportable;
import modelo.*;

/**
 * Clase central del sistema: coordina pacientes, medicos y turnos.
 * Aplica las reglas de negocio (horarios de atencion, superposicion de turnos,
 * cancelaciones) y usa los DAO para guardar y cargar los datos desde MySQL.
 */
public class GestorTurnos implements Reportable {
    private Gestor<Paciente> pacientes;
    private Gestor<Medico> medicos;
    private Gestor<Turno> turnos;
    private ArrayList<Consultorio> consultorios;
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final MedicoDAO medicoDAO = new MedicoDAO();
    private final TurnoDAO turnoDAO = new TurnoDAO();

    public GestorTurnos() {
        pacientes = new Gestor<>();
        medicos = new Gestor<>();
        turnos = new Gestor<>();
        consultorios = new ArrayList<>();
    }

    /**
     * Carga los datos al iniciar el sistema.
     * Pacientes, medicos y turnos se leen de la base de datos,
     * los consultorios y los horarios de atencion se crean en memoria.
     */

    public void cargarDatos() throws SQLException {
        Consultorio c1 = new Consultorio(1, 1);
        Consultorio c2 = new Consultorio(2, 1);
        Consultorio c3 = new Consultorio(3, 2);
        consultorios.add(c1);
        consultorios.add(c2);
        consultorios.add(c3);

        for (Paciente p : pacienteDAO.listarTodos()) {
            pacientes.agregar(p);
        }

        for (Medico m : medicoDAO.listarTodos()) {
            asignarHorarios(m);
            medicos.agregar(m);
        }

        //Se rearma cada turno con el paciente y el medico ya cargados en memoria, para que todo el sistema comparta los mismos objetos.

        for (Turno t : turnoDAO.listarTodos()) {
            Paciente p = pacientes.buscarPorId(t.getPaciente().getDni());
            Medico m = medicos.buscarPorId(t.getMedico().getDni());
            turnos.agregar(new Turno(t.getId(), p, m, t.getFechaHora(), t.getMotivoConsulta(), t.getEstado()));
        }
    }

    /**
     * Asigna los horarios de atencion a un medico (se manejan en memoria).
     * Los medicos de ejemplo tienen horarios propios, el resto atiende
     * de lunes a viernes de 9 a 13 en el consultorio 1.
     */
    private void asignarHorarios(Medico m) {
        Consultorio c1 = consultorios.get(0);
        Consultorio c2 = consultorios.get(1);
        Consultorio c3 = consultorios.get(2);

        switch (m.getDni()) {
            case "20123456" -> { // Carlos Fernandez - Clinica medica
                m.agregarHorario(new HorarioAtencion(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0), c1));
                m.agregarHorario(new HorarioAtencion(DayOfWeek.WEDNESDAY, LocalTime.of(14, 0), LocalTime.of(18, 0), c1));
            }
            case "25987654" -> { // Ana Lopez - Pediatria
                m.agregarHorario(new HorarioAtencion(DayOfWeek.TUESDAY, LocalTime.of(8, 0), LocalTime.of(12, 0), c2));
                m.agregarHorario(new HorarioAtencion(DayOfWeek.WEDNESDAY, LocalTime.of(10, 0), LocalTime.of(13, 0), c2));
                m.agregarHorario(new HorarioAtencion(DayOfWeek.THURSDAY, LocalTime.of(8, 0), LocalTime.of(12, 0), c2));
            }
            case "28456789" -> { // Jorge Martinez - Cardiologia
                m.agregarHorario(new HorarioAtencion(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(13, 0), c3));
                m.agregarHorario(new HorarioAtencion(DayOfWeek.FRIDAY, LocalTime.of(9, 0), LocalTime.of(13, 0), c3));
            }
            default -> {
                for (DayOfWeek dia : new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                        DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY}) {
                    m.agregarHorario(new HorarioAtencion(dia, LocalTime.of(9, 0), LocalTime.of(13, 0), c1));
                }
            }
        }
    }

    /**
     * Registra un paciente nuevo: primero lo guarda en la base y, si no hay error, lo agrega en memoria.
     */

    public void registrarPaciente(Paciente p) throws SQLException {
        pacienteDAO.insertar(p);
        pacientes.agregar(p);
    }


    public Paciente buscarPaciente(String dni) throws PacienteNoEncontradoException {
        Paciente p = pacientes.buscarPorId(dni);

        if (p == null) {
            throw new PacienteNoEncontradoException(dni);
        }

        return p;

    }

    /**
     * Registra un medico en el sistema
     *
     * @param m medico a registrar
     */

    public void registrarMedico(Medico m) {
        medicos.agregar(m);
    }

    /**
     * Busca un medico por su DNI
     */

    public Medico buscarMedico(String dni) {
        return medicos.buscarPorId(dni);
    }

    /**
     * Devuelve todos los pacientes ordenados por apellido y nombre
     */

    public ArrayList<Paciente> listarPacientes() {
        ArrayList<Paciente> lista = pacientes.listar();
        Collections.sort(lista);
        return lista;
    }

    /**
     * Devuelve todos los medicos registrados
     */

    public ArrayList<Medico> listarMedicos() {
        return medicos.listar();
    }

    /**
     * Asigna un turno nuevo a un paciente con un medico.
     * Valida que el paciente exista, que el medico atienda en ese horario
     * y que ni el medico ni el paciente tengan otro turno que se superponga.
     */

    public Turno asignarTurno(String dni, Medico m, LocalDateTime fechaHora, String motivo)
            throws PacienteNoEncontradoException, TurnoSuperpuestoException, HorarioNoDisponibleException, SQLException {

        Paciente p = buscarPaciente(dni);

        if (!m.atiendeEn(fechaHora)) {
            throw new HorarioNoDisponibleException("El medico " + m.getNombreCompleto()
                    + " no atiende el " + fechaHora.toLocalDate() + " a las " + fechaHora.toLocalTime());
        }

        Turno nuevoTurno = new Turno(p, m, fechaHora, motivo);

        for (Turno t : turnos.listar()) {
            if (t.getEstado() == EstadoTurno.CANCELADO || !t.seSuperponeCon(nuevoTurno)) {
                continue;
            }
            if (t.getMedico().getDni().equals(m.getDni())) {
                throw new TurnoSuperpuestoException("El medico " + m.getNombreCompleto()
                        + " ya tiene un turno a las " + t.getFechaHora().toLocalTime());
            }
            if (t.getPaciente().getDni().equals(dni)) {
                throw new TurnoSuperpuestoException("El paciente " + p.getNombreCompleto()
                        + " ya tiene otro turno a las " + t.getFechaHora().toLocalTime());
            }
        }

        turnoDAO.insertar(nuevoTurno);
        turnos.agregar(nuevoTurno);
        return nuevoTurno;
    }

    /**
     * Cancela un turno reservado y guarda el cambio en la base.
     * Si la base falla, el turno vuelve a quedar RESERVADO en memoria.
     */

    public void cancelarTurno(String idTurno) throws TurnoNoCancelableException, SQLException {

        Turno t = turnos.buscarPorId(idTurno);

        if (t == null) {
            throw new TurnoNoCancelableException(idTurno);
        }

        if (t.getEstado() != EstadoTurno.RESERVADO) {
            throw new TurnoNoCancelableException(idTurno);
        }

        t.cancelar();
        try {
            turnoDAO.actualizar(t);
        } catch (SQLException e) {
            t.setEstado(EstadoTurno.RESERVADO);
            throw e;
        }
    }

    /**
     * Busca los horarios libres de todos los medicos de una especialidad en una fecha.
     * Recorre cada horario de atencion del medico para ese dia, lo divide en franjas
     * segun la duracion de turno, y descarta las franjas ocupadas por turnos no cancelados.
     *
     * @param especialidad especialidad buscada
     * @param fecha        dia para el que se buscan horarios
     * @return lista de fechas y horas disponibles, ordenada
     */

    public ArrayList<LocalDateTime> buscarHorariosLibres(Especialidad especialidad, LocalDate fecha) {
        ArrayList<LocalDateTime> horariosLibres = new ArrayList<>();

        for (Medico m : medicos.listar()) {
            int duracion = m.getDuracionTurnoMin();
            if (m.getEspecialidad() != especialidad || duracion <= 0) {
                continue;
            }

            for (HorarioAtencion h : m.getHorarios()) {
                if (h.getDia() != fecha.getDayOfWeek()) {
                    continue;
                }

                LocalTime hora = h.getHoraInicio();
                while (!hora.plusMinutes(duracion).isAfter(h.getHoraFin())) {
                    LocalDateTime inicioNuevo = fecha.atTime(hora);
                    LocalDateTime finNuevo = inicioNuevo.plusMinutes(duracion);

                    boolean disponible = true;
                    for (Turno t : turnos.listar()) {
                        boolean mismoMedico = t.getMedico().getDni().equals(m.getDni());
                        if (mismoMedico && t.getEstado() != EstadoTurno.CANCELADO) {
                            LocalDateTime inicioExistente = t.getFechaHora();
                            LocalDateTime finExistente = inicioExistente.plusMinutes(duracion);
                            if (inicioNuevo.isBefore(finExistente) && inicioExistente.isBefore(finNuevo)) {
                                disponible = false;
                                break;
                            }
                        }
                    }
                    if (disponible && !horariosLibres.contains(inicioNuevo)) {
                        horariosLibres.add(inicioNuevo);
                    }
                    hora = hora.plusMinutes(duracion);
                }
            }
        }

        Collections.sort(horariosLibres);
        return horariosLibres;
    }

    /**
     * Devuelve los turnos de un medico en una fecha, ordenados por hora.
     */

    public ArrayList<Turno> listarTurnosPorMedico(Medico medico, LocalDate fecha) {
        ArrayList<Turno> lista = new ArrayList<>();
        for (Turno t : turnos.listar()) {
            if (t.getMedico().getDni().equals(medico.getDni()) && t.getFechaHora().toLocalDate().equals(fecha)) {
                lista.add(t);
            }
        }
        Collections.sort(lista);
        return lista;
    }

    /**
     * Devuelve todos los turnos de un paciente, ordenados por fecha y hora.
     */

    public ArrayList<Turno> listarTurnosPorPaciente(String dni) {
        ArrayList<Turno> lista = new ArrayList<>();
        for (Turno t : turnos.listar()) {
            if (t.getPaciente().getDni().equals(dni)) {
                lista.add(t);
            }
        }
        Collections.sort(lista);
        return lista;
    }

    /**
     * Genera un reporte estadístico con la cantidad de turnos registrados
     * en el sistema, separados según su estado: reservados, atendidos,
     * cancelados y ausentes.
     *
     * @return un texto con el total de turnos y la cantidad correspondiente a cada estado
     */
    @Override
    public String generarReporteEstadistico() {
        int reservados = 0;
        int atendidos = 0;
        int cancelados = 0;
        int ausentes = 0;
        for (Turno t : turnos.listar()) {
            switch (t.getEstado()) {
                case RESERVADO:
                    reservados++;
                    break;
                case ATENDIDO:
                    atendidos++;
                    break;
                case CANCELADO:
                    cancelados++;
                    break;
                case AUSENTE:
                    ausentes++;
                    break;
            }
        }
        String reporte = "REPORTE ESTADÍSTICO\n"
                + "Total de turnos: " + turnos.cantidad() + "\n"
                + "Reservados: " + reservados + "\n"
                + "Atendidos: " + atendidos + "\n"
                + "Cancelados: " + cancelados + "\n"
                + "Ausentes: " + ausentes;

        return reporte;
    }
}
