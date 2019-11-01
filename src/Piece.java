/** An instance is a Box Shogi piece with the piece type represented as a String. */
public class Piece {

	// add more fields
	String pieceType; // d, n, g, s, r, p
	boolean lower; // if true then lower piece else UPPER piece
	boolean promoted; // if true then promoted else not

	/** Constructor: creates a Piece object with the type represented <br>
	 * by the value of pieceType. The value of lower determines if it <br>
	 * is a lower or UPPER piece, and the value of promoted determines <br>
	 * if the piece is promoted or not. Drive ("d") and Shield ("s") <br>
	 * pieces cannot be promoted. */
	public Piece(String pieceType, boolean lower, boolean promoted) {
		this.lower= lower;
		this.pieceType= pieceType;
		this.promoted= pieceType.equals("d") || pieceType.equals("s") ? false : promoted;
	}

	/** String representation of a Piece object. */
	@Override
	public String toString() {
		return promoted ? "+" : "" + (lower ? pieceType : pieceType.toUpperCase());
	}
}
