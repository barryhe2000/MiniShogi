import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

	static class InitialPosition {
		String piece;
		String position;

		public InitialPosition(String pc, String pos) {
			piece= pc;
			position= pos;
		}

		@Override
		public String toString() {
			return piece + " " + position;
		}
	}

	static class TestCase {

		List<InitialPosition> initialPieces;
		List<String> upperCaptures;
		List<String> lowerCaptures;
		List<String> moves;

		public TestCase(List<InitialPosition> ip, List<String> uc, List<String> lc, List<String> m) {
			initialPieces= ip;
			upperCaptures= uc;
			lowerCaptures= lc;
			moves= m;
		}

		@Override
		public String toString() {
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

	public static TestCase parseTestCase(String path) throws Exception {
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

		return new TestCase(initialPieces, upperCaptures, lowerCaptures, moves);
	}
}