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

class FastCollinearPointsTest {

  boolean includingLargeData;
  private MergeSort<Point> mergeSort;
  private MergeBUSort<Point> mergeBUSort;
  private QuickSort<Point> quickSort;
  private Quick3waySort<Point> quick3waySort;
  private QuickXSort<Point> quickXSort;
  private InsertionSort<Point> insertionSort;
  private InsertionXSort<Point> insertionXSort;
  private SelectionSort<Point> selectionSort;
  private ShellSort<Point> shellSort;

  @BeforeEach
  void setUp() {
    includingLargeData = true;
    mergeSort = new MergeSort<>();
    mergeBUSort = new MergeBUSort<>();
    quickSort = new QuickSort<>();
    quick3waySort = new Quick3waySort<>();
    quickXSort = new QuickXSort<>();
    insertionSort = new InsertionSort<>();
    insertionXSort = new InsertionXSort<>();
    selectionSort = new SelectionSort<>();
    shellSort = new ShellSort<>();
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

  FastCollinearPoints runTestCase(String filename) {
    Point[] points = loadFile(filename);
    Stopwatch stopwatch = new Stopwatch();
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    double elapsedTime = stopwatch.elapsedTime();
    int numberOfSegments = collinear.numberOfSegments();
    StdOut.println("numberOfSegments: " + numberOfSegments);
    StdOut.println("elapsedTime: " + elapsedTime);
    assertEquals(numberOfSegments, collinear.segments().length);
    return collinear;
  }

  void testSortInterface(SortInterface<Point> sortInterface) {
    FastCollinearPoints.setSortInterface(sortInterface);
    FastCollinearPoints collinear = runTestCase("rs1423.txt");
    assertEquals(443, collinear.numberOfSegments());

    collinear = runTestCase("equidistant.txt");
    assertEquals(4, collinear.numberOfSegments());

    collinear = runTestCase("input400.txt");
    assertEquals(7, collinear.numberOfSegments());

    collinear = runTestCase("input1000.txt");
    assertEquals(0, collinear.numberOfSegments());

    if (includingLargeData && "true".equals(System.getenv("includingLargeData"))) {
      collinear = runTestCase("mystery10089.txt");
      assertEquals(34, collinear.numberOfSegments());

      collinear = runTestCase("input10000.txt");
      assertEquals(35, collinear.numberOfSegments());
    }
  }

  @Test
  void testJavaArraysSort() {
    testSortInterface(null);
  }

  @Test
  void testJavaArraysParallelSort() {
    SortInterface<Point> parallelSort = Arrays::parallelSort;
    testSortInterface(parallelSort);
  }

  @Test
  void testMergeSort() {
    testSortInterface(mergeSort);
  }

  @Test
  void testMergeBUSort() {
    testSortInterface(mergeBUSort);
  }

  @Test
  void testQuickSort() {
    testSortInterface(quickSort);
  }

  @Test
  void testQuick3waySort() {
    testSortInterface(quick3waySort);
  }

  @Test
  void testQuickXSort() {
    testSortInterface(quickXSort);
  }

  @Test
  void testInsertionSort() {
    includingLargeData = false;
    testSortInterface(insertionSort);
  }

  @Test
  void testInsertionXSort() {
    includingLargeData = false;
    testSortInterface(insertionXSort);
  }

  @Test
  void testSelectionSort() {
    includingLargeData = false;
    testSortInterface(selectionSort);
  }

  @Test
  void testShellSort() {
    includingLargeData = false;
    testSortInterface(shellSort);
  }
}
