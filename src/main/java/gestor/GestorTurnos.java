package gestor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

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

    public void registrarPaciente(Paciente p) {
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

    public void registrarMedico(Medico m){
        medicos.agregar(m);
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
            if (t.getMedico().equals(m) && t.getEstado() != EstadoTurno.CANCELADO && t.seSuperponeCon(nuevoTurno)) {
                throw new TurnoSuperpuestoException("El médico ya tiene un turno en ese horario");
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

        t.cancelar();
    }

    /**
     * Busca los horarios libres de todos los medicos de una especialidad en una fecha.
     * Recorre cada horario de atencion del medico para ese dia, lo divide en franjas
     * segun la duracion de turno, y descarta las franjas ocupadas por turnos no cancelados.
     *
     * @param especialidad especialidad buscada
     * @param fecha dia para el que se buscan horarios
     * @return lista de fechas y horas disponibles, ordenada
     */

    public ArrayList<LocalDateTime> buscarHorariosLibres(Especialidad especialidad, LocalDate fecha){
        ArrayList<LocalDateTime> horariosLibres = new ArrayList<>();

        for(Medico m : medicos.listar()){
            int duracion = m.getDuracionTurnoMin();
            if(m.getEspecialidad() != especialidad || duracion <= 0){
                continue;
            }

            for(HorarioAtencion h : m.getHorarios()){
                if(h.getDia() != fecha.getDayOfWeek()){
                    continue;
                }

                LocalTime hora = h.getHoraInicio();
                while(!hora.plusMinutes(duracion).isAfter(h.getHoraFin())){
                    LocalDateTime inicioNuevo = fecha.atTime(hora);
                    LocalDateTime finNuevo = inicioNuevo.plusMinutes(duracion);

                    boolean disponible = true;
                    for(Turno t : turnos.listar()){
                        boolean mismoMedico = t.getMedico().getDni().equals(m.getDni());
                        if(mismoMedico && t.getEstado() != EstadoTurno.CANCELADO){
                            LocalDateTime inicioExistente = t.getFechaHora();
                            LocalDateTime finExistente = inicioExistente.plusMinutes(duracion);
                            if(inicioNuevo.isBefore(finExistente) && inicioExistente.isBefore(finNuevo)){
                                disponible = false;
                                break;
                            }
                        }
                    }
                    if(disponible && !horariosLibres.contains(inicioNuevo)){
                        horariosLibres.add(inicioNuevo);
                    }
                    hora = hora.plusMinutes(duracion);
                }
            }
        }

        Collections.sort(horariosLibres);
        return horariosLibres;
    }

        public ArrayList<Turno> listarTurnosPorMedico (Medico m, LocalDate fecha){
            ArrayList<Turno> lista = new ArrayList<>();

            for (Turno t : turnos.listar()) {
                if (t.getMedico().equals(m)
                        && t.getFechaHora().toLocalDate().equals(fecha)) {
                    lista.add(t);
                }
            }

            return lista;

        }

        public ArrayList<Turno> listarTurnosPorPaciente (String dni){
            ArrayList<Turno> lista = new ArrayList<>();

            for (Turno t : turnos.listar()) {
                if (t.getPaciente().getDni().equals(dni)) {
                    lista.add(t);
                }
            }
            return lista;

        }



        @Override
        public String generarReporteEstadistico () {
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
