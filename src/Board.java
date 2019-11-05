/** Class to represent Box Shogi board */
public class Board {

    // add more fields
    Piece[][] board;
    final int BOARD_SIZE= 5;

    public static void main(String[] args) {
        Board b= new Board();
        System.out.println(b.toString());
    }

    /** Constructor: creates Board object and initializes Pieces in place. */
    public Board() {
        board= new Piece[BOARD_SIZE][BOARD_SIZE];
        for (int i= 0; i < BOARD_SIZE; i++) { // put starter pieces in place
            if (i == 0) {
                board[0][i]= new Piece("n", false, false);
                board[BOARD_SIZE - 1][BOARD_SIZE - 1 - i]= new Piece("n", true, false);
            } else if (i == 1) {
                board[0][i]= new Piece("g", false, false);
                board[BOARD_SIZE - 1][BOARD_SIZE - 1 - i]= new Piece("g", true, false);
            } else if (i == 2) {
                board[0][i]= new Piece("r", false, false);
                board[BOARD_SIZE - 1][BOARD_SIZE - 1 - i]= new Piece("r", true, false);
            } else if (i == 3) {
                board[0][i]= new Piece("s", false, false);
                board[BOARD_SIZE - 1][BOARD_SIZE - 1 - i]= new Piece("s", true, false);
            } else {
                board[0][i]= new Piece("d", false, false);
                board[1][i]= new Piece("p", false, false);
                board[BOARD_SIZE - 1][BOARD_SIZE - 1 - i]= new Piece("d", true, false);
                board[BOARD_SIZE - 2][BOARD_SIZE - 1 - i]= new Piece("p", true, false);
            }
        }
    }

    /* Print board */
    @Override public String toString() {
        String[][] pieces= new String[BOARD_SIZE][BOARD_SIZE];
        for (int row= 0; row < BOARD_SIZE; row++) {
            for (int col= 0; col < BOARD_SIZE; col++) {
                Piece curr= board[col][row];
                pieces[col][row]= isOccupied(col, row) ? board[col][row].toString() : "";
            }
        }
        return stringifyBoard(pieces);
    }

    private boolean isOccupied(int col, int row) {
        return board[col][row] != null;
    }

    private String stringifyBoard(String[][] board) {
        String str= "";
        for (int row= board.length - 1; row >= 0; row--) {

            str+= Integer.toString(row + 1) + " |";
            for (int col= 0; col < board[row].length; col++) {
                str+= stringifySquare(board[col][row]);
            }
            str+= System.getProperty("line.separator");
        }
        str+= "    a  b  c  d  e" + System.getProperty("line.separator");
        return str;
    }

    private String stringifySquare(String sq) {
        switch (sq.length()) {
            case 0:
                return "__|";
            case 1:
                return " " + sq + "|";
            case 2:
                return sq + "|";
        }
        throw new IllegalArgumentException(
                "Board must be an array of strings like \"\", \"P\", or \"+P\"");
    }
}