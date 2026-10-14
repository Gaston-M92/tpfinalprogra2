import Interfaces.Identificable;
import java.util.ArrayList;

public class Repositorio<T extends Identificable>{

    private ArrayList<T> elementos;

    public Repositorio(){
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
    }

    public ArrayList<T> listar(){
        return new ArrayList<>(elementos);
    }

    public int cantidad(){
        return elementos.size();
    }



}
