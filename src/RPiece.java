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
    @Override protected boolean canMove(int[] initPos, int[] finalPos, Board board,
            boolean behind) {
        if (!Piece.checkBounds(initPos, finalPos) || hitOwnPiece(initPos, finalPos, board))
            return false;
        int deltaI= Math.abs(initPos[0] - finalPos[0]);
        int deltaJ= Math.abs(initPos[1] - finalPos[1]);
        boolean relayCheck= true;
        if (deltaI > 1 || deltaJ > 1)
            relayCheck= false;
        Piece curr= board.getPiece(initPos[0], initPos[1]);
        if (getPromoted()) {
            if (curr.getLower() && finalPos[1] == initPos[1] - 1
                    && (finalPos[0] == initPos[0] - 1 || finalPos[0] == initPos[0] + 1))
                relayCheck= false;
            if (!curr.getLower() && finalPos[1] == initPos[1] + 1
                    && (finalPos[0] == initPos[0] - 1 || finalPos[0] == initPos[0] + 1))
                relayCheck= false;
            if (! (deltaI <= 1 && deltaJ <= 1))
                relayCheck= false;
        }

        if (curr.getLower()) {
            if ( (finalPos[0] == initPos[0] + 1 || finalPos[0] == initPos[0] - 1)
                    && finalPos[1] == initPos[1]
                    || finalPos[0] == initPos[0] && finalPos[1] == initPos[1] - 1)
                relayCheck= false;
        } else {
            if ( (finalPos[0] == initPos[0] + 1 || finalPos[0] == initPos[0] - 1)
                    && finalPos[1] == initPos[1]
                    || finalPos[0] == initPos[0] && finalPos[1] == initPos[1] + 1)
                relayCheck= false;
        }
        if (!behind) {
            Piece behindPiece= pieceBehind(initPos, board);
            if (behindPiece == null)
                return relayCheck;
            return behindPiece.canMove(initPos, finalPos, board, true) || relayCheck;
        }
        return relayCheck;
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