import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** An instance represents a game of BoxShogi being played with <br>
 * either the command being given in advance (file mode) or <br>
 * played by two people making moves each turn(interactive mode). <br>
 * Game rules and specifications are all according to BoxShogi <br>
 * specifications. <br>
 * @author Barry He (bh375@cornell.edu) */
public class Game {
    /** The pieces captured by the UPPER player. */
    private List<String> upperCaptures;

    /** The pieces captured by the lower player. */
    private List<String> lowerCaptures;

    /** Moves to be made in file mode. Null if interactive mode. */
    private List<String> moves;

    /** The board that BoxShogi is played on. */
    private Board        b;

    /** Represents the turn of either lower player or UPPER player. */
    private boolean      lowerTurn;

    /** The number of turns the game has gone on for. */
    private int          numTurns;

    /** Signals whether or not the game has ended. */
    private boolean      gameOver;

    /** Constructor: Interactive mode board initialization with <br>
     * upperCaptures, lowerCaptures, the position of the <br>
     * pieces on b, numTurns, and gameOver set to default values. <br>
     * The field moves is not used and is not instantiated. */
    public Game() {
        upperCaptures= new ArrayList<>();
        lowerCaptures= new ArrayList<>();
        b= new Board(false);
        moves= null;
        lowerTurn= true;
        numTurns= 0;
        gameOver= false;
    }

    /** Constructor: File mode board initialization which passes in <br>
     * upperCaptures, lowerCaptures, moves, and the position of the <br>
     * pieces on b given Util.TestCase tc. The other two fields <br>
     * numTurns and gameOver are set to default of 0 and false, <br>
     * respectively.  */
    public Game(Utils.TestCase tc) {
        upperCaptures= new ArrayList<>();
        for (String p : tc.getUpperCaptures()) {
            if (p.equals(""))
                continue;
            upperCaptures.add(p);
        }
        lowerCaptures= new ArrayList<>();
        for (String p : tc.getLowerCaptures()) {
            if (p.equals(""))
                continue;
            lowerCaptures.add(p);
        }
        b= new Board(true);
        for (Utils.InitialPosition ip : tc.getInitPieces()) {
            int[] position= convertXY(ip.getPosition());
            b.setBoardPiece(position[0], position[1], convertStrToPiece(ip.getPiece(), false));
        }
        moves= tc.getMoves();
        lowerTurn= true;
        numTurns= 0;
        gameOver= false;
    }

    /** Helper function that converts move into a 2d array of <br>
     * coordinates and returns it (i.e. a5 into [0,4]). */
    private static int[] convertXY(String move) {
        assert move.length() == 2 && move.charAt(0) >= 97 && move.charAt(0) <= 101
                && move.charAt(1) >= 49 && move.charAt(1) <= 53;
        return new int[] {move.charAt(0) - 97, move.charAt(1) - 49};
    }

    /** Helper function that converts 2d array of coordinates pos <br>
     * into a move and returns it (i.e. [0,4] into a5). */
    private static String convertCoords(int[] pos) {
        assert pos[0] >= 0 && pos[0] <= 4 && pos[1] >= 0 && pos[1] <= 4;
        String ret= "";
        switch (pos[0]) {
            case 0:
                ret+= "a";
                break;
            case 1:
                ret+= "b";
                break;
            case 2:
                ret+= "c";
                break;
            case 3:
                ret+= "d";
                break;
            case 4:
                ret+= "e";
        }
        return ret + Integer.toString(pos[1] + 1);
    }

    /** Helper function that converts a string representation of piece <br>
     * str with boolean cap into a Piece object and returns it. If cap <br>
     * is true then the Piece is captured, else not captured.  */
    private static Piece convertStrToPiece(String str, boolean cap) {
        boolean promote= false;
        String p= str;
        if (str.length() == 2) {
            promote= true;
            p= Character.toString(str.charAt(1));
        }
        if (p.equals("D"))
            return new DPiece(false, cap);
        else if (p.equals("S"))
            return new SPiece(false, cap);
        else if (p.equals("G"))
            return new GPiece(false, promote, cap);
        else if (p.equals("P"))
            return new PPiece(false, promote, cap);
        else if (p.equals("R"))
            return new RPiece(false, promote, cap);
        else if (p.equals("N"))
            return new NPiece(false, promote, cap);
        else if (p.equals("d"))
            return new DPiece(true, cap);
        else if (p.equals("s"))
            return new SPiece(true, cap);
        else if (p.equals("g"))
            return new GPiece(true, promote, cap);
        else if (p.equals("p"))
            return new PPiece(true, promote, cap);
        else if (p.equals("r"))
            return new RPiece(true, promote, cap);
        else if (p.equals("n"))
            return new NPiece(true, promote, cap);
        else
            return null;
    }

    /** Returns an output to be printed by the console after executing <br>
     * a move given String m and boolean turn. The move m can either <br>
     * be a valid move command or drop command as specified by BoxShogi <br>
     * specifications, and if turn is true the player is lower, else <br>
     * the player is UPPER. */
    private String move(String m, boolean turn) {
        numTurns++;
        boolean doMove= false;
        String[] oneMove= m.split(" ");
        boolean dropMove= oneMove[0].equals("drop") ? true : false;
        int[] init= !dropMove ? convertXY(oneMove[1]) : new int[0];
        int[] moveTo= convertXY(oneMove[2]);
        Piece curPiece= !dropMove ? b.getPiece(init[0], init[1]) : null;
        if (oneMove.length == 4)
            doMove= isLegalMove(init, moveTo, curPiece, true, turn);
        else {
            if (dropMove)
                doMove= isLegalDrop(oneMove[1], moveTo, turn);
            else
                doMove= isLegalMove(init, moveTo, curPiece, false, turn);
        }
        if (!doMove) {
            gameOver= true;
            lowerTurn= !lowerTurn;
            return lowerTurn ? "lower player wins.  Illegal move."
                    : "UPPER player wins.  Illegal move.";
        } else {
            if (dropMove) {
                Piece newPiece= lowerTurn ? convertStrToPiece(oneMove[1], false)
                        : convertStrToPiece(oneMove[1].toUpperCase(), false);
                commitMove(moveTo, newPiece, turn);
            } else {
                commitMove(init, moveTo, curPiece, turn);
            }
            if (gameOver) {
                lowerTurn= !lowerTurn;
                return lowerTurn ? "lower player wins.  Illegal move."
                        : "UPPER player wins.  Illegal move.";
            }
        }
        lowerTurn= !lowerTurn;
        String defaultRet= lowerTurn ? "lower> " : "UPPER> ";
        if (inCheck(!turn)) {
            List<String> nextMoves= availableMoves(!turn);
            if (nextMoves.size() == 0) {
                gameOver= true;
                return lowerTurn ? "UPPER player wins.  Checkmate."
                        : "lower player wins.  Checkmate.";
            }
            String ret= lowerTurn ? "lower player is in check!" : "UPPER player is in check!";
            ret+= "\n" + "Available moves:";
            for (String moves : nextMoves) {
                ret+= "\n" + moves;
            }
            ret+= "\n" + defaultRet;
            return ret;
        }
        if (numTurns == 400) {
            gameOver= true;
            return "Tie game.  Too many moves.";
        }
        return defaultRet;
    }

    /** Returns true given boolean turn if the player is in check, <br>
     * false if not in check. If turn is true then player is lower, <br>
     * else player is UPPER. */
    private boolean inCheck(boolean turn) {
        if (turn) {
            int[] dPosition= new int[2];
            for (int i= 0; i < Board.BOARD_SIZE; i++) {
                for (int j= 0; j < Board.BOARD_SIZE; j++) {
                    Piece curr= b.getPiece(i, j);
                    if (curr != null && curr.toString().equals("d")) {
                        dPosition[0]= i;
                        dPosition[1]= j;
                    }
                }
            }
            for (int i= 0; i < Board.BOARD_SIZE; i++) {
                for (int j= 0; j < Board.BOARD_SIZE; j++) {
                    Piece curr= b.getPiece(i, j);
                    if (curr != null && !curr.getLower()) {
                        if (curr.canMove(new int[] {i, j}, dPosition, b, false)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } else {
            int[] dPosition= new int[2];
            for (int i= 0; i < Board.BOARD_SIZE; i++) {
                for (int j= 0; j < Board.BOARD_SIZE; j++) {
                    Piece curr= b.getPiece(i, j);
                    if (curr != null && curr.toString().equals("D")) {
                        dPosition[0]= i;
                        dPosition[1]= j;
                    }
                }
            }
            for (int i= 0; i < Board.BOARD_SIZE; i++) {
                for (int j= 0; j < Board.BOARD_SIZE; j++) {
                    Piece curr= b.getPiece(i, j);
                    if (curr != null && curr.getLower()) {
                        if (curr.canMove(new int[] {i, j}, dPosition, b, false)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    /** Returns a list of moves to get the player out of check <br>
     * given the boolean turn. If turn is true, player is lower, else <br>
     * player is UPPER. */
    private List<String> availableMoves(boolean turn) {
        List<String> ret= new ArrayList<>();
        List<String> caps= new ArrayList<>();
        if (turn) {
            for (String i : lowerCaptures) {
                caps.add(i);
            }
        } else {
            for (String i : upperCaptures) {
                caps.add(i);
            }
        }
        List<int[]> myPieces= new ArrayList<>();
        for (int i= 0; i < Board.BOARD_SIZE; i++) {
            for (int j= 0; j < Board.BOARD_SIZE; j++) {
                int[] finalPos= new int[] {i, j};
                for (String potentialDrop : caps) {
                    String move= "drop " + potentialDrop.toLowerCase() + " "
                            + convertCoords(finalPos);
                    if (testDrop(move, turn))
                        ret.add(move);
                }
                Piece myPiece= b.getPiece(i, j);
                if (myPiece != null && myPiece.getLower() == lowerTurn)
                    myPieces.add(finalPos);
            }
        }
        for (int i= 0; i < Board.BOARD_SIZE; i++) {
            for (int j= 0; j < Board.BOARD_SIZE; j++) {
                int[] finalPos= new int[] {i, j};
                for (int[] pos : myPieces) {
                    String move= "move " + convertCoords(pos) + " " + convertCoords(finalPos);
                    if (testMove(move, turn))
                        ret.add(move);
                }
            }
        }
        Collections.sort(ret);
        return ret;
    }

    /** Returns true if String move on boolean turn will remove <br>
     * player from check. If turn is true, player is lower, else <br>
     * player is UPPER. The move input is a valid move command <br>
     * as specified by BoxShogi rules.  */
    private boolean testMove(String move, boolean turn) {
        boolean retVal= false;
        String[] oneMove= move.split(" ");
        assert oneMove[0].equals("move");
        int[] initPos= convertXY(oneMove[1]);
        int[] toPos= convertXY(oneMove[2]);
        Piece curPiece= b.getPiece(initPos[0], initPos[1]);
        if (isLegalMove(initPos, toPos, curPiece, false, turn)) {
            Piece enemyPiece= b.getPiece(toPos[0], toPos[1]);
            commitMove(initPos, toPos, curPiece, turn);
            if (!inCheck(turn))
                retVal= true;
            b.setBoardPiece(initPos[0], initPos[1], curPiece);
            b.setBoardPiece(toPos[0], toPos[1], enemyPiece);
            if (enemyPiece != null) {
                Piece newPiece= enemyPiece.beenCaptured();
                String enemyString= newPiece.toString();
                if (turn) {
                    for (int i= lowerCaptures.size() - 1; i >= 0; i--) {
                        if (lowerCaptures.get(i).equals(enemyString)) {
                            lowerCaptures.remove(i);
                            break;
                        }
                    }
                } else {
                    for (int i= upperCaptures.size() - 1; i >= 0; i--) {
                        if (upperCaptures.get(i).equals(enemyString)) {
                            upperCaptures.remove(i);
                            break;
                        }
                    }
                }
            }
        }
        return retVal;
    }

    /** Returns true if String drop on boolean turn will remove <br>
     * player from check. If turn is true, player is lower, else <br>
     * player is UPPER. The drop input is a drop move command <br>
     * as specified by BoxShogi rules.  */
    private boolean testDrop(String drop, boolean turn) {
        boolean retVal= false;
        String[] dropMove= drop.split(" ");
        assert dropMove[0].equals("drop");
        int[] dropPos= convertXY(dropMove[2]);
        if (isLegalDrop(dropMove[1], dropPos, turn)) {
            Piece newPiece= turn ? convertStrToPiece(dropMove[1], false)
                    : convertStrToPiece(dropMove[1].toUpperCase(), false);
            int pPos= turn ? lowerCaptures.indexOf(dropMove[1])
                    : upperCaptures.indexOf(dropMove[1].toUpperCase());
            commitMove(dropPos, newPiece, turn);
            if (!inCheck(turn))
                retVal= true;
            b.setBoardPiece(dropPos[0], dropPos[1], null);
            if (turn)
                lowerCaptures.add(pPos, dropMove[1]);
            else
                upperCaptures.add(pPos, dropMove[1].toUpperCase());
        }
        return retVal;
    }

    /** Returns true if a legal move of Piece curPiece with boolean pro from init <br>
     * to moveTo given boolean turn is valid and false otherwise. If turn is true, <br>
     * player is lower, and if false then UPPER. The boolean pro is true if curPiece <br>
     * is promoted and false otherwise. */
    private boolean isLegalMove(int[] init, int[] moveTo, Piece curPiece, boolean pro,
            boolean turn) {
        if (curPiece == null || curPiece.getLower() != turn
                || !curPiece.canMove(init, moveTo, b, false))
            return false;
        if (pro) {
            if (curPiece.getPromoted())
                return false;
            if (curPiece.getLower() && (init[1] == 4 || moveTo[1] == 4)
                    || !curPiece.getLower() && (init[1] == 0 || moveTo[1] == 0)) {
                if (!curPiece.promote())
                    return false;
                return true;
            }
            return false;
        }
        if (curPiece.toString().equals("p") && moveTo[1] == 4
                || curPiece.toString().equals("P") && moveTo[1] == 0) {
            curPiece.promote();
        }
        return true;
    }

    /** Returns true if a legal drop of String piece to dropPos with <br>
     * the given boolean turn is valid and false otherwise. If turn is true, <br>
     * player is lower, and if false then UPPER. The piece is represented as <br>
     * a lowercase string as given by the drop command from the user. */
    private boolean isLegalDrop(String piece, int[] dropPos, boolean turn) {
        if (b.isOccupied(dropPos[0], dropPos[1]))
            return false;
        if (turn) {
            if (!lowerCaptures.contains(piece)) {
                return false;
            }
            if (piece.equals("p")) {
                if (dropPos[1] == 4)
                    return false;
                for (int i= 0; i < Board.BOARD_SIZE; i++) {
                    Piece potential= b.getPiece(dropPos[0], i);
                    if (potential != null && potential.toString().equals("p"))
                        return false;
                }
            }
        } else {
            if (!upperCaptures.contains(piece.toUpperCase()))
                return false;
            if (piece.equals("p")) {
                if (dropPos[1] == 0)
                    return false;
                for (int i= 0; i < Board.BOARD_SIZE; i++) {
                    Piece potential= b.getPiece(dropPos[0], i);
                    if (potential != null && potential.toString().equals("P"))
                        return false;
                }
            }
        }
        if (piece.equals("p")) {
            boolean retVal= true;
            int pPos= turn ? lowerCaptures.indexOf("p") : upperCaptures.indexOf("P");
            Piece newPiece= turn ? convertStrToPiece("p", false) : convertStrToPiece("P", false);
            commitMove(dropPos, newPiece, turn);
            if (inCheck(!turn) && availableMoves(!turn).size() == 0) {
                retVal= false;
            }
            b.setBoardPiece(dropPos[0], dropPos[1], null);
            if (turn)
                lowerCaptures.add(pPos, "p");
            else
                upperCaptures.add(pPos, "P");
            return retVal;
        }
        return true;
    }

    /** Executes a legal move command given the initial coordinates initPos and <br>
     * drop coordinates moveTo, the piece to drop Piece curPiece, and the boolean <br>
     * turn of the player with true being lower and false being UPPER. Also checks to see <br>
     * if the committed move will move the current player into check, and if so, reverses <br>
     * the move and ends the game. */
    private void commitMove(int[] initPos, int[] moveTo, Piece curPiece, boolean turn) {
        boolean alreadyCheck= inCheck(turn);
        b.setBoardPiece(initPos[0], initPos[1], null);
        Piece potentialCap= b.getPiece(moveTo[0], moveTo[1]);
        Piece newCap= null;
        String capPiece= "";
        if (potentialCap != null) {
            newCap= potentialCap.beenCaptured();
            capPiece= newCap.toString();
            if (turn)
                lowerCaptures.add(capPiece);
            else
                upperCaptures.add(capPiece);
        }
        b.setBoardPiece(moveTo[0], moveTo[1], curPiece);
        if (!alreadyCheck && inCheck(turn)) {
            b.setBoardPiece(initPos[0], initPos[1], curPiece);
            b.setBoardPiece(moveTo[0], moveTo[1], potentialCap);
            if (potentialCap != null) {
                if (turn) {
                    for (int i= lowerCaptures.size() - 1; i >= 0; i--) {
                        if (lowerCaptures.get(i).equals(capPiece)) {
                            lowerCaptures.remove(i);
                            break;
                        }
                    }
                } else {
                    for (int i= upperCaptures.size() - 1; i >= 0; i--) {
                        if (upperCaptures.get(i).equals(capPiece)) {
                            upperCaptures.remove(i);
                            break;
                        }
                    }
                }
            }
            gameOver= true;
        }
    }

    /** Executes a legal drop command given the drop coordinates moveTo, <br>
     * the piece to drop Piece curPiece, and the boolean turn of the player <br>
     * with true being lower and false being UPPER. */
    private void commitMove(int[] moveTo, Piece curPiece, boolean turn) {
        if (turn)
            lowerCaptures.remove(curPiece.toString());
        else
            upperCaptures.remove(curPiece.toString());
        b.setBoardPiece(moveTo[0], moveTo[1], curPiece);
    }

    /** Returns the board representation as a string. */
    private String printBoard() {
        return b.toString();
    }

    /** Prints the current state of the game as the standard <br>
     * BoxShogi console format given a Game g, with the <br>
     * last move being documented as String lastRemoved and the <br>
     * statement to print as String justMoved. */
    private static void printGame(Game g, String lastRemoved, String justMoved) {
        if (g.numTurns != 0) {
            System.out.print( (!g.lowerTurn ? "lower" : "UPPER") + " player action: ");
            System.out.println(lastRemoved);
        }
        System.out.println(g.printBoard());
        System.out.print("Captures UPPER:");
        for (String i : g.upperCaptures) {
            System.out.print(" " + i);
        }
        System.out.println("");
        System.out.print("Captures lower:");
        for (String i : g.lowerCaptures) {
            System.out.print(" " + i);
        }
        System.out.println("");
        System.out.println("");
        System.out.print(justMoved);
    }

    /** Run Game on the arguments listed in args. <br>
     * If args doesn't match -f <fileName> or -i, the program <br>
     * exits after notifying the user of an illegal call. <br>
     * If args does match, a new Game will be instantiated of the <br>
     * respective mode, and the game is run by parsing moves <br>
     * and printing the board until the game is over. */
    public static void main(String[] args) throws Exception {
        Game shogi= null;
        if (args[0].equals("-f")) {
            shogi= new Game(Utils.parseTestCase(args[1]));
            String justMoved= "";
            String lastRemoved= "";
            while (!shogi.gameOver && !shogi.moves.isEmpty()) {
                justMoved= shogi.move(shogi.moves.get(0), shogi.lowerTurn);
                lastRemoved= shogi.moves.remove(0);
            }
            printGame(shogi, lastRemoved, justMoved);
        } else if (args[0].equals("-i")) {
            shogi= new Game();
            BufferedReader in= new BufferedReader(new InputStreamReader(System.in));
            String line= null;
            printGame(shogi, "", "lower> ");
            while (!shogi.gameOver) {
                line= in.readLine().trim();
                printGame(shogi, line, shogi.move(line, shogi.lowerTurn));
            }
        } else {
            System.out.println(
                    "Invalid mode. Please type -i for interactive mode or -f for file mode.");
        }
    }
}