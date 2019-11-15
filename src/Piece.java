/** Abstract class representing the superclass of the six pieces.
 * Cannot be instantiated, will instead be instantiated by one of
 * the six subclasses representing the various pieces of the game. */
public abstract class Piece {
    /** Represents the type of this piece. */
    private String  pieceType;

    /** The player this piece belongs to. */
    private boolean lower;

    /** The promotion status of this piece. */
    private boolean promoted;

    /** The captured status of this piece. */
    private boolean captured;

    /** Constructor: creates a Piece object with the type represented <br>
     * by the value of pieceType. The value of lower determines if it <br>
     * is a lower or UPPER piece, and the value of promoted determines <br>
     * if the piece is promoted or not. The value of captured determines <br>
     * whether or not the piece is captured. Drive and Shield pieces <br>
     * cannot be promoted. */
    public Piece(String pieceType, boolean lower, boolean promoted, boolean captured) {
        setPieceType(pieceType);
        setLower(lower);
        setPromoted(pieceType.equals("d") || pieceType.equals("s") ? false : promoted);
        setCaptured(captured);
    }

    /** Returns whether or not this piece can move from initPos to finalPos on board. */
    protected abstract boolean canMove(int[] initPos, int[] finalPos, Board board, boolean behind);

    /** Promotes this piece and returns if promotion was successful. */
    protected abstract boolean promote();

    /** Returns a captured version of this piece, with promotion <br>
     * stripped and player ownership changing sides. */
    protected abstract Piece beenCaptured();

    /** Returns true if the initial and final position of <br>
     * pieces being moved does not reach out of the board boundaries and <br>
     * if the initial and final positions are different. */
    protected static boolean checkBounds(int[] initPos, int[] finalPos) {
        assert initPos[0] >= 0 && initPos[0] <= 4 && initPos[1] >= 0 && initPos[1] <= 4;
        if (initPos[0] == finalPos[0] && initPos[1] == finalPos[1])
            return false;
        return finalPos[0] >= 0 && finalPos[0] <= 4 && finalPos[1] >= 0 && finalPos[1] <= 4;
    }

    /** Returns true if one of the player's own pieces is collided into. */
    protected boolean hitOwnPiece(int[] initPos, int[] finalPos, Board b) {
        Piece p= b.getPiece(finalPos[0], finalPos[1]);
        return p != null && p.getLower() == lower;
    }

    /** Returns true if there is a piece behind initPos. */
    protected Piece pieceBehind(int[] initPos, Board b) {
        int[] finalPos= new int[] {initPos[0], initPos[1] + 1};
        if (lower) {
            finalPos= new int[] {initPos[0], initPos[1] - 1};
        }
        if (checkBounds(initPos, finalPos)) {
            Piece potential= b.getPiece(finalPos[0], finalPos[1]);
            return potential;
        }
        return null;
    }

    /** Returns the type of this piece. */
    public String getPieceType() {
        return pieceType;
    }

    /** Sets the type of this piece. */
    protected void setPieceType(String pieceType) {
        assert pieceType.equals("d") || pieceType.equals("s") || pieceType.equals("p")
                || pieceType.equals("n") || pieceType.equals("g") || pieceType.equals("r");
        this.pieceType= pieceType;
    }

    /** Returns the player this piece belongs to. */
    public boolean getLower() {
        return lower;
    }

    /** Sets the player this piece belongs to. */
    protected void setLower(boolean lower) {
        this.lower= lower;
    }

    /** Returns the promotion status of this piece. */
    public boolean getPromoted() {
        return promoted;
    }

    /** Sets the promotion status of this piece. */
    protected void setPromoted(boolean promoted) {
        this.promoted= promoted;
    }

    /** Returns the captured status of this piece. */
    public boolean getCaptured() {
        return captured;
    }

    /** Sets the captured status of this piece. */
    protected void setCaptured(boolean captured) {
        this.captured= captured;
    }

    /** Returns the String representation of a Piece object. */
    @Override public String toString() {
        return (promoted ? "+" : "") + (lower ? pieceType : pieceType.toUpperCase());
    }
}