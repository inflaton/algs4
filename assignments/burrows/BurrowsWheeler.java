import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.IndexMinPQ;

public class BurrowsWheeler {

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

    IndexMinPQ<Long> pq = new IndexMinPQ<>(input.length());
    int i;
    for (i = 0; i < t.length; i++) {
      long key = ((long) t[i] << Integer.SIZE) | i;
      pq.insert(i, key);
    }

    char[] s = new char[t.length];
    int[] next = new int[t.length];
    i = 0;
    while (!pq.isEmpty()) {
      s[i] = (char) (pq.minKey() >> Integer.SIZE);
      next[i] = pq.delMin();
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
