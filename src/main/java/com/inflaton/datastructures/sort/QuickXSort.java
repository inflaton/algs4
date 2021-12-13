package com.inflaton.datastructures.sort;

public class QuickXSort<T extends Comparable<T>> extends QuickSort<T> {

  // cutoff to insertion sort, must be >= 1
  private static final int INSERTION_SORT_CUTOFF = 8;

  public void preSort(T[] a) {}

  // quicksort the subarray from a[lo] to a[hi]
  public void sort(T[] a, int lo, int hi) {
    if (hi <= lo) return;

    // cutoff to insertion sort (Insertion.sort() uses half-open intervals)
    int n = hi - lo + 1;
    if (n <= INSERTION_SORT_CUTOFF) {
      insertionSort(a, lo, hi);
      return;
    }

    int j = partition(a, lo, hi);
    sort(a, lo, j - 1);
    sort(a, j + 1, hi);
  }

  private void insertionSort(T[] a, int lo, int hi) {
    for (int i = lo + 1; i <= hi; i++) {
      for (int j = i; j > lo && less(a[j], a[j - 1]); j--) {
        swap(a, j, j - 1);
      }
    }
    assert isSorted(a, lo, hi);
  }

  // partition the subarray a[lo..hi] so that a[lo..j-1] <= a[j] <= a[j+1..hi]
  // and return the index j.
  private int partition(T[] a, int lo, int hi) {
    int n = hi - lo + 1;
    int m = median3(a, lo, lo + n / 2, hi);
    swap(a, m, lo);

    int i = lo;
    int j = hi + 1;
    T v = a[lo];

    // a[lo] is unique largest element
    while (less(a[++i], v)) {
      if (i == hi) {
        swap(a, lo, hi);
        return hi;
      }
    }

    // a[lo] is unique smallest element
    while (less(v, a[--j])) {
      if (j == lo + 1) return lo;
    }

    // the main loop
    while (i < j) {
      swap(a, i, j);
      while (less(a[++i], v))
        ;
      while (less(v, a[--j]))
        ;
    }

    // put partitioning item v at a[j]
    swap(a, lo, j);

    // now, a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
    return j;
  }

  // return the index of the median element among a[i], a[j], and a[k]
  private int median3(T[] a, int i, int j, int k) {
    return (less(a[i], a[j])
        ? (less(a[j], a[k]) ? j : less(a[i], a[k]) ? k : i)
        : (less(a[k], a[j]) ? j : less(a[k], a[i]) ? k : i));
  }
}
