package com.inflaton.datastructures.sort;

public class Quick3waySort<T extends Comparable<T>> extends QuickSort<T> {

  // quicksort the subarray a[lo .. hi] using 3-way partitioning
  public void sort(T[] a, int lo, int hi) {
    if (hi <= lo) return;
    int lt = lo, gt = hi;
    T v = a[lo];
    int i = lo + 1;
    while (i <= gt) {
      int cmp = compare(a[i], v);
      if (cmp < 0) swap(a, lt++, i++);
      else if (cmp > 0) swap(a, i, gt--);
      else i++;
    }

    // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
    sort(a, lo, lt - 1);
    sort(a, gt + 1, hi);
    assert isSorted(a, lo, hi);
  }
}
