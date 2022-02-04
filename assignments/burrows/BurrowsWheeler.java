import edu.princeton.cs.algs4.*;

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

    //    BinaryStdOut.write(String.valueOf(output));
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
