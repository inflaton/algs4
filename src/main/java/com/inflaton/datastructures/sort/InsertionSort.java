package com.inflaton.datastructures.sort;

public class InsertionSort<T extends Comparable<T>> extends AbstractSort<T> {

  public void doSort(T[] a) {
    sort(a, 0, a.length - 1);
  }

  public void sort(T[] a, int lo, int hi) {
    insertionSort(a, lo, hi);
  }
}
