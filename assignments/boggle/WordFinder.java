import java.util.ArrayList;
import java.util.HashSet;

public class WordFinder {
  private final int numOfVertices;
  private final boolean[] onStack;
  private final char[] boardLetters;
  private final HashSet<String> allWords;
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

    boardLetters = new char[numOfVertices];
    adjVerticesArray = new int[numOfVertices][];

    for (int v = 0; v < numOfVertices; v++) {
      adjVerticesArray[v] = adjVertices(v);
      if (v > 0) {
        int i = (v - 1) / board.cols();
        int j = (v - 1) % board.cols();
        boardLetters[v] = board.getLetter(i, j);
      }
    }

    allWords = new HashSet<>();
    onStack = new boolean[numOfVertices];

    dfs(null, 0);
  }

  private void dfs(BoggleTrie.Node node, int v) {
    if (v > 0) {
      node = trieDict.getNextNode(node, boardLetters[v]);

      if (node == null) {
        return;
      }
      if (node.isWord() && node.getWord().length() > 2) {
        allWords.add(node.getWord());
      }
    }

    onStack[v] = true;
    for (int w : adjVerticesArray[v]) {
      if (!onStack[w]) {
        dfs(node, w);
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

  public Iterable<String> getAllWords() {
    return allWords;
  }
}
