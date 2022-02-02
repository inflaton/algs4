import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.TST;

public class WordFinder {
  private final int numOfVertices;
  private final boolean[] onStack;
  private final String[] boardLetters;
  private final SET<String> allWords;
  private final TST<Integer> tstDict;
  private final BoggleBoard board;

  public WordFinder(TST<Integer> tstDict, BoggleBoard board) {
    if (board == null) {
      throw new IllegalArgumentException();
    }

    this.tstDict = tstDict;
    this.board = board;
    numOfVertices = board.rows() * board.cols() + 1;

    boardLetters = new String[numOfVertices];
    for (int v = 0; v < numOfVertices; v++) {
      if (v == 0) {
        boardLetters[v] = "";
      } else {
        int i = (v - 1) / board.cols();
        int j = (v - 1) % board.cols();
        char letter = board.getLetter(i, j);
        if (letter == 'Q') {
          boardLetters[v] = "QU";
        } else {
          boardLetters[v] = String.valueOf(letter);
        }
      }
    }

    allWords = new SET<>();
    onStack = new boolean[numOfVertices];

    String prefix = "";
    dfs(prefix, 0);
  }

  private void dfs(String prefix, int v) {
    prefix = prefix + boardLetters[v];

    if (prefix.length() > 2) {
      if (tstDict.contains(prefix)) {
        allWords.add(prefix);
      } else if (!tstDict.keysWithPrefix(prefix).iterator().hasNext()) {
        return;
      }
    }
    onStack[v] = true;
    for (int w : adj(v)) {
      if (!onStack[w]) {
        dfs(prefix, w);
      }
    }
    onStack[v] = false;
  }

  private SET<Integer> adj(int v) {
    SET<Integer> SET = new SET<>();
    if (v == 0) { // virtual start vertex
      for (int w = 1; w < numOfVertices; w++) {
        SET.add(w);
      }
    } else {
      int i = (v - 1) / board.cols();
      int j = (v - 1) % board.cols();

      checkThenAdd(SET, i, j - 1);
      checkThenAdd(SET, i, j + 1);
      checkThenAdd(SET, i - 1, j);
      checkThenAdd(SET, i + 1, j);

      checkThenAdd(SET, i - 1, j - 1);
      checkThenAdd(SET, i - 1, j + 1);
      checkThenAdd(SET, i + 1, j - 1);
      checkThenAdd(SET, i + 1, j + 1);
    }

    return SET;
  }

  private void checkThenAdd(SET<Integer> SET, int i, int j) {
    if (i >= 0 && i < board.rows() && j >= 0 && j < board.cols()) {
      SET.add(vertexOf(i, j));
    }
  }

  private int vertexOf(int i, int j) {
    return i * board.cols() + j + 1;
  }

  public SET<String> getAllWords() {
    return allWords;
  }
}
