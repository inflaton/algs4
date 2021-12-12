package com.inflaton.datastructures.sort;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Comparator;

public class QuickSort<T extends Comparable> implements SortInterface<T> {
  private Comparator<? super T> comparator;

  public void sort(T[] a, Comparator<? super T> c) {
    this.comparator = c;
    StdRandom.shuffle(a);
    sort(a, 0, a.length - 1);
    assert isSorted(a);
  }

  public void sort(T[] a) {
    sort(a, null);
  }

  // quicksort the subarray from a[lo] to a[hi]
  private void sort(T[] a, int lo, int hi) {
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

      exch(a, i, j);
    }

    // put partitioning item v at a[j]
    exch(a, lo, j);

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
    if (k < 0 || k >= a.length) {
      throw new IllegalArgumentException("index is not between 0 and " + a.length + ": " + k);
    }
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

  /***************************************************************************
   *  Helper sorting functions.
   ***************************************************************************/

  // is v < w ?
  private boolean less(T v, T w) {
    if (v == w) return false; // optimization when reference equals
    if (comparator == null) {
      return v.compareTo(w) < 0;
    }
    return comparator.compare(v, w) < 0;
  }

  // exchange a[i] and a[j]
  private void exch(Object[] a, int i, int j) {
    Object swap = a[i];
    a[i] = a[j];
    a[j] = swap;
  }

  /***************************************************************************
   *  Check if array is sorted - useful for debugging.
   ***************************************************************************/
  private boolean isSorted(T[] a) {
    return isSorted(a, 0, a.length - 1);
  }

  private boolean isSorted(T[] a, int lo, int hi) {
    for (int i = lo + 1; i <= hi; i++) if (less(a[i], a[i - 1])) return false;
    return true;
  }
}
