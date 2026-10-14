import java.time.DayOfWeek;
import java.time.LocalTime;

public class HorarioAtencion {
    private DayOfWeek dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Consultorio consultorio;

    public HorarioAtencion(DayOfWeek dia, LocalTime horaInicio, LocalTime horaFin, Consultorio consultorio) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.consultorio = consultorio;
    }

    public DayOfWeek getDia() {
        return dia;
    }

    public void setDia(DayOfWeek dia) {
        this.dia = dia;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Consultorio getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(Consultorio consultorio) {
        this.consultorio = consultorio;
    }
}
