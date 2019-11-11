/** An instance is a Box Governance piece. */
class GPiece extends Piece {
    /** Constructor: creates a GPiece object with the piece type  <br>
     * representing governance. The value of lower determines if it <br>
     * is a lower or UPPER piece, and the value of promoted determines <br>
     * if it is promoted or not. The value of captured determines <br>
     * whether or not the piece is captured. */
    public GPiece(boolean lower, boolean promoted, boolean captured) {
        super("g", lower, promoted, captured);
    }

    /** Returns whether or not this piece can move from initPos to finalPos on board. */
    @Override protected boolean canMove(int[] initPos, int[] finalPos, Board board) {
        if (!Piece.checkBounds(initPos, finalPos) || hitOwnPiece(initPos, finalPos, board))
            return false;
        int deltaI= Math.abs(initPos[0] - finalPos[0]);
        int deltaJ= Math.abs(initPos[1] - finalPos[1]);
        if (getPromoted()) {
            if (deltaI <= 1 && deltaJ <= 1)
                return true;
        }
        if (deltaI != deltaJ)
            return false;
        boolean right= initPos[0] < finalPos[0] ? true : false;
        boolean up= initPos[1] < finalPos[1] ? true : false;
        if (up) {
            if (right) {
                for (int i= 1; i < deltaI; i++) {
                    if (board.getPiece(initPos[0] + i, initPos[1] + i) != null)
                        return false;
                }
            } else {
                for (int i= 1; i < deltaI; i++) {
                    if (board.getPiece(initPos[0] - i, initPos[1] + i) != null)
                        return false;
                }
            }
        } else {
            if (right) {
                for (int i= 1; i < deltaI; i++) {
                    if (board.getPiece(initPos[0] + i, initPos[1] - i) != null)
                        return false;
                }
            } else {
                for (int i= 1; i < deltaI; i++) {
                    if (board.getPiece(initPos[0] - i, initPos[1] - i) != null)
                        return false;
                }
            }
        }
        return true;
    }

    /** Promotes this piece and returns if promotion was successful. */
    @Override protected boolean promote() {
        setPromoted(true);
        return true;
    }

    /** Returns a captured version of this piece, with promotion <br>
     * stripped and player ownership changing sides. */
    @Override protected Piece beenCaptured() {
        return new GPiece(!getLower(), false, !getCaptured());
    }
}