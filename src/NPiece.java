/** An instance is a Box Notes piece. */
class NPiece extends Piece {
    /** Constructor: creates an NPiece object with the piece type  <br>
     * representing notes. The value of lower determines if it <br>
     * is a lower or UPPER piece, and the value of promoted determines <br>
     * if it is promoted or not. The value of captured determines <br>
     * whether or not the piece is captured. */
    public NPiece(boolean lower, boolean promoted, boolean captured) {
        super("n", lower, promoted, captured);
    }

    /** Returns whether or not this piece can move from initPos to finalPos on board. */
    @Override protected boolean canMove(int[] initPos, int[] finalPos, Board board,
            boolean behind) {
        if (!Piece.checkBounds(initPos, finalPos) || hitOwnPiece(initPos, finalPos, board))
            return false;
        int deltaI= Math.abs(initPos[0] - finalPos[0]);
        int deltaJ= Math.abs(initPos[1] - finalPos[1]);
        boolean driveCheck= deltaI <= 1 && deltaJ <= 1;
        if (getPromoted()) {
            if (driveCheck)
                return true;
        }
        boolean notesCheck= true;
        if (! (deltaI == 0 && deltaJ != 0 || deltaI != 0 && deltaJ == 0))
            notesCheck= false;
        if (deltaI != 0) {
            if (initPos[0] > finalPos[0]) {
                for (int i= initPos[0] - 1; i > finalPos[0]; i--) {
                    if (board.getPiece(i, initPos[1]) != null)
                        notesCheck= false;
                }
            } else {
                for (int i= initPos[0] + 1; i < finalPos[0]; i++) {
                    if (board.getPiece(i, initPos[1]) != null)
                        notesCheck= false;
                }
            }
        } else {
            if (initPos[1] > finalPos[1]) {
                for (int i= initPos[1] - 1; i > finalPos[1]; i--) {
                    if (board.getPiece(initPos[0], i) != null)
                        notesCheck= false;
                }
            } else {
                for (int i= initPos[1] + 1; i < finalPos[1]; i++) {
                    if (board.getPiece(initPos[0], i) != null)
                        notesCheck= false;
                }
            }
        }
        if (!behind) {
            Piece behindPiece= pieceBehind(initPos, board);
            if (behindPiece == null)
                return notesCheck;
            return behindPiece.canMove(initPos, finalPos, board, true) || notesCheck;
        }
        return notesCheck;
    }

    /** Promotes this piece and returns if promotion was successful. */
    @Override protected boolean promote() {
        setPromoted(true);
        return true;
    }

    /** Returns a captured version of this piece, with promotion <br>
     * stripped and player ownership changing sides. */
    @Override protected Piece beenCaptured() {
        return new NPiece(!getLower(), false, !getCaptured());
    }
}