package modelo;

import Enums.EstadoTurno;
import Interfaces.Identificable;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa un turno médico del consultorio.
 * Relaciona un paciente con un médico en una fecha y hora determinada.
 * Almacena el motivo de consulta y el estado actual del turno.
 */
public class Turno implements Identificable, Comparable<Turno> {

    private String idTurno;
    private Paciente paciente;
    private Medico medico;
    private LocalDateTime fechaHora;
    private String motivoConsulta;
    private EstadoTurno estado;

    /**
     * Crea un turno con su identificador, paciente, médico,
     * fecha y hora, motivo de consulta y estado inicial.
     */
    public Turno(String idTurno, Paciente paciente, Medico medico,
                 LocalDateTime fechaHora, String motivoConsulta,
                 EstadoTurno estado) {
        this.idTurno = idTurno;
        this.paciente = paciente;
        this.medico = medico;
        this.fechaHora = fechaHora;
        this.motivoConsulta = motivoConsulta;
        this.estado = estado;
    }

    /**
     * Crea un turno generando automáticamente su identificador
     * y estableciendo su estado inicial como RESERVADO.
     */
    public Turno(Paciente paciente, Medico medico,
                 LocalDateTime fechaHora, String motivoConsulta) {
        this.idTurno = UUID.randomUUID().toString();
        this.paciente = paciente;
        this.medico = medico;
        this.fechaHora = fechaHora;
        this.motivoConsulta = motivoConsulta;
        this.estado = EstadoTurno.RESERVADO;
    }

    /** Devuelve el identificador del turno. */
    @Override
    public String getId() {
        return idTurno;
    }

    public void setId(String idTurno) {
        this.idTurno = idTurno;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public void setEstado(EstadoTurno estado) {
        this.estado = estado;
    }

    /** Cancela el turno. */
    public void cancelar() {
        this.estado = EstadoTurno.CANCELADO;
    }

    /** Marca el turno como atendido. */
    public void marcarAtendido() {
        this.estado = EstadoTurno.ATENDIDO;
    }

    /** Marca al paciente como ausente. */
    public void marcarAusente() {
        this.estado = EstadoTurno.AUSENTE;
    }

    /**
     * Verifica si este turno se superpone con otro turno.
     *
     * @param otro turno con el que se desea comparar.
     * @return true si los turnos se superponen, false en caso contrario.
     */
    public boolean seSuperponeCon(Turno otro) {
        LocalDateTime inicio1 = this.fechaHora;
        LocalDateTime fin1 = inicio1.plusMinutes(
                this.medico.getDuracionTurnoMin());

        LocalDateTime inicio2 = otro.fechaHora;
        LocalDateTime fin2 = inicio2.plusMinutes(
                otro.medico.getDuracionTurnoMin());

        return inicio1.isBefore(fin2) && inicio2.isBefore(fin1);
    }

    /** Compara por fecha y hora para ordenar los turnos cronológicamente. */
    @Override
    public int compareTo(Turno otro) {
        return this.fechaHora.compareTo(otro.fechaHora);
    }
}