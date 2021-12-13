package com.inflaton.datastructures.sort;

import java.util.Comparator;

public abstract class AbstractSort<T extends Comparable<T>> implements SortInterface<T> {

  private Comparator<? super T> comparator;

  public void setComparator(Comparator<? super T> comparator) {
    this.comparator = comparator;
  }

  public void sort(T[] a) {
    sort(a, null);
  }

  public void sort(T[] a, Comparator<? super T> c) {
    setComparator(c);
    preSort(a);
    doSort(a);
    assert isSorted(a);
  }

  public void preSort(T[] a) {
    assert a != null;
  }

  public abstract void doSort(T[] a);

  /***************************************************************************
   *  Helper sorting functions.
   ***************************************************************************/
  protected int compare(T v, T w) {
    if (v == w) return 0; // optimization when reference equals
    if (comparator == null) {
      return v.compareTo(w);
    }
    return comparator.compare(v, w);
  }

  // is v < w ?
  protected boolean less(T v, T w) {
    return compare(v, w) < 0;
  }

  // exchange a[i] and a[j]
  protected void swap(Object[] a, int i, int j) {
    Object o = a[i];
    a[i] = a[j];
    a[j] = o;
  }

  /***************************************************************************
   *  Check if array is sorted - useful for debugging.
   ***************************************************************************/
  protected boolean isSorted(T[] a) {
    return isSorted(a, 0, a.length - 1);
  }

  protected boolean isSorted(T[] a, int lo, int hi) {
    for (int i = lo + 1; i <= hi; i++) if (less(a[i], a[i - 1])) return false;
    return true;
  }
}
