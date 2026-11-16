package gestor;

import Interfaces.Identificable;
import java.util.ArrayList;

/**
 * Colección genérica de elementos identificables.
 * Permite agregar, eliminar, buscar por id y listar cualquier tipo
 * que implemente Identificable (Paciente, Medico, Turno).
 *
 * @param <T> tipo de elemento que guarda
 */
public class Gestor<T extends Identificable>{


    private ArrayList<T> elementos;

    /**
     * Crea un gestor vacío para guardar elementos.
     */
    public Gestor(){
        elementos = new ArrayList<>();
    }

    /**
     * Agrega un elemento a la colección.
     *
     * @param elemento elemento que se desea agregar
     */
    public void agregar(T elemento){
        elementos.add(elemento);
    }

    /**
     * Elimina de la colección el elemento que tenga el id indicado.
     *
     * @param id identificador del elemento que se desea eliminar
     */
    public void eliminar(String id){
        elementos.removeIf(element -> element.getId().equals(id));
    }

    /**
     * Busca un elemento en la colección a partir de su id.
     *
     * @param id identificador del elemento que se desea buscar
     * @return el elemento encontrado o null si no existe
     */
    public T buscarPorId(String id){
        for(T elemento : elementos){
            if(elemento.getId().equals(id)){
                return elemento;
            }
        }
        return null;
    }

    /**
     * Devuelve una lista con todos los elementos guardados.
     *
     * @return una lista con los elementos del gestor
     */
    public ArrayList<T> listar(){
        return new ArrayList<>(elementos);
    }

    /**
     * Cuenta la cantidad de elementos guardados.
     *
     * @return cantidad de elementos de la colección
     */
    public int cantidad(){
        return elementos.size();
    }



}
