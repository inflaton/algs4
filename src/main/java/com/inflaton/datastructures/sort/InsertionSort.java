package com.inflaton.datastructures.sort;

public class InsertionSort<T extends Comparable<T>> extends AbstractSort<T> {

  public void doSort(T[] a) {
    sort(a, 0, a.length - 1);
  }

  public void sort(T[] a, int lo, int hi) {
    for (int i = lo + 1; i <= hi; i++) {
      for (int j = i; j > lo && less(a[j], a[j - 1]); j--) {
        swap(a, j, j - 1);
      }
    }
    assert isSorted(a, lo, hi);
  }
}
