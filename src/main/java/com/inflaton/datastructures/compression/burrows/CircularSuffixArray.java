package com.inflaton.datastructures.compression.burrows;

import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {

  private static class IndexedSuffix implements Comparable<IndexedSuffix> {
    private final int index;
    private final char[] suffixChars;

    public IndexedSuffix(int i, char[] chars) {
      index = i;
      suffixChars = chars;
    }

    @Override
    public int compareTo(IndexedSuffix that) {
      int r = 0;
      for (int i = 0; i < suffixChars.length; i++) {
        r = Character.compare(this.charAt(i), that.charAt(i));
        if (r != 0) {
          break;
        }
      }

      return r;
    }

    private char charAt(int i) {
      i += index;
      if (i >= suffixChars.length) {
        i -= suffixChars.length;
      }
      return suffixChars[i];
    }
  }

  private final IndexedSuffix[] suffixArray;

  // circular suffix array of s
  public CircularSuffixArray(String s) {
    if (s == null) {
      throw new IllegalArgumentException();
    }
    char[] suffixChars = s.toCharArray();
    suffixArray = new IndexedSuffix[s.length()];
    for (int i = 0; i < s.length(); i++) {
      suffixArray[i] = new IndexedSuffix(i, suffixChars);
    }

    MergeX.sort(suffixArray);
  }

  // length of s
  public int length() {
    return suffixArray.length;
  }

  // returns index of ith sorted suffix
  public int index(int i) {
    if (i < 0 || i >= suffixArray.length) {
      throw new IllegalArgumentException();
    }
    return suffixArray[i].index;
  }

  // unit testing (required)
  public static void main(String[] args) {
    String s = "ABRACADABRA!";
    CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
    for (int i = 0; i < circularSuffixArray.length(); i++) {
      int index = circularSuffixArray.index(i);
      StdOut.println(index);
    }
  }
}
