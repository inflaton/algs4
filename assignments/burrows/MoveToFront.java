import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;

public class MoveToFront {
  // alphabet size of extended ASCII
  private static final int R = 256;
  private final int[] charArray;
  private final int[] indexArray;

  private MoveToFront() {
    charArray = new int[R];
    indexArray = new int[R];
    for (int i = 0; i < R; i++) {
      charArray[i] = i;
      indexArray[i] = i;
    }
  }

  // apply move-to-front encoding, reading from standard input and writing to standard output
  public static void encode() {
    MoveToFront moveToFront = new MoveToFront();
    // StdOut.println(moveToFront);
    while (!BinaryStdIn.isEmpty()) {
      byte b = BinaryStdIn.readByte();
      byte i = moveToFront.indexOf(b);

      // StdOut.println((char) b + ": " + i);
      moveToFront.go(i);
      BinaryStdOut.write(i);
    }

    BinaryStdOut.close();
  }

  private byte indexOf(byte b) {
    return (byte) indexArray[b];
  }

  private byte charAt(int i) {
    return (byte) charArray[i];
  }

  private void swap(int i, int j) {
    int swap = charArray[i];
    charArray[i] = charArray[j];
    charArray[j] = swap;
    indexArray[charArray[i]] = i;
    indexArray[charArray[j]] = j;
  }

  private void go(int k) {
    while (k > 0) {
      swap(k, k - 1);
      k--;
    }
  }

  @Override
  public String toString() {
    return "MoveToFront{"
        + "\ncharArray="
        + Arrays.toString(charArray)
        + "\nindexArray="
        + Arrays.toString(indexArray)
        + "\n}";
  }

  // apply move-to-front decoding, reading from standard input and writing to standard output
  public static void decode() {
    MoveToFront moveToFront = new MoveToFront();
    // StdOut.println(moveToFront);
    while (!BinaryStdIn.isEmpty()) {
      byte i = BinaryStdIn.readByte();
      byte b = moveToFront.charAt(i);

      // StdOut.println((char) b + ": " + i);
      moveToFront.go(i);
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
