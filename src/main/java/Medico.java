import Enums.Especialidad;

public class Medico extends Persona implements Especialidad {
    private String matricula;
    private Especialidad especialidad;
    private int duracionTurnoMin;
    private ArrayList<HorarioAtencion> horarios;

}
