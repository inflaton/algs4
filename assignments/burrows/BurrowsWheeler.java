import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

  private static final int R = 256;

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

    // compute frequency counts
    int[] count = new int[R + 1];
    for (int i = 0; i < length; i++) {
      count[t.charAt(i) + 1]++;
    }
    // transform counts to indices
    for (int i = 0; i < R; i++) {
      count[i + 1] += count[i];
    }
    // generate next array
    int[] next = new int[length];
    for (int i = 0; i < length; i++) {
      char c = t.charAt(i);
      next[count[c]] = i;
      count[c]++;
    }

    // output
    for (int i = 0; i < length; i++) {
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
