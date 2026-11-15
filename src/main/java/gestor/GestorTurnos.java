package gestor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import Enums.Especialidad;
import Exceptions.PacienteNoEncontradoException;
import Exceptions.TurnoNoCancelableException;
import Interfaces.Reportable;
import modelo.Consultorio;
import modelo.Medico;
import modelo.Paciente;
import modelo.Turno;


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

    public Turno asignarTurno(String dni, Medico m, LocalDateTime fechaHora, String motivo){

    }

    public void cancelarTurno(String idTurno) throws TurnoNoCancelableException{

    }

    public ArrayList<LocalDateTime> buscarHorariosLibres(Especialidad especialidad, LocalDate fecha){

    }

    public ArrayList<Turno> listarTurnosPorMedico(Medico m, LocalDate fecha){

    }

    public ArrayList<Turno> listarTurnosPorPaciente(String dni){

    }


    @Override
    public String generarReporteEstadistico() {
        // Nuestra lógica
    }


}
