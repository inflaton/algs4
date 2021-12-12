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

  private MergeSort<Point> mergeSort;
  private QuickSort<Point> quickSort;
  private Quick3waySort<Point> quick3waySort;
  private QuickXSort<Point> quickXSort;
  private InsertionSort<Point> insertionSort;

  @BeforeEach
  void setUp() {
    mergeSort = new MergeSort<>();
    quickSort = new QuickSort<>();
    quick3waySort = new Quick3waySort<>();
    quickXSort = new QuickXSort<>();
    insertionSort = new InsertionSort<>();
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
    //    assertEquals(443, collinear.numberOfSegments());

    collinear = runTestCase("equidistant.txt");
    assertEquals(4, collinear.numberOfSegments());

    collinear = runTestCase("input400.txt");
    assertEquals(7, collinear.numberOfSegments());

    collinear = runTestCase("input1000.txt");
    assertEquals(0, collinear.numberOfSegments());

    //    collinear = runTestCase("mystery10089.txt");
    //    assertEquals(34, collinear.numberOfSegments());
    //
    //    collinear = runTestCase("input10000.txt");
    //    assertEquals(35, collinear.numberOfSegments());
  }

  @Test
  void useJavaArraysSort() {
    testSortInterface(null);
  }

  @Test
  void useJavaArraysParallelSort() {
    SortInterface<Point> parallelSort = Arrays::parallelSort;

    testSortInterface(parallelSort);
  }

  @Test
  void useMergeSort() {
    testSortInterface(mergeSort);
  }

  @Test
  void useQuickSort() {
    testSortInterface(quickSort);
  }

  @Test
  void useQuick3waySort() {
    testSortInterface(quick3waySort);
  }

  @Test
  void useQuickXSort() {
    testSortInterface(quickXSort);
  }

  @Test
  void useInsertionSort() {
    testSortInterface(insertionSort);
  }
}
