import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class BoggleSolver {
  private final BoggleTrie trieDict;

  // Initializes the data structure using the given array of strings as the dictionary.
  // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
  public BoggleSolver(String[] dictionary) {
    if (dictionary == null) {
      throw new IllegalArgumentException();
    }
    trieDict = new BoggleTrie();
    for (String word : dictionary) {
      trieDict.add(word);
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
    return len > 3 ? len - 3 : 0;
  }

  // Returns the set of all valid words in the given Boggle board, as an Iterable.
  public Iterable<String> getAllValidWords(BoggleBoard board) {
    WordFinder wordFinder = new WordFinder(trieDict, board);
    return wordFinder.getAllWords();
  }

  // Returns the score of the given word if it is in the dictionary, zero otherwise.
  // (You can assume the word contains only the uppercase letters A through Z.)
  public int scoreOf(String word) {
    return trieDict.contains(word) ? calcScore(word) : 0;
  }

  public static void main(String[] args) {
    In in = new In(args[0]);
    String[] dictionary = in.readAllStrings();
    Stopwatch stopwatch = new Stopwatch();
    BoggleSolver solver = new BoggleSolver(dictionary);
    double elapsedTime = stopwatch.elapsedTime();
    StdOut.println("constructor elapsedTime: " + elapsedTime);

    for (int i = 1; i < args.length; i++) {
      String filename = args[i];
      StdOut.println(filename);
      BoggleBoard board = new BoggleBoard(filename);

      stopwatch = new Stopwatch();
      Iterable<String> words = solver.getAllValidWords(board);
      elapsedTime = stopwatch.elapsedTime();

      int score = 0;
      for (String word : words) {
        score += solver.scoreOf(word);
      }
      StdOut.println("Score = " + score);
      StdOut.println("getAllValidWords elapsedTime: " + elapsedTime);
    }
  }
}
