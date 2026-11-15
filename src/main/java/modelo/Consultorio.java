package modelo;

/**
 * Representa un consultorio del establecimiento.
 * Almacena su número identificador y el piso en el que se encuentra.
 */
public class Consultorio {
    private int numero;
    private int piso;

    /**
     * Crea un consultorio indicando su número y piso.
     */
    public Consultorio(int numero, int piso) {
        this.numero = numero;
        this.piso = piso;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }
}
