package com.inflaton.datastructures.sort;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuickSortTest {
  private static final int TEST_SIZE = 10000;
  private QuickSort<Integer> quickSort;
  private Integer[] a;

  @BeforeEach
  void setUp() {
    quickSort = new QuickSort<>();
    a = new Integer[TEST_SIZE];
    for (int i = 0; i < TEST_SIZE; i++) {
      a[i] = i;
    }
    StdRandom.shuffle(a);
  }

  @Test
  void testSort() {
    int i = StdRandom.uniform(TEST_SIZE);
    quickSort.sort(a);
    assertEquals(i, a[i]);
    assertTrue(quickSort.isSorted(a));
  }

  @Test
  void testSelect() {
    int i = StdRandom.uniform(TEST_SIZE);
    assertEquals(i, quickSort.select(a, i));
    assertFalse(quickSort.isSorted(a));
  }
}
