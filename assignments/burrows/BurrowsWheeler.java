import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.IndexMinPQ;

public class BurrowsWheeler {

  // apply Burrows-Wheeler transform,
  // reading from standard input and writing to standard output
  public static void transform() {
    String input = BinaryStdIn.readString();
    int length = input.length();
    CircularSuffixArray csa = new CircularSuffixArray(input);

    // find "first" and output
    for (int i = 0; i < length; i++) {
      if (csa.index(i) == 0) {
        // write a 32-bit int
        BinaryStdOut.write(i);
        break;
      }
    }

    // find the string "t"
    for (int i = 0; i < length; i++) {
      // the i-th original suffix string
      int index = csa.index(i);
      // get the index of its last character
      int lastIndex = index == 0 ? length - 1 : index - 1;
      // append these characters, and then we get "t"
      BinaryStdOut.write(input.charAt(lastIndex));
    }

    BinaryStdOut.close();
  }

  // apply Burrows-Wheeler inverse transform,
  // reading from standard input and writing to standard output
  public static void inverseTransform() {
    // input
    int first = BinaryStdIn.readInt();
    String t = BinaryStdIn.readString();
    int length = t.length();

    IndexMinPQ<Long> pq = new IndexMinPQ<>(length);
    int i;
    for (i = 0; i < length; i++) {
      long key = ((long) t.charAt(i) << Integer.SIZE) | i;
      pq.insert(i, key);
    }

    int[] next = new int[length];
    i = 0;
    while (!pq.isEmpty()) {
      next[i] = pq.delMin();
      i++;
    }

    // output
    for (i = 0; i < length; i++) {
      BinaryStdOut.write(t.charAt(next[first]));
      first = next[first];
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
