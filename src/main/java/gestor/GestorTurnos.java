package gestor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import Enums.Especialidad;
import Enums.EstadoTurno;
import Exceptions.HorarioNoDisponibleException;
import Exceptions.PacienteNoEncontradoException;
import Exceptions.TurnoNoCancelableException;
import Exceptions.TurnoSuperpuestoException;
import Interfaces.Reportable;
import modelo.*;


public class GestorTurnos implements Reportable {
    private Gestor<Paciente> pacientes;
    private Gestor<Medico> medicos;
    private Gestor<Turno> turnos;
    private ArrayList<Consultorio> consultorios;

    public GestorTurnos() {
        pacientes = new Gestor<>();
        medicos = new Gestor<>();
        turnos = new Gestor<>();
        consultorios = new ArrayList<>();
    }

    public void registrarPaciente(Paciente p){

    }

    public Paciente buscarPaciente(String dni) throws PacienteNoEncontradoException {
        Paciente p = pacientes.buscarPorId(dni);

        if(p == null){
            throw new PacienteNoEncontradoException(dni);
        }

        return p;

    }

    public Turno asignarTurno(String dni, Medico m, LocalDateTime fechaHora, String motivo) throws PacienteNoEncontradoException, TurnoSuperpuestoException, HorarioNoDisponibleException {

        Paciente p = pacientes.buscarPorId(dni);

        if (p == null) {
            throw new PacienteNoEncontradoException(dni);
        }

        if (!m.atiendeEn(fechaHora)) {
            throw new HorarioNoDisponibleException(dni);
        }

        Turno nuevoTurno = new Turno(p, m, fechaHora, motivo);

        for (Turno t : turnos.listar()) {
            if (t.getEstado() != EstadoTurno.CANCELADO
                    && t.seSuperponeCon(nuevoTurno)) {
                throw new TurnoSuperpuestoException("El turno ya esta superpuesto");
            }
        }

        turnos.agregar(nuevoTurno);
        return nuevoTurno;
    }

    public void cancelarTurno(String idTurno) throws TurnoNoCancelableException {

        Turno t = turnos.buscarPorId(idTurno);

        if (t == null) {
            throw new TurnoNoCancelableException(idTurno);
        }

        if (t.getEstado() != EstadoTurno.RESERVADO) {
            throw new TurnoNoCancelableException(idTurno);
        }
        t.setEstado(EstadoTurno.CANCELADO);
    }

    public ArrayList<LocalDateTime> buscarHorariosLibres(Especialidad especialidad, LocalDate fecha) {

        ArrayList<LocalDateTime> horariosLibres = new ArrayList<>();

        for (Medico m : medicos.listar()) {

            if (m.getEspecialidad() == especialidad) {

                for (HorarioAtencion h : m.getHorarios()) {

                    if (h.getDia() == fecha.getDayOfWeek()) {

                        LocalTime hora = h.getHoraInicio();

                        while (!hora.plusMinutes(m.getDuracionTurnoMin()).isAfter(h.getHoraFin())) {

                            LocalDateTime inicioNuevo = fecha.atTime(hora);
                            LocalDateTime finNuevo = inicioNuevo.plusMinutes(m.getDuracionTurnoMin());

                            boolean disponible = true;

                            for (Turno t : turnos.listar()) {

                                if (t.getEstado() != EstadoTurno.CANCELADO && t.getMedico().equals(m)) {

                                    LocalDateTime inicioExistente = t.getFechaHora();
                                    LocalDateTime finExistente = inicioExistente.plusMinutes(t.getMedico().getDuracionTurnoMin());

                                    if (inicioNuevo.isBefore(finExistente) && inicioExistente.isBefore(finNuevo)) {
                                        disponible = false;
                                        break;
                                    }
                                }
                            }

                            if (disponible) {
                                horariosLibres.add(inicioNuevo);
                            }

                            hora = hora.plusMinutes(m.getDuracionTurnoMin());
                        }
                    }
                }
            }
        }

        return horariosLibres;
    }

    public ArrayList<Turno> listarTurnosPorMedico(Medico m, LocalDate fecha){
        ArrayList<Turno> lista = new ArrayList<>();

        for (Turno t : turnos.listar()) {
            if (t.getMedico().equals(m)
                    && t.getFechaHora().toLocalDate().equals(fecha)) {
                lista.add(t);
            }
        }

        return lista;

    }

    public ArrayList<Turno> listarTurnosPorPaciente(String dni){
        ArrayList<Turno> lista = new ArrayList<>();

        for (Turno t : turnos.listar()) {
            if(t.getPaciente().equals(dni)){
                lista.add(t);
            }
        }
        return lista;

    }


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
