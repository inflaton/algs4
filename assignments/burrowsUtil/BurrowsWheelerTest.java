import edu.princeton.cs.algs4.*;

public class BurrowsWheelerTest {

  // apply Burrows-Wheeler transform,
  // reading from standard input and writing to standard output
  public static void transform_12th() {
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
  public static void inverseTransform_12th() {
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

  public static void inverseTransformTimed(String m) {
    Stopwatch sw = new Stopwatch();

    if (m.equals("b")) {
      bruteForce();
    } else if (m.equals("c")) {
      inverseTransform_12th();
    } else {
      BurrowsWheeler.inverseTransform();
    }

    StdOut.println("\nmode: " + m + "\nelapsedTime: " + sw.elapsedTime());
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
    } else {
      inverseTransformTimed(args[0]);
    }
  }
}
