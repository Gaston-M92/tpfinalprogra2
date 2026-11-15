package modelo;

import java.time.LocalDate;

public class Paciente extends Persona {
    private int historiaClinica;
    private String obraSocial;
    private LocalDate fechaNacimiento;

    public Paciente(String nombre, String apellido, String dni, String telefono, int historiaClinica, String obraSocial, LocalDate fechaNacimiento) {
        super(nombre, apellido, dni, telefono);
        this.historiaClinica = historiaClinica;
        this.obraSocial = obraSocial;
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getHistoriaClinica() {
        return historiaClinica;
    }

    public void setHistoriaClinica(int historiaClinica) {
        this.historiaClinica = historiaClinica;
    }

    public String getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(String obraSocial) {
        this.obraSocial = obraSocial;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Utiliza generarFicha por parte de la clase Paciente.
     */
    @Override
    public String generarFicha() {
        return "PACIENTE - " + getApellido() + ", " + getNombre()
                + " | DNI: " + getDni()
                + " | HC: " + historiaClinica
                + " | Obra social: " + obraSocial;
    }

}
