
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Agent, se encarga de leer el tablero y sus opciones desde un archivo tbl, 
 * ademas inicia la ejecucion del algoritmo Monte Carlo Tree Search.
 */
public class Agent {
    
    /**
     * Inicia el funcionamiento del agente jugador de ajedrez.
     * 
     * @param args argumentos (2), ruta del archivo que contiene el tablero y 
     * tiempo maximo de ejecucion del algoritmo en minutos
     */
    public static void main(String[] args)
    {
        try {
            // Verificando la cantidad de parametros ingresados
            if (args.length != 2) {
                System.out.println("Usage: java Agent [tbl file] [time limit]");
                System.out.println(" [tbl file] is the file path that contains a board state.");
                System.out.println(" [time limit] is an int number that determines the maximum time of execution in minutes.");
                System.exit(1);
            }
            
            // Leyendo archivo tbl y creando tablero
            BufferedReader input = new BufferedReader(new FileReader(args[0]));
            int b [][] = new int[8][8];
            for (int i=0; i<8; i++) {
                String line = input.readLine();
                String[] pieces = line.split("\\s");
                for (int j=0; j<8; j++) {
                    b[i][j] = Integer.parseInt(pieces[j]);
                }
            }
            Board board = new Board();
            board.fromArray(b);
            
            // Agregando opciones al tablero
            String turn = input.readLine();
            if (turn.equalsIgnoreCase("N")) board.setTurn(Board.TURNBLACK);
            else board.setTurn(Board.TURNWHITE);
            
            board.setShortCastle(Board.TURNBLACK, false);
            board.setLongCastle(Board.TURNBLACK, false);
            board.setShortCastle(Board.TURNWHITE, false);
            board.setLongCastle(Board.TURNWHITE, false);
            
            String info = input.readLine();
            while (info != null) {
                switch (info) {
                    case "EnroqueC_N": board.setShortCastle(Board.TURNBLACK, true);
                        break;
                    case "EnroqueL_N": board.setLongCastle(Board.TURNBLACK, true);
                        break;
                    case "EnroqueC_B": board.setShortCastle(Board.TURNWHITE, true);
                        break;
                    case "EnroqueL_B": board.setLongCastle(Board.TURNWHITE, true);
                        break;
                }
                if (info.contains("MovsHastaEmpate")) {
                    String[] split = info.split("\\s");
                    board.movestodraw = Integer.parseInt(split[1]);
                }
                else if (info.contains("AlPaso")) {
                    String[] split = info.split("\\s");
                    board.setEnPassent(new Coord(split[1].trim()));
                }
                info = input.readLine();
            }
            
            // Verificando si el tablero se encuentra en un estado final
            if (board.isCheckMate()) {
                System.out.println("RESIGN");
                System.err.println("Error: Tablero en Jaque Mate");
                System.exit(1);
            }
            if (board.isStalemate()) {
                System.out.println("RESIGN");
                System.err.println("Error: Tablero en Empate");
                System.exit(1);
            }
            
            // Obteniendo tiempo limite de ejecucion
            int time_limit = Integer.parseInt(args[1]);
            
            // Ejecutando Monte Carlo Tree Search
            MCTS mcts = new MCTS();
            System.out.println(mcts.Run(board, time_limit));
            
        } catch (IOException ex) {
            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
