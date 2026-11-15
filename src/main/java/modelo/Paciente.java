package modelo;

import java.time.LocalDate;

/**
 * Representa a un paciente del consultorio.
 * Hereda los datos personales de Persona y agrega
 * su número de historia clínica, obra social y fecha de nacimiento.
 */
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
     * Genera una ficha con los datos personales y médicos del paciente.
     *
     * @return cadena con la información principal del paciente.
     */
    @Override
    public String generarFicha() {
        return "PACIENTE - " + getApellido() + ", " + getNombre()
                + " | DNI: " + getDni()
                + " | HC: " + historiaClinica
                + " | Obra social: " + obraSocial;
    }

}
