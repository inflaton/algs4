package com.inflaton.datastructures.sort;

public class SelectionSort<T extends Comparable<T>> extends AbstractSort<T> {

  public void doSort(T[] a) {
    int n = a.length;
    for (int i = 0; i < n; i++) {
      int min = i;
      for (int j = i + 1; j < n; j++) {
        if (less(a[j], a[min])) min = j;
      }
      exch(a, i, min);
      assert isSorted(a, 0, i);
    }
  }
}
