import edu.princeton.cs.algs4.*;

public class BurrowsWheelerTest {

  public static void inverseTransformTimed(boolean bruteForce) {
    Stopwatch sw = new Stopwatch();

    if (bruteForce) {
      bruteForce();
    } else {
      BurrowsWheeler.inverseTransform();
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
      BurrowsWheeler.transform();
    } else if (args[0].equals("+")) {
      BurrowsWheeler.inverseTransform();
    } else if (args[0].equals("a")) {
      inverseTransformTimed(false);
    } else if (args[0].equals("b")) {
      inverseTransformTimed(true);
    }
  }
}
