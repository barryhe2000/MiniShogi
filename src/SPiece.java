/** An instance is a Box Shield piece. */
class SPiece extends Piece {
    /** Constructor: creates a SPiece object with the piece type  <br>
     * representing shield. The value of lower determines if it <br>
     * is a lower or UPPER piece, and the value of captured determines <br>
     * whether or not the piece is captured. Shield pieces cannot be <br>
     * promoted. */
    public SPiece(boolean lower, boolean captured) {
        super("s", lower, false, captured);
    }

    /** Returns whether or not this piece can move from initPos to finalPos on board. */
    @Override protected boolean canMove(int[] initPos, int[] finalPos, Board board) {
        if (!Piece.checkBounds(initPos, finalPos) || hitOwnPiece(initPos, finalPos, board))
            return false;
        int deltaI= Math.abs(initPos[0] - finalPos[0]);
        int deltaJ= Math.abs(initPos[1] - finalPos[1]);
        Piece curr= board.getPiece(initPos[0], initPos[1]);
        if (curr.getLower() && finalPos[1] == initPos[1] - 1
                && (finalPos[0] == initPos[0] - 1 || finalPos[0] == initPos[0] + 1))
            return false;
        if (!curr.getLower() && finalPos[1] == initPos[1] + 1
                && (finalPos[0] == initPos[0] - 1 || finalPos[0] == initPos[0] + 1))
            return false;
        return deltaI <= 1 && deltaJ <= 1;
    }

    /** Promotes this piece and returns if promotion was successful. */
    @Override protected boolean promote() {
        return false;
    }

    /** Returns a captured version of this piece, with promotion <br>
     * stripped and player ownership changing sides. */
    @Override protected Piece beenCaptured() {
        return new SPiece(!getLower(), !getCaptured());
    }
}