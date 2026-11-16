package Exceptions;

public class HorarioNoDisponibleException extends Exception {
    /**
     * Se produce cuando se intenta asignar un turno
     * en un día u horario en el que el médico no atiende.
     */
    public HorarioNoDisponibleException(String message) {
        super(message);
    }
}
