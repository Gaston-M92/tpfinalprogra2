package gestor;

import Interfaces.Identificable;
import java.util.ArrayList;

public class Gestor<T extends Identificable>{
    /**
     * Colección genérica de elementos identificables.
     * Permite agregar, eliminar, buscar por id y listar cualquier tipo
     * que implemente Identificable (Paciente, Medico, Turno).
     *
     * @param <T> tipo de elemento que guarda
     */

    private ArrayList<T> elementos;

    public Gestor(){
        elementos = new ArrayList<>();
    }

    public void agregar(T elemento){
        elementos.add(elemento);
    }

    public void eliminar(String id){
        elementos.removeIf(element -> element.getId().equals(id));
    }

    public T buscarPorId(String id){
        for(T elemento : elementos){
            if(elemento.getId().equals(id)){
                return elemento;
            }
        }
        return null;
    }

    public ArrayList<T> listar(){
        return new ArrayList<>(elementos);
    }

    public int cantidad(){
        return elementos.size();
    }



}
