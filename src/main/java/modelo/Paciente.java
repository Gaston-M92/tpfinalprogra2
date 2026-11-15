package modelo;

import java.time.LocalDate;
import java.time.Period;

/**
 * Representa a un paciente del consultorio.
 * Hereda los datos personales de Persona y agrega
 * su número de historia clínica, obra social y fecha de nacimiento.
 */
public class Paciente extends Persona implements Comparable<Paciente>{
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
     * Calcula la edad actual del paciente a partir de su fecha de nacimiento.
     *
     * @return la edad del paciente expresada en años completos.
     */
    public int calcularEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    /**
     * Compara este paciente con otro paciente según su apellido.
     * Si ambos tienen el mismo apellido, compara sus nombres.
     *
     * @param otro paciente con el que se desea realizar la comparación.
     * @return un valor negativo, cero o positivo según el orden alfabético.
     */
     @Override
     public int compareTo(Paciente otro) {
         int comparacion = getApellido().compareToIgnoreCase(otro.getApellido());
         if (comparacion != 0) {
             return comparacion;
         }

         return getNombre().compareToIgnoreCase(otro.getNombre());
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
