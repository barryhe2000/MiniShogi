import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** An instance represents a utility class for parsing file inputs in file mode
 * of the BoxShogi game. */
public class Utils {
    /** A static instance represents the initial position of a piece. */
    static class InitialPosition {
        /** Represents the piece. */
        String piece;

        /** Represents the position in letter number form (i.e. a4). */
        String position;

        /**Constructor: creates a static InitialPosition object given the name <br>
         * of the piece pc and the position pos of its initial location. */
        public InitialPosition(String pc, String pos) {
            piece= pc;
            position= pos;
        }

        /** Returns this piece. */
        public String getPiece() {
            return piece;
        }

        /** Returns the position of this piece. */
        public String getPosition() {
            return position;
        }

        /** Returns the String representation of the piece and its position. */
        @Override public String toString() {
            return piece + " " + position;
        }
    }

    /** A static instance represents the inputs given from the input file. */
    static class TestCase {
        /** The initial pieces and their positions. */
        List<InitialPosition> initialPieces;

        /** The pieces captured by the UPPER player. */
        List<String>          upperCaptures;

        /** The pieces captured by the lower player. */
        List<String>          lowerCaptures;

        /** The moves given to be made. */
        List<String>          moves;

        /**A static instance represents a test case that contains the file information <br>
         * regarding initial positions, upper captures, lower captures, and moves to <br>
         * be made. */
        public TestCase(List<InitialPosition> ip, List<String> uc, List<String> lc,
                List<String> m) {
            initialPieces= ip;
            upperCaptures= uc;
            lowerCaptures= lc;
            moves= m;
        }

        /** Returns the list of initial pieces. */
        public List<InitialPosition> getInitPieces() {
            return initialPieces;
        }

        /** Returns the list of upper captures. */
        public List<String> getUpperCaptures() {
            return upperCaptures;
        }

        /** Returns the list of lower captures. */
        public List<String> getLowerCaptures() {
            return lowerCaptures;
        }

        /** Returns the list of moves to be made. */
        public List<String> getMoves() {
            return moves;
        }

        /** Returns the String representation of the test case file. */
        @Override public String toString() {
            String str= "";
            str+= "initialPieces: [\n";
            for (InitialPosition piece : initialPieces) {
                str+= piece + "\n";
            }
            str+= "]\n";
            str+= "upperCaptures: [";
            for (String piece : upperCaptures) {
                str+= piece + " ";
            }
            str+= "]\n";
            str+= "lowerCaptures: [";
            for (String piece : lowerCaptures) {
                str+= piece + " ";
            }
            str+= "]\n";
            str+= "moves: [\n";
            for (String move : moves) {
                str+= move + "\n";
            }
            str+= "]";
            return str;
        }
    }

    /** Parses a file test case and returns a test case object containing the <br>
     * information of the initial pieces, upper and lower captures, and moves <br>
     * to be made. */
    public static TestCase parseTestCase(String path) throws Exception { //input file
        BufferedReader br= new BufferedReader(new FileReader(path));
        String line= br.readLine().trim();
        List<InitialPosition> initialPieces= new ArrayList<>();
        while (!line.equals("")) {
            String[] lineParts= line.split(" ");
            initialPieces.add(new InitialPosition(lineParts[0], lineParts[1]));
            line= br.readLine().trim();
        }
        line= br.readLine().trim();
        List<String> upperCaptures= Arrays.asList(line.substring(1, line.length() - 1).split(" "));
        line= br.readLine().trim();
        List<String> lowerCaptures= Arrays.asList(line.substring(1, line.length() - 1).split(" "));
        line= br.readLine().trim();
        line= br.readLine().trim();
        List<String> moves= new ArrayList<>();
        while (line != null) {
            line= line.trim();
            moves.add(line);
            line= br.readLine();
        }
        br.close();
        return new TestCase(initialPieces, upperCaptures, lowerCaptures, moves);
    }
}