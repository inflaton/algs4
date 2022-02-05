import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.MinPQ;

public class BurrowsWheeler {

  private static class Node implements Comparable<Node> {
    private final int index;
    private final char key;

    public Node(int index, char key) {
      this.index = index;
      this.key = key;
    }

    public int getIndex() {
      return index;
    }

    public char getKey() {
      return key;
    }

    @Override
    public int compareTo(Node that) {
      int r = Character.compare(this.key, that.key);
      if (r == 0) {
        r = Integer.compare(this.index, that.index);
      }
      return r;
    }
  }
  // apply Burrows-Wheeler transform,
  // reading from standard input and writing to standard output
  public static void transform() {
    String input = BinaryStdIn.readString();
    char[] output = new char[input.length()];
    CircularSuffixArray circularSuffixArray = new CircularSuffixArray(input);
    for (int i = 0; i < input.length(); i++) {
      int index = circularSuffixArray.index(i);
      if (index == 0) {
        BinaryStdOut.write(i);
        index = input.length() - 1;
      } else {
        index--;
      }
      output[i] = input.charAt(index);
    }

    BinaryStdOut.write(String.valueOf(output));
    BinaryStdOut.close();
  }

  // apply Burrows-Wheeler inverse transform,
  // reading from standard input and writing to standard output
  public static void inverseTransform() {
    final int first = BinaryStdIn.readInt();
    String input = BinaryStdIn.readString();
    char[] t = input.toCharArray();

    MinPQ<Node> pq = new MinPQ<>(input.length());
    int i;
    for (i = 0; i < t.length; i++) {
      pq.insert(new Node(i, t[i]));
    }

    char[] s = new char[t.length];
    int[] next = new int[t.length];
    i = 0;
    while (!pq.isEmpty()) {
      Node node = pq.delMin();
      s[i] = node.getKey();
      next[i] = node.getIndex();
      i++;
    }

    int pointer = first;
    for (i = 0; i < t.length; i++) {
      BinaryStdOut.write((byte) s[pointer]);
      pointer = next[pointer];
    }
    BinaryStdOut.flush();
  }

  // if args[0] is "-", apply Burrows-Wheeler transform
  // if args[0] is "+", apply Burrows-Wheeler inverse transform
  public static void main(String[] args) {
    if (args[0].equals("-")) {
      transform();
    } else if (args[0].equals("+")) {
      inverseTransform();
    }
  }
}
