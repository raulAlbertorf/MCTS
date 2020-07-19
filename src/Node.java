
import java.util.ArrayList;

/**
 * Node, estructura que permite construir un arbol de busqueda en base a 
 * simulaciones Monte Carlo, almacena resultados y otras propiedades.
 */
public class Node {
    
    // Movimiento perteneciente al nodo y jugador que lo realiza
    public Move move;
    public int player;
    
    // Contadores de victorias y visitas para nodo actual
    public int wins;
    public int visits;
    
    // Punteros al padre y a los hijos visitados y no visitados
    public Node parent;
    public ArrayList<Node> children;
    public ArrayList<Node> unvisited_children;
   
    /**
     * Constructor, inicializa un nuevo nodo con un movimiento y el jugador que 
     * lo ha realizado.
     * 
     * @param move movimiento valido desde el estado actual
     * @param player jugador que ha realizado el movimiento
     */
    public Node(Move move, int player)
    {
        this.move = move;
        this.player = player;
        
        this.wins = 0;
        this.visits = 0;
        
        this.parent = null;
        this.children = new ArrayList<>();
        this.unvisited_children = null;
    }
    
}
