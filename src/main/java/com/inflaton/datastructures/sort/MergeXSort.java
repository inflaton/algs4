package com.inflaton.datastructures.sort;

public class MergeXSort<T extends Comparable<T>> extends MergeSort<T> {
  // cutoff to insertion sort, must be >= 1
  private static final int INSERTION_SORT_CUTOFF = 7;

  public void preSort(T[] a) {
    auxArray = a.clone();
  }

  public void doSort(T[] a) {
    assert auxArray != null;
    sort(auxArray, a, 0, a.length - 1);
  }

  protected void merge(T[] src, T[] dst, int lo, int mid, int hi) {
    // precondition: src[lo .. mid] and src[mid+1 .. hi] are sorted subarrays
    assert isSorted(src, lo, mid);
    assert isSorted(src, mid + 1, hi);

    int i = lo, j = mid + 1;
    for (int k = lo; k <= hi; k++) {
      if (i > mid) dst[k] = src[j++];
      else if (j > hi) dst[k] = src[i++];
      else if (less(src[j], src[i])) dst[k] = src[j++]; // to ensure stability
      else dst[k] = src[i++];
    }

    // postcondition: dst[lo .. hi] is sorted subarray
    assert isSorted(dst, lo, hi);
  }

  protected void sort(T[] src, T[] dst, int lo, int hi) {
    // cutoff to insertion sort
    if (hi <= lo + INSERTION_SORT_CUTOFF) {
      insertionSort(dst, lo, hi);
      return;
    }
    int mid = lo + (hi - lo) / 2;
    sort(dst, src, lo, mid);
    sort(dst, src, mid + 1, hi);

    // using System.arraycopy() is a bit faster than the above loop
    if (!less(src[mid + 1], src[mid])) {
      System.arraycopy(src, lo, dst, lo, hi - lo + 1);
      return;
    }

    merge(src, dst, lo, mid, hi);
  }
}
