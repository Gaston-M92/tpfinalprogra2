import java.time.LocalDate;

public class Paciente extends Persona{
    private int historiaClinica;
    private int contador = 1000;
    private String obraSocial;
    private LocalDate fechaNacimiento;

    public Paciente(String nombre, String apellido, String dni, String telefono, int historiaClinica, int contador, String obraSocial, LocalDate fechaNacimiento) {
        super(nombre, apellido, dni, telefono);
        this.historiaClinica = historiaClinica;
        this.contador = contador;
        this.obraSocial = obraSocial;
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getHistoriaClinica() {
        return historiaClinica;
    }

    public void setHistoriaClinica(int historiaClinica) {
        this.historiaClinica = historiaClinica;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
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
}
