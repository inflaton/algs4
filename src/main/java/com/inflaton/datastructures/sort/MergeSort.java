package com.inflaton.datastructures.sort;

public class MergeSort<T extends Comparable<T>> extends AbstractSort<T> {

  protected T[] auxArray;

  public void preSort(T[] a) {
    if (auxArray == null || auxArray.length != a.length) auxArray = (T[]) new Comparable[a.length];
  }

  public void doSort(T[] a) {
    assert auxArray != null;
    sort(a, auxArray, 0, a.length - 1);
  }

  // stably merge a[lo .. mid] with a[mid+1 ..hi] using aux[lo .. hi]
  protected void merge(T[] a, T[] aux, int lo, int mid, int hi) {
    // precondition: a[lo .. mid] and a[mid+1 .. hi] are sorted subarrays
    assert isSorted(a, lo, mid);
    assert isSorted(a, mid + 1, hi);

    // copy to aux[]
    if (hi + 1 - lo >= 0) System.arraycopy(a, lo, aux, lo, hi + 1 - lo);

    // merge back to a[]
    int i = lo, j = mid + 1;
    for (int k = lo; k <= hi; k++) {
      if (i > mid) a[k] = aux[j++];
      else if (j > hi) a[k] = aux[i++];
      else if (less(aux[j], aux[i])) a[k] = aux[j++];
      else a[k] = aux[i++];
    }

    // post condition: a[lo .. hi] is sorted
    assert isSorted(a, lo, hi);
  }

  // mergesort a[lo..hi] using auxiliary array aux[lo..hi]
  protected void sort(T[] a, T[] aux, int lo, int hi) {
    if (hi <= lo) return;
    int mid = lo + (hi - lo) / 2;
    sort(a, aux, lo, mid);
    sort(a, aux, mid + 1, hi);
    merge(a, aux, lo, mid, hi);
  }
}
