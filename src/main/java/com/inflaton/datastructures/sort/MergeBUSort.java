package com.inflaton.datastructures.sort;

public class MergeBUSort<T extends Comparable<T>> extends MergeSort<T> {

  // mergesort a[lo..hi] using auxiliary array aux[lo..hi]
  protected void sort(T[] a, T[] aux, int start, int end) {
    int n = end - start + 1;
    for (int len = 1; len < n; len *= 2) {
      for (int lo = start; lo < start + n - len; lo += len + len) {
        int mid = lo + len - 1;
        int hi = start + Math.min(lo + len + len - 1, n - 1);
        merge(a, aux, lo, mid, hi);
      }
    }
  }
}
