package Exceptions;

/**
 * Excepción que se produce cuando un turno se superpone
 * con otro turno existente del médico o del paciente.
 */
public class TurnoSuperpuestoException extends Exception {
    public TurnoSuperpuestoException(String message) {
        super(message);
    }
}
