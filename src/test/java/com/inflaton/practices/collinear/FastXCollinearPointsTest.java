package com.inflaton.practices.collinear;

import com.inflaton.datastructures.sort.*;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FastXCollinearPointsTest {

  boolean includingLargeData;
  private MergeSort<Point> mergeSort;
  private MergeBUSort<Point> mergeBUSort;
  private MergeXSort<Point> mergeXSort;
  private QuickSort<Point> quickSort;
  private Quick3waySort<Point> quick3waySort;
  private QuickXSort<Point> quickXSort;
  private InsertionSort<Point> insertionSort;
  private InsertionXSort<Point> insertionXSort;
  private SelectionSort<Point> selectionSort;
  private ShellSort<Point> shellSort;
  private HeapSort<Point> heapSort;

  @BeforeEach
  void setUp() {
    includingLargeData = true;
    mergeSort = new MergeSort<>();
    mergeBUSort = new MergeBUSort<>();
    mergeXSort = new MergeXSort<>();
    quickSort = new QuickSort<>();
    quick3waySort = new Quick3waySort<>();
    quickXSort = new QuickXSort<>();
    insertionSort = new InsertionSort<>();
    insertionXSort = new InsertionXSort<>();
    selectionSort = new SelectionSort<>();
    shellSort = new ShellSort<>();
    heapSort = new HeapSort<>();
  }

  @AfterEach
  void tearDown() {}

  Point[] loadFile(String filename) {
    filename = "src/test/data/collinear/" + filename;
    StdOut.println("Loading points from file: " + filename);
    In in = new In(filename);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }
    return points;
  }

  FastXCollinearPoints runTestCase(String filename) {
    Point[] points = loadFile(filename);
    Stopwatch stopwatch = new Stopwatch();
    FastXCollinearPoints collinear = new FastXCollinearPoints(points);
    double elapsedTime = stopwatch.elapsedTime();
    int numberOfSegments = collinear.numberOfSegments();
    StdOut.println("numberOfSegments: " + numberOfSegments);
    StdOut.println("elapsedTime: " + elapsedTime);
    assertEquals(numberOfSegments, collinear.segments().length);
    return collinear;
  }

  void testSortInterface(SortInterface<Point> sortInterface, boolean stableSort) {
    FastXCollinearPoints.setSortInterface(sortInterface, stableSort);
    FastXCollinearPoints collinear = runTestCase("input1000.txt");
    assertEquals(0, collinear.numberOfSegments());

    collinear = runTestCase("equidistant.txt");
    assertEquals(4, collinear.numberOfSegments());

    collinear = runTestCase("input299.txt");
    assertEquals(6, collinear.numberOfSegments());

    collinear = runTestCase("input400.txt");
    assertEquals(7, collinear.numberOfSegments());

    collinear = runTestCase("rs1423.txt");
    assertEquals(443, collinear.numberOfSegments());

    if (includingLargeData && "true".equals(System.getenv("includingLargeData"))) {
      collinear = runTestCase("mystery10089.txt");
      assertEquals(34, collinear.numberOfSegments());

      collinear = runTestCase("input10000.txt");
      assertEquals(35, collinear.numberOfSegments());
    }
  }

  @Test
  void testJavaArraysSort() {
    testSortInterface(null, true);
  }

  @Test
  void testJavaArraysParallelSort() {
    SortInterface<Point> parallelSort = Arrays::parallelSort;
    testSortInterface(parallelSort, true);
  }

  @Test
  void testMergeSort() {
    testSortInterface(mergeSort, true);
  }

  @Test
  void testMergeBUSort() {
    testSortInterface(mergeBUSort, true);
  }

  @Test
  void testMergeXSort() {
    testSortInterface(mergeXSort, true);
  }

  @Test
  void testQuickSort() {
    testSortInterface(quickSort, false);
  }

  @Test
  void testQuick3waySort() {
    testSortInterface(quick3waySort, false);
  }

  @Test
  void testQuickXSort() {
    testSortInterface(quickXSort, false);
  }

  @Test
  void testHeapSort() {
    testSortInterface(heapSort, false);
  }

  @Test
  void testInsertionSort() {
    includingLargeData = false;
    testSortInterface(insertionSort, true);
  }

  @Test
  void testInsertionXSort() {
    includingLargeData = false;
    testSortInterface(insertionXSort, true);
  }

  @Test
  void testSelectionSort() {
    includingLargeData = false;
    testSortInterface(selectionSort, false);
  }

  @Test
  void testShellSort() {
    includingLargeData = false;
    testSortInterface(shellSort, false);
  }
}
