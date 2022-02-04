import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;
import java.util.HashMap;

public class MoveToFront {
  private static class Worker {
    // alphabet size of extended ASCII
    private static final int R = 256;
    private final byte[] byteArray;
    private final HashMap<Byte, Integer> indexMap;

    public Worker() {
      byteArray = new byte[R];
      indexMap = new HashMap<>();
      for (int i = 0; i < R; i++) {
        byte b = (byte) i;
        byteArray[i] = b;
        indexMap.put(b, i);
      }
    }

    public int indexOf(byte b) {
      return indexMap.get(b);
    }

    public byte byteAt(int i) {
      return (byte) byteArray[i];
    }

    public int toInt(byte b) {
      int i = b < 0 ? R + b : b;
      return i;
    }

    public void moveToFront(int k) {
      if (k > 0) {
        byte swap = byteArray[k];
        for (int i = k; i > 0; i--) {
          byte b = byteArray[i - 1];
          indexMap.put(b, i);
          byteArray[i] = b;
        }
        byteArray[0] = swap;
        indexMap.put(swap, 0);
      }
    }

    @Override
    public String toString() {
      return "Worker{" + "byteArray=" + Arrays.toString(byteArray) + ", indexMap=" + indexMap + '}';
    }
  }

  // apply move-to-front encoding, reading from standard input and writing to standard output
  public static void encode() {
    Worker worker = new Worker();

    while (!BinaryStdIn.isEmpty()) {
      byte b = BinaryStdIn.readByte();
      int i = worker.indexOf(b);
      worker.moveToFront(i);
      BinaryStdOut.write((byte) i);
    }

    BinaryStdOut.close();
  }

  // apply move-to-front decoding, reading from standard input and writing to standard output
  public static void decode() {
    Worker worker = new Worker();

    while (!BinaryStdIn.isEmpty()) {
      byte b = BinaryStdIn.readByte();
      int i = worker.toInt(b);
      b = worker.byteAt(i);
      worker.moveToFront(i);
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
