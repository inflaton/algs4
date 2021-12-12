package com.inflaton.datastructures.sort;

import java.util.Comparator;

public class MergeSort<T extends Comparable> implements SortInterface<T> {
  private Comparator<? super T> comparator;

  // stably merge a[lo .. mid] with a[mid+1 ..hi] using aux[lo .. hi]
  private void merge(T[] a, T[] aux, int lo, int mid, int hi) {
    // precondition: a[lo .. mid] and a[mid+1 .. hi] are sorted subarrays
    assert isSorted(a, lo, mid);
    assert isSorted(a, mid + 1, hi);

    // copy to aux[]
    for (int k = lo; k <= hi; k++) {
      aux[k] = a[k];
    }

    // merge back to a[]
    int i = lo, j = mid + 1;
    for (int k = lo; k <= hi; k++) {
      if (i > mid) a[k] = aux[j++];
      else if (j > hi) a[k] = aux[i++];
      else if (less(aux[j], aux[i])) a[k] = aux[j++];
      else a[k] = aux[i++];
    }

    // postcondition: a[lo .. hi] is sorted
    assert isSorted(a, lo, hi);
  }

  // mergesort a[lo..hi] using auxiliary array aux[lo..hi]
  private void sort(T[] a, T[] aux, int lo, int hi) {
    if (hi <= lo) return;
    int mid = lo + (hi - lo) / 2;
    sort(a, aux, lo, mid);
    sort(a, aux, mid + 1, hi);
    merge(a, aux, lo, mid, hi);
  }

  public void sort(T[] a, Comparator<? super T> c) {
    this.comparator = c;
    T[] aux = (T[]) new Comparable[a.length];
    sort(a, aux, 0, a.length - 1);
    assert isSorted(a);
  }

  public void sort(T[] a) {
    sort(a, null);
  }

  /***************************************************************************
   *  Helper sorting function.
   ***************************************************************************/

  // is v < w ?
  private boolean less(T v, T w) {
    if (v == w) return false; // optimization when reference equals
    if (comparator == null) {
      return v.compareTo(w) < 0;
    }
    return comparator.compare(v, w) < 0;
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

  /***************************************************************************
   *  Index mergesort.
   ***************************************************************************/
  // stably merge a[lo .. mid] with a[mid+1 .. hi] using aux[lo .. hi]
  private void merge(T[] a, int[] index, int[] aux, int lo, int mid, int hi) {

    // copy to aux[]
    for (int k = lo; k <= hi; k++) {
      aux[k] = index[k];
    }

    // merge back to a[]
    int i = lo, j = mid + 1;
    for (int k = lo; k <= hi; k++) {
      if (i > mid) index[k] = aux[j++];
      else if (j > hi) index[k] = aux[i++];
      else if (less(a[aux[j]], a[aux[i]])) index[k] = aux[j++];
      else index[k] = aux[i++];
    }
  }

  /**
   * Returns a permutation that gives the elements in the array in ascending order.
   *
   * @param a the array
   * @return a permutation {@code p[]} such that {@code a[p[0]]}, {@code a[p[1]]}, ..., {@code
   *     a[p[N-1]]} are in ascending order
   */
  public int[] indexSort(T[] a) {
    int n = a.length;
    int[] index = new int[n];
    for (int i = 0; i < n; i++) index[i] = i;

    int[] aux = new int[n];
    sort(a, index, aux, 0, n - 1);
    return index;
  }

  // mergesort a[lo..hi] using auxiliary array aux[lo..hi]
  private void sort(T[] a, int[] index, int[] aux, int lo, int hi) {
    if (hi <= lo) return;
    int mid = lo + (hi - lo) / 2;
    sort(a, index, aux, lo, mid);
    sort(a, index, aux, mid + 1, hi);
    merge(a, index, aux, lo, mid, hi);
  }
}
