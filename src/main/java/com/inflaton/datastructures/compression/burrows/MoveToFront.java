package com.inflaton.datastructures.compression.burrows;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
  private static class Worker {
    // alphabet size of extended ASCII
    private static final int R = 256;
    private final byte[] byteArray;
    private final int[] indexArray;

    public Worker() {
      byteArray = new byte[R];
      indexArray = new int[R];
      for (int i = 0; i < R; i++) {
        byte b = (byte) i;
        byteArray[i] = b;
        indexArray[i] = i;
      }
    }

    public int indexOf(byte b) {
      return indexArray[toInt(b)];
    }

    public byte byteAt(int i) {
      return byteArray[i];
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
          indexArray[toInt(b)] = i;
          byteArray[i] = b;
        }
        byteArray[0] = swap;
        indexArray[toInt(swap)] = 0;
      }
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
