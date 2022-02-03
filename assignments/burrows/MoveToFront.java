import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
  // alphabet size of extended ASCII
  private static final int R = 256;
  private static final int[] charArray = new int[R];
  private static final int[] indexArray = new int[R];

  private static void resetArrays() {
    for (int i = 0; i < R; i++) {
      charArray[i] = i;
      indexArray[i] = i;
    }
  }

  // apply move-to-front encoding, reading from standard input and writing to standard output
  public static void encode() {
    resetArrays();

    while (!BinaryStdIn.isEmpty()) {
      byte b = BinaryStdIn.readByte();
      byte i = indexOf(b);
      moveToFront(i);
      BinaryStdOut.write(i);
    }

    BinaryStdOut.close();
  }

  private static byte indexOf(byte b) {
    return (byte) indexArray[b];
  }

  private static byte charAt(int i) {
    return (byte) charArray[i];
  }

  private static void moveToFront(int k) {
    if (k > 0) {
      int swap = charArray[k];
      for (int i = k; i > 0; i--) {
        indexArray[charArray[i - 1]] = i;
        charArray[i] = charArray[i - 1];
      }
      charArray[0] = swap;
      indexArray[swap] = 0;
    }
  }

  // apply move-to-front decoding, reading from standard input and writing to standard output
  public static void decode() {
    resetArrays();

    while (!BinaryStdIn.isEmpty()) {
      byte i = BinaryStdIn.readByte();
      byte b = charAt(i);
      moveToFront(i);
      BinaryStdOut.write(b);
    }

    BinaryStdOut.close();
  }

  // if args[0] is "-", apply move-to-front encoding
  // if args[0] is "+", apply move-to-front decoding
  public static void main(String[] args) {
    if (args[0].equals("-")) {
      encode();
    } else if (args[0].equals("+")) {
      decode();
    }
  }
}
