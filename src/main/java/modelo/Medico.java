package modelo;

import Enums.Especialidad;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Representa a un médico del consultorio.
 * Hereda los datos personales de Persona y agrega matrícula,
 * especialidad, duración de turno y sus horarios de atención.
 */
public class Medico extends Persona{

    private String matricula;
    private Especialidad especialidad;
    private int duracionTurnoMin;
    private ArrayList<HorarioAtencion> horarios;

    public Medico(String nombre, String apellido, String dni, String telefono, String matricula, Especialidad especialidad, int duracionTurnoMin, ArrayList<HorarioAtencion> horarios) {
        super(nombre, apellido, dni, telefono);
        this.matricula = matricula;
        this.especialidad = especialidad;
        this.duracionTurnoMin = duracionTurnoMin;
        this.horarios = new ArrayList<>(horarios);
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public int getDuracionTurnoMin() {
        return duracionTurnoMin;
    }

    public void setDuracionTurnoMin(int duracionTurnoMin) {
        this.duracionTurnoMin = duracionTurnoMin;
    }

    public ArrayList<HorarioAtencion> getHorarios() {
        return horarios;
    }

    public void setHorarios(ArrayList<HorarioAtencion> horarios) {
        this.horarios = horarios;
    }

    /**
     * Agrega un horario de atención a la lista de horarios del médico.
     */
    public void agregarHorario(HorarioAtencion h) {
        horarios.add(h);
    }

    /**
     * Verifica si el médico atiende en una fecha y hora determinadas.
     */
    public boolean atiendeEn(LocalDateTime fechaHora) {

        for (HorarioAtencion h : horarios) {
            if (h.incluye(fechaHora)) {
                return true;
            }
        }

        return false;
    }



    /**
     * Genera una ficha con los datos personales y profesionales del médico.
     */
    @Override
    public String generarFicha() {
        return "MÉDICO - " + getApellido() + ", " + getNombre()
                + " | DNI: " + getDni()
                + " | Matrícula: " + matricula
                + " | Especialidad: " + especialidad;
    }
}


