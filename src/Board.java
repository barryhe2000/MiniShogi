/** An instance represents a BoxShogi board for a game to be played. */
public class Board {
    /** The board on which BoxShogi is played. */
    private Piece[][] board;

    /** The length of the square board. */
    final static int  BOARD_SIZE= 5;

    /** Constructor: creates Board object with Pieces initialized in standard start <br>
     * positions if fileMode is false, otherwise in positions based on input file. */
    public Board(boolean fileMode) {
        board= new Piece[BOARD_SIZE][BOARD_SIZE];
        if (!fileMode)
            setInitBoard();
    }

    /** Helper that sets pieces in standard start position for interactive mode. */
    private void setInitBoard() {
        board[0][0]= new DPiece(true, false);
        board[1][0]= new SPiece(true, false);
        board[2][0]= new RPiece(true, false, false);
        board[3][0]= new GPiece(true, false, false);
        board[4][0]= new NPiece(true, false, false);
        board[0][1]= new PPiece(true, false, false);
        board[0][4]= new NPiece(false, false, false);
        board[1][4]= new GPiece(false, false, false);
        board[2][4]= new RPiece(false, false, false);
        board[3][4]= new SPiece(false, false);
        board[4][4]= new DPiece(false, false);
        board[4][3]= new PPiece(false, false, false);
    }

    /** Returns the piece on the board at the given coordinates x and y. */
    public Piece getPiece(int x, int y) {
        return board[x][y];
    }

    /** Sets a Piece p on the board according to the given coordinates x and y. */
    protected void setBoardPiece(int x, int y, Piece p) {
        board[x][y]= p;
    }

    /** Returns a String representation of the board. */
    @Override public String toString() {
        String[][] pieces= new String[BOARD_SIZE][BOARD_SIZE];
        for (int row= 0; row < BOARD_SIZE; row++) {
            for (int col= 0; col < BOARD_SIZE; col++) {
                pieces[col][row]= isOccupied(col, row) ? board[col][row].toString() : "";
            }
        }
        return stringifyBoard(pieces);
    }

    /** Returns if the position on the board given by (col, row) is occupied. */
    protected boolean isOccupied(int col, int row) {
        return board[col][row] != null;
    }

    /** Helper function that returns the String output of outside of the board. */
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

    /** Helper function that returns the String output of inside of the board. */
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