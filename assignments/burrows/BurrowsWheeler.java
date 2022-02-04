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
    bruteForce();
  }

  public static void inverseTransformTimed(boolean bruteForce) {
    Stopwatch sw = new Stopwatch();

    if (bruteForce) {
      bruteForce();
    } else {
      inverseTransform();
    }

    StdOut.println("\nbruteForce: " + bruteForce + "\nelapsedTime: " + sw.elapsedTime());
    BinaryStdOut.close();
  }

  private static void bruteForce() {
    final int first = BinaryStdIn.readInt();
    String input = BinaryStdIn.readString();
    char[] t = input.toCharArray();

    Integer[] s = new Integer[t.length];
    int i;
    for (i = 0; i < t.length; i++) {
      s[i] = Integer.valueOf(t[i]);
    }
    MergeX.sort(s);

    int[] next = new int[t.length];
    boolean[] marked = new boolean[t.length];
    for (i = 0; i < t.length; i++) {
      for (int j = 0; j < t.length; j++) {
        if (!marked[j]) {
          if (s[i] == t[j]) {
            marked[j] = true;
            next[i] = j;
            break;
          }
        }
      }
    }

    char[] output = new char[input.length()];
    int pointer = first;
    for (i = 0; i < t.length; i++) {
      output[i] = (char) s[pointer].intValue();
      pointer = next[pointer];
    }

    BinaryStdOut.write(String.valueOf(output));
    BinaryStdOut.flush();
  }

  // if args[0] is "-", apply Burrows-Wheeler transform
  // if args[0] is "+", apply Burrows-Wheeler inverse transform
  public static void main(String[] args) {
    if (args[0].equals("-")) {
      transform();
    } else if (args[0].equals("+")) {
      inverseTransform();
    } else if (args[0].equals("a")) {
      inverseTransformTimed(false);
    } else if (args[0].equals("b")) {
      inverseTransformTimed(true);
    }
  }
}
