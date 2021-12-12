package com.inflaton.datastructures.sort;

import java.util.Comparator;

public interface SortInterface<T> {
  public void sort(T[] a, Comparator<? super T> c);
}
