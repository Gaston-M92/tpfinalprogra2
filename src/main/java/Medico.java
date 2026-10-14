import Enums.Especialidad;

public class Medico extends Persona implements Especialidad {
    private String matricula;
    private Especialidad especialidad;
    private int duracionTurnoMin;
    private ArrayList<HorarioAtencion> horarios;

    public Medico(String nombre, String apellido, String dni, String telefono, String matricula, Especialidad especialidad, int duracionTurnoMin, ArrayList<HorarioAtencion> horarios) {
        super(nombre, apellido, dni, telefono);
        this.matricula = matricula;
        this.especialidad = especialidad;
        this.duracionTurnoMin = duracionTurnoMin;
        this.horarios = horarios;
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
}
