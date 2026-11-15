package modelo;

import Interfaces.Identificable;

/**
 * Clase abstracta que representa los datos comunes de una persona.
 * Implementa Identificable utilizando el DNI como identificador.
 * Es la clase padre de Paciente y Medico.
 */
public abstract class Persona implements Identificable {
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;

    public Persona(String nombre, String apellido, String dni, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Devuelve una ficha con los datos de la persona.
     * Cada subclase la arma a su manera (polimorfismo).
     */
    public abstract String generarFicha();

    /**
     * Devuelve el identificador de la persona, que es su DNI.
     * Lo usa Repositorio para buscar y eliminar elementos.
     */
    @Override
    public String getId() {
        return dni;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni='" + dni + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
