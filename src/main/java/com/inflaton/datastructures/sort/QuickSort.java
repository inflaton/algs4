package com.inflaton.datastructures.sort;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Comparator;

public class QuickSort<T extends Comparable<T>> extends AbstractSort<T> {

  public void preSort(T[] a) {
    StdRandom.shuffle(a);
  }

  public void doSort(T[] a) {
    sort(a, 0, a.length - 1);
  }

  // quicksort the subarray from a[lo] to a[hi]
  public void sort(T[] a, int lo, int hi) {
    if (hi <= lo) return;
    int j = partition(a, lo, hi);
    sort(a, lo, j - 1);
    sort(a, j + 1, hi);
    assert isSorted(a, lo, hi);
  }

  // partition the subarray a[lo..hi] so that a[lo..j-1] <= a[j] <= a[j+1..hi]
  // and return the index j.
  private int partition(T[] a, int lo, int hi) {
    int i = lo;
    int j = hi + 1;
    T v = a[lo];
    while (true) {

      // find item on lo to swap
      while (less(a[++i], v)) {
        if (i == hi) break;
      }

      // find item on hi to swap
      while (less(v, a[--j])) {
        if (j == lo) break; // redundant since a[lo] acts as sentinel
      }

      // check if pointers cross
      if (i >= j) break;

      swap(a, i, j);
    }

    // put partitioning item v at a[j]
    swap(a, lo, j);

    // now, a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
    return j;
  }

  /**
   * Rearranges the array so that {@code a[k]} contains the kth smallest key; {@code a[0]} through
   * {@code a[k-1]} are less than (or equal to) {@code a[k]}; and {@code a[k+1]} through {@code
   * a[n-1]} are greater than (or equal to) {@code a[k]}.
   *
   * @param a the array
   * @param k the rank of the key
   * @return the key of rank {@code k}
   * @throws IllegalArgumentException unless {@code 0 <= k < a.length}
   */
  public T select(T[] a, int k) {
    return select(a, k, null);
  }

  public T select(T[] a, int k, Comparator<? super T> c) {
    if (k < 0 || k >= a.length) {
      throw new IllegalArgumentException("index is not between 0 and " + a.length + ": " + k);
    }

    setComparator(c);
    StdRandom.shuffle(a);

    int lo = 0, hi = a.length - 1;
    while (hi > lo) {
      int i = partition(a, lo, hi);
      if (i > k) hi = i - 1;
      else if (i < k) lo = i + 1;
      else return a[i];
    }
    return a[lo];
  }

  /**
   * Reads in a sequence of strings from standard input; quicksorts them; and prints them to
   * standard output in ascending order. Shuffles the array and then prints the strings again to
   * standard output, but this time, using the select method.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    QuickSort<String> quickSort = new QuickSort<>();

    String[] a = StdIn.readAllStrings();
    quickSort.sort(a);

    for (int i = 0; i < a.length; i++) {
      StdOut.println(a[i]);
    }
    assert quickSort.isSorted(a);

    // shuffle
    StdRandom.shuffle(a);

    // display results again using select
    StdOut.println();
    for (int i = 0; i < a.length; i++) {
      String ith = (String) quickSort.select(a, i);
      StdOut.println(ith);
    }
  }
}
