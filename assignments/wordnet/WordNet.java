import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class WordNet {

  private HashMap<String, Set<Integer>> nounToIdMapping;
  private ArrayList<String> synsetList;
  private SAP sap;
  private int nounCount;

  // constructor takes the name of the two input files
  public WordNet(String synsets, String hypernyms) {
    if (synsets == null || hypernyms == null) {
      throw new IllegalArgumentException();
    }

    nounToIdMapping = new HashMap<>();
    synsetList = new ArrayList<>();
    In in = new In(synsets);

    while (!in.isEmpty()) {
      String line = in.readLine();
      String[] parts = line.split(",");
      int id = Integer.parseInt(parts[0]);
      synsetList.add(parts[1]);
      String[] nouns = parts[1].split(" ");
      for (String noun : nouns) {
        Set<Integer> ids = nounToIdMapping.get(noun);
        if (ids == null) {
          ids = new HashSet<>();
          nounCount++;
        }
        ids.add(id);
        nounToIdMapping.put(noun, ids);
      }
    }

    in = new In(hypernyms);
    Digraph digraph = new Digraph(synsetList.size());
    while (!in.isEmpty()) {
      String line = in.readLine();
      String[] parts = line.split(",");

      if (parts.length < 2) {
        continue;
      }
      int id = Integer.parseInt(parts[0]);
      for (int i = 1; i < parts.length; i++) {
        int hypernymId = Integer.parseInt(parts[i]);
        digraph.addEdge(id, hypernymId);
      }
    }

    sap = new SAP(digraph);
  }

  // returns all WordNet nouns
  public Iterable<String> nouns() {
    return nounToIdMapping.keySet();
  }

  // is the word a WordNet noun?
  public boolean isNoun(String word) {
    if (word == null) {
      throw new IllegalArgumentException();
    }
    return nounToIdMapping.get(word) != null;
  }

  // distance between nounA and nounB (defined below)
  public int distance(String nounA, String nounB) {
    if (!isNoun(nounA) || !isNoun(nounB)) {
      throw new IllegalArgumentException();
    }
    return sap.length(nounToIdMapping.get(nounA), nounToIdMapping.get(nounB));
  }

  // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
  // in a shortest ancestral path (defined below)
  public String sap(String nounA, String nounB) {
    if (!isNoun(nounA) || !isNoun(nounB)) {
      throw new IllegalArgumentException();
    }
    int anc = sap.ancestor(nounToIdMapping.get(nounA), nounToIdMapping.get(nounB));
    return anc < 0 ? null : synsetList.get(anc);
  }

  // do unit testing of this class
  public static void main(String[] args) {
    WordNet wordNet = new WordNet(args[0], args[1]);

    while (!StdIn.isEmpty()) {
      String nounA = StdIn.readString();
      String nounB = StdIn.readString();
      StdOut.println(nounA + " isNoun: " + wordNet.isNoun(nounA));
      StdOut.println(nounB + " isNoun: " + wordNet.isNoun(nounB));
      if (wordNet.isNoun(nounA) && wordNet.isNoun(nounB)) {
        StdOut.printf(
            "sap = %s, distance = %d\n", wordNet.sap(nounA, nounB), wordNet.distance(nounA, nounB));
      }
    }
  }
}
