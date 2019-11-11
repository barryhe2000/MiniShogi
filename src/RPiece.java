/** An instance is a Box Relay piece. */
class RPiece extends Piece {
    /** Constructor: creates an RPiece object with the piece type  <br>
     * representing relay. The value of lower determines if it <br>
     * is a lower or UPPER piece, and the value of promoted determines <br>
     * if it is promoted or not. The value of captured determines <br>
     * whether or not the piece is captured. */
    public RPiece(boolean lower, boolean promoted, boolean captured) {
        super("r", lower, promoted, captured);
    }

    /** Returns whether or not this piece can move from initPos to finalPos on board. */
    @Override protected boolean canMove(int[] initPos, int[] finalPos, Board board) {
        if (!Piece.checkBounds(initPos, finalPos) || hitOwnPiece(initPos, finalPos, board))
            return false;
        int deltaI= Math.abs(initPos[0] - finalPos[0]);
        int deltaJ= Math.abs(initPos[1] - finalPos[1]);
        if (deltaI > 1 || deltaJ > 1)
            return false;
        Piece curr= board.getPiece(initPos[0], initPos[1]);
        if (getPromoted()) {
            if (curr.getLower() && finalPos[1] == initPos[1] - 1
                    && (finalPos[0] == initPos[0] - 1 || finalPos[0] == initPos[0] + 1))
                return false;
            if (!curr.getLower() && finalPos[1] == initPos[1] + 1
                    && (finalPos[0] == initPos[0] - 1 || finalPos[0] == initPos[0] + 1))
                return false;
            return deltaI <= 1 && deltaJ <= 1;
        }
        if (curr.getLower())
            return ! ( (finalPos[0] == initPos[0] + 1 || finalPos[0] == initPos[0] - 1)
                    && finalPos[1] == initPos[1]
                    || finalPos[0] == initPos[0] && finalPos[1] == initPos[1] - 1);
        return ! ( (finalPos[0] == initPos[0] + 1 || finalPos[0] == initPos[0] - 1)
                && finalPos[1] == initPos[1]
                || finalPos[0] == initPos[0] && finalPos[1] == initPos[1] + 1);
    }

    /** Promotes this piece and returns if promotion was successful. */
    @Override protected boolean promote() {
        setPromoted(true);
        return true;
    }

    /** Returns a captured version of this piece, with promotion <br>
     * stripped and player ownership changing sides. */
    @Override protected Piece beenCaptured() {
        return new RPiece(!getLower(), false, !getCaptured());
    }
}