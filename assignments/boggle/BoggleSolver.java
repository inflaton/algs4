import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

public class BoggleSolver {
  private final TST<Integer> tst;

  // Initializes the data structure using the given array of strings as the dictionary.
  // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
  public BoggleSolver(String[] dictionary) {
    tst = new TST<>();
    for (String word : dictionary) {
      int score = calcScore(word);
      tst.put(word, score);
    }
  }

  //  word length   points
  //        3–4    1
  //        5      2
  //        6      3
  //        7      5
  //        8+     11
  private int calcScore(String word) {
    int len = word.length();
    if (len > 7) {
      return 11;
    }
    if (len == 7 || len == 3) {
      return len - 2;
    }

    if (len > 3 && len < 7) {
      return len - 3;
    }

    return 0;
  }

  // Returns the set of all valid words in the given Boggle board, as an Iterable.
  public Iterable<String> getAllValidWords(BoggleBoard board) {
    return null;
  }

  // Returns the score of the given word if it is in the dictionary, zero otherwise.
  // (You can assume the word contains only the uppercase letters A through Z.)
  public int scoreOf(String word) {
    Integer value = tst.get(word);
    return value != null ? value.intValue() : 0;
  }

  public static void main(String[] args) {
    In in = new In(args[0]);
    String[] dictionary = in.readAllStrings();
    BoggleSolver solver = new BoggleSolver(dictionary);
    BoggleBoard board = new BoggleBoard(args[1]);
    int score = 0;
    for (String word : solver.getAllValidWords(board)) {
      StdOut.println(word);
      score += solver.scoreOf(word);
    }
    StdOut.println("Score = " + score);
  }
}
