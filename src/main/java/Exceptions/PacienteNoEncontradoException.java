package Exceptions;

/**
 * Se lanza cuando no existe un paciente con el DNI indicado.
 */
public class PacienteNoEncontradoException extends Exception {

    public PacienteNoEncontradoException(String dni) {
        super("No existe un paciente con DNI " + dni);
    }
}