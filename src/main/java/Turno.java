import Enums.EstadoTurno;

import java.time.LocalDateTime;

public class Turno {
    private String id;
    private Paciente paciente;
    private Medico medico;
    private LocalDateTime fechaHora;
    private String motivoConsulta;
    private EstadoTurno  estado;

    public Turno(String id, Paciente paciente, Medico medico, LocalDateTime fechaHora, String motivoConsulta, EstadoTurno estado) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.fechaHora = fechaHora;
        this.motivoConsulta = motivoConsulta;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
