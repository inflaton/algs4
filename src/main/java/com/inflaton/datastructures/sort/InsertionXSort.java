package com.inflaton.datastructures.sort;

public class InsertionXSort<T extends Comparable<T>> extends AbstractSort<T> {

  public void doSort(T[] a) {
    int n = a.length;

    // put smallest element in position to serve as sentinel
    int exchanges = 0;
    for (int i = n - 1; i > 0; i--) {
      if (less(a[i], a[i - 1])) {
        exch(a, i, i - 1);
        exchanges++;
      }
    }
    if (exchanges == 0) return;

    // insertion sort with half-exchanges
    for (int i = 2; i < n; i++) {
      T v = a[i];
      int j = i;
      while (less(v, a[j - 1])) {
        a[j] = a[j - 1];
        j--;
      }
      a[j] = v;
    }
  }
}
