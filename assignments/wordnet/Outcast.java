import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
  private final WordNet wordnet;

  public Outcast(WordNet wordnet) {
    if (wordnet == null) {
      throw new IllegalArgumentException();
    }
    this.wordnet = wordnet;
  }

  public String outcast(String[] nouns) {
    if (nouns == null || nouns.length < 2) {
      throw new IllegalArgumentException();
    }
    int maxDistance = -1;
    int index = -1;
    for (int i = 0; i < nouns.length; i++) {
      if (nouns[i] == null || !wordnet.isNoun(nouns[i])) {
        throw new IllegalArgumentException();
      }
      int dist = calcDistance(nouns, i);
      if (maxDistance < dist) {
        maxDistance = dist;
        index = i;
      }
    }
    return nouns[index];
  }

  private int calcDistance(String[] nouns, int w) {
    int dist = 0;
    for (int i = 0; i < nouns.length; i++) {
      if (i != w) {
        int newDist = wordnet.distance(nouns[i], nouns[w]);
        if (newDist < 0) {
          newDist = Integer.MAX_VALUE;
        }
        dist += newDist;
      }
    }
    return dist;
  }

  public static void main(String[] args) {
    WordNet wordnet = new WordNet(args[0], args[1]);
    Outcast outcast = new Outcast(wordnet);
    for (int t = 2; t < args.length; t++) {
      In in = new In(args[t]);
      String[] nouns = in.readAllStrings();
      StdOut.println(
          args[t] + ": " + (nouns.length < 2 ? "length: " + nouns.length : outcast.outcast(nouns)));
    }
  }
}
