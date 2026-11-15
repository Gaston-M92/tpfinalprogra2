package Exceptions;

/**
 * Se lanza cuando se intenta cancelar un turno que no se puede cancelar.
 */

public class TurnoNoCancelableException extends Exception {
    private final String id;

    public TurnoNoCancelableException( String id) {
        super("El turno con id: " + id + " no se puede cancelar");
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
