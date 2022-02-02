import edu.princeton.cs.algs4.SET;

public class WordFinder {
  private final int numOfVertices;
  private final boolean[] onStack;
  private final String[] boardLetters;
  private final SET<String> allWords;
  private final BoggleTrie trieDict;
  private final BoggleBoard board;

  public WordFinder(BoggleTrie trieDict, BoggleBoard board) {
    if (board == null) {
      throw new IllegalArgumentException();
    }

    this.trieDict = trieDict;
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
      if (trieDict.contains(prefix)) {
        allWords.add(prefix);
      } else if (!trieDict.keysWithPrefix(prefix).iterator().hasNext()) {
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
    SET<Integer> adjVertices = new SET<>();
    if (v == 0) { // virtual start vertex
      for (int w = 1; w < numOfVertices; w++) {
        adjVertices.add(w);
      }
    } else {
      int i = (v - 1) / board.cols();
      int j = (v - 1) % board.cols();

      checkThenAdd(adjVertices, i, j - 1);
      checkThenAdd(adjVertices, i, j + 1);
      checkThenAdd(adjVertices, i - 1, j);
      checkThenAdd(adjVertices, i + 1, j);

      checkThenAdd(adjVertices, i - 1, j - 1);
      checkThenAdd(adjVertices, i - 1, j + 1);
      checkThenAdd(adjVertices, i + 1, j - 1);
      checkThenAdd(adjVertices, i + 1, j + 1);
    }

    return adjVertices;
  }

  private void checkThenAdd(SET<Integer> adjVertices, int i, int j) {
    if (i >= 0 && i < board.rows() && j >= 0 && j < board.cols()) {
      adjVertices.add(vertexOf(i, j));
    }
  }

  private int vertexOf(int i, int j) {
    return i * board.cols() + j + 1;
  }

  public SET<String> getAllWords() {
    return allWords;
  }
}
