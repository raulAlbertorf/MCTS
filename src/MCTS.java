
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * MCTS, representa el algoritmo Monte Carlo Tree Search basado en UCT, 
 * contiene sus cuatro pasos: Selection, Expansion, Simulation y 
 * Backpropagation, ademas extrae BestChild mediante UCB y BestMove por medio 
 * del criterio Robust child.
 */
public class MCTS {
    
    // Jugador inicial, tiempo limite de ejecucion y generador de numeros aleatorios
    private int player;
    private long time_limit;
    private final Random random;
    
    /**
     * Constructor, inicializa el generador de numeros aleatorios.
     */
    public MCTS()
    {
        this.random = new Random();
    }
    
    /**
     * Crea el nodo raiz e inicia la ejecucion del algoritmo MCTS.
     * 
     * @param board tablero con el estado actual del juego
     * @param time_limit limite de tiempo para ejecutar
     * @return movimiento mejor evaluado
     */
    public Move Run(Board board, int time_limit)
    {
        this.player = board.turn;
        this.time_limit = System.currentTimeMillis() + (time_limit * 60 * 1000);
        
        Node root = new Node(null, board.turn);
        return ALG(root, board);
    }
    
    /**
     * Ejecuta los pasos de MCTS y el metodo para elegir el mejor movimiento.
     * 
     * @param node nodo raiz desde donde comienza el arbol de busqueda
     * @param board tablero con el estado actual del juego
     * @return movimiento mejor evaluado
     */
    public Move ALG(Node node, Board board)
    {
        Map.Entry<Board, Node> pair = SelectionAndExpansion(node, board);
        int result = Simulation(pair.getKey(), pair.getValue());
        Backpropagation(pair.getValue(), result);
        
        return BestMove(node);
    }
    
    /**
     * Ejecuta los pasos (1) Selection y (2) Expansion del algoritmo MCTS.
     * 
     * @param node nodo raiz desde donde comienza el arbol de busqueda
     * @param board tablero con el estado actual del juego
     * @return par(tablero, nodo) tablero con el mejor movimiento realizado y 
     * nodo al que pertenece tal movimiento
     */
    public Map.Entry<Board,Node> SelectionAndExpansion(Node node, Board board)
    {
        while (!board.isCheckMate() && !board.isStalemate()) {
            if (node.unvisited_children == null) {
                Move[] moves = board.getValidMoves();
                node.unvisited_children = new ArrayList<>();
                for (Move m : moves) {
                    Node child = new Node(m, board.turn);
                    node.unvisited_children.add(child);
                }
            }
            if (!node.unvisited_children.isEmpty()) {
                Node child = node.unvisited_children.remove(random.nextInt(node.unvisited_children.size()));
                node.children.add(child);
                child.parent = node;
                board.makeMove(child.move);
                return new AbstractMap.SimpleEntry<>(board, child);
            }
            else {
                Node best = BestChild(node);
                node = best;
                board.makeMove(best.move);
            }
        }
        return new AbstractMap.SimpleEntry<>(board, node);
    }
    
    /**
     * Ejecuta el paso (3) Simulation del algoritmo MCTS.
     * 
     * @param board tablero con el estado actual del juego
     * @param node nodo con el mejor movimiento segun Selection
     * @return 1 si el jugador gana, -1 si pierde, 0 en otro caso
     */
    public int Simulation(Board board, Node node)
    {
        while (!board.isCheckMate() && !board.isStalemate()) {
            Move[] moves = board.getValidMoves();
            Move m = moves[random.nextInt(moves.length)];
            board.makeMove(m);
        }
        if (board.isCheckMate()) {
            if (player == board.turn)
                return 1;
            else
                return -1;
        }
        else {
            return 0;
        }
    }
    
    /**
     * Ejecuta el paso (4) Backpropagation del algoritmo MCTS.
     * 
     * @param node nodo con el mejor movimiento segun Selection
     * @param value valor de ganancia/perdida retornado por Simulation
     */
    public void Backpropagation(Node node, int value)
    {
        while (node.parent != null) {
            node.visits++;
            if (value == 1)
                node.wins++;
            node = node.parent;
        }
    }
    
    /**
     * Busca el nodo hijo que tenga la mejor evaluacion UCB, se utiliza en el 
     * paso Selection.
     * 
     * @param node nodo raiz desde donde comienza el arbol de busqueda
     * @return nodo con el mayor puntaje UCB
     */
    private Node BestChild(Node node)
    {
        double best = Double.NEGATIVE_INFINITY;
        double ucb;
        ArrayList<Node> best_children = new ArrayList<>();
        
        for (Node child : node.children) {
            ucb = (child.parent.wins / child.visits) + Math.sqrt(2.0) * 
                    (Math.sqrt(Math.log(child.parent.visits + 1) / child.visits));
            
            if (ucb > best) {
                best_children.clear();
                best_children.add(child);
                best = ucb;
            }
            else if (ucb == best) {
                best_children.add(child);
            }
        }
        return best_children.get(random.nextInt(best_children.size()));
    }
    
    /**
     * Busca el nodo hijo que contenga la mejor accion (i.e. mejor movimiento) 
     * segun el criterio de Robust child (hijo mas visitado).
     * 
     * @param node nodo raiz desde donde comienza el arbol de busqueda
     * @return movimiento mejor evaluado
     */
    public Move BestMove(Node node)
    {
        double best = Double.NEGATIVE_INFINITY;
        double temp_best;
        ArrayList<Node> best_children = new ArrayList<>();

        for (Node child : node.children) {
            temp_best = child.visits;
            
            if (temp_best > best) {
                best_children.clear();
                best_children.add(child);
                best = temp_best;
            }
            else if (temp_best == best) {
                best_children.add(child);
            }
        }
        return best_children.get(random.nextInt(best_children.size())).move;
    }
    
}
