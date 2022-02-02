import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class WordFinder {
  private final int numOfVertices;
  private final boolean[] onStack;
  private final String[] boardLetters;
  private final SET<String> allWords;
  private final BoggleTrie trieDict;
  private final BoggleBoard board;
  private final int[][] adjVerticesArray;

  public WordFinder(BoggleTrie trieDict, BoggleBoard board) {
    if (board == null) {
      throw new IllegalArgumentException();
    }

    this.trieDict = trieDict;
    this.board = board;
    numOfVertices = board.rows() * board.cols() + 1;

    boardLetters = new String[numOfVertices];
    adjVerticesArray = new int[numOfVertices][];

    for (int v = 0; v < numOfVertices; v++) {
      adjVerticesArray[v] = adjVertices(v);
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

    if (prefix.length() > 0 && !trieDict.hasKeysWithPrefix(prefix)) {
      return;
    }

    if (prefix.length() > 2 && trieDict.contains(prefix)) {
      allWords.add(prefix);
    }

    onStack[v] = true;
    for (int w : adjVerticesArray[v]) {
      if (!onStack[w]) {
        dfs(prefix, w);
      }
    }
    onStack[v] = false;
  }

  private int[] adjVertices(int v) {
    ArrayList<Integer> adjVertices = new ArrayList<>();
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

    int[] array = new int[adjVertices.size()];
    for (int i = 0; i < array.length; i++) {
      array[i] = adjVertices.get(i);
    }
    return array;
  }

  private void checkThenAdd(ArrayList<Integer> adjVertices, int i, int j) {
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
