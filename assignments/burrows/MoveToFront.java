import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
  private static class Worker {
    // alphabet size of extended ASCII
    private static final int R = 256;
    private final int[] charArray;
    private final int[] indexArray;

    public Worker() {
      charArray = new int[R];
      indexArray = new int[R];
      for (int i = 0; i < R; i++) {
        charArray[i] = i;
        indexArray[i] = i;
      }
    }

    private byte indexOf(byte b) {
      return (byte) indexArray[((int) b) & 0xff];
    }

    private byte charAt(int i) {
      return (byte) charArray[i];
    }

    private void moveToFront(int k) {
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
  }

  // apply move-to-front encoding, reading from standard input and writing to standard output
  public static void encode() {
    Worker worker = new Worker();

    while (!BinaryStdIn.isEmpty()) {
      byte b = BinaryStdIn.readByte();
      byte i = worker.indexOf(b);
      worker.moveToFront(i);
      BinaryStdOut.write(i);
    }

    BinaryStdOut.close();
  }

  // apply move-to-front decoding, reading from standard input and writing to standard output
  public static void decode() {
    Worker worker = new Worker();

    while (!BinaryStdIn.isEmpty()) {
      byte i = BinaryStdIn.readByte();
      byte b = worker.charAt(i);
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
