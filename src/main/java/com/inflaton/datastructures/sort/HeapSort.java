package com.inflaton.datastructures.sort;

public class HeapSort<T extends Comparable<T>> extends AbstractSort<T> {

  public void doSort(T[] pq) {
    int n = pq.length;

    // heapify phase
    for (int k = n / 2; k >= 1; k--) sink(pq, k, n);

    // sortdown phase
    int k = n;
    while (k > 1) {
      swap(pq, 1, k--);
      sink(pq, 1, k);
    }
  }

  private void sink(T[] pq, int k, int n) {
    while (2 * k <= n) {
      int j = 2 * k;
      if (j < n && less(pq, j, j + 1)) j++;
      if (!less(pq, k, j)) break;
      swap(pq, k, j);
      k = j;
    }
  }

  /***************************************************************************
   * Helper functions for comparisons and swaps.
   * Indices are "off-by-one" to support 1-based indexing.
   ***************************************************************************/
  private boolean less(T[] pq, int i, int j) {
    return less(pq[i - 1], pq[j - 1]);
  }

  protected void swap(T[] pq, int i, int j) {
    super.swap(pq, i - 1, j - 1);
  }
}
