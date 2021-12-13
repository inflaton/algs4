package com.inflaton.practices.collinear;

import com.inflaton.datastructures.sort.SortInterface;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastXCollinearPoints {
  // finds all line segments containing 4 or more points
  private final LineSegment[] segments;
  private final Point[] sortedPoints;

  // finds all line segments containing 4 points
  public FastXCollinearPoints(Point[] points) {
    if (points == null) {
      throw new IllegalArgumentException();
    }
    for (Point point : points) {
      if (point == null) {
        throw new IllegalArgumentException();
      }
    }

    final int n = points.length;
    sortedPoints = Arrays.copyOf(points, n);
    if (n > 1) {
      sort(sortedPoints);
      for (int i = 0; i < n - 1; i++) {
        if (sortedPoints[i].compareTo(sortedPoints[i + 1]) == 0) {
          throw new IllegalArgumentException();
        }
      }
    }
    ArrayList<LineSegment> segmentsArrayList = findAllLineSegments();
    segments = new LineSegment[segmentsArrayList.size()];
    segmentsArrayList.toArray(segments);
  }

  private ArrayList<LineSegment> findAllLineSegments() {
    ArrayList<LineSegment> segmentsArrayList = new ArrayList<>();
    final int n = sortedPoints.length;
    if (n < 4) {
      return segmentsArrayList;
    }

    for (int p = 0; p < n; p++) {
      Point pp = sortedPoints[p];
      Comparator<Point> slopeOrder = pp.slopeOrder();
      Point[] points = Arrays.copyOf(sortedPoints, n);
      sort(points, slopeOrder);

      int startIndex = 0;
      int endIndex = 1;
      while (endIndex < n) {
        if (endIndex == n - 1 || slopeOrder.compare(points[startIndex], points[endIndex]) != 0) {
          if (endIndex == n - 1 && slopeOrder.compare(points[startIndex], points[endIndex]) == 0)
            endIndex++;
          int len = endIndex - startIndex;
          if (len >= 3) {
            if (pp.compareTo(points[startIndex]) < 0) {
              LineSegment seg = new LineSegment(pp, points[endIndex - 1]);
              segmentsArrayList.add(seg);
            }
          }
          startIndex = endIndex;
        }
        endIndex++;
      }
    }

    return segmentsArrayList;
  }

  // the number of line segments
  public int numberOfSegments() {
    return segments.length;
  }

  // the line segments
  public LineSegment[] segments() {
    return Arrays.copyOf(segments, segments.length);
  }

  private static SortInterface sortInterface;

  public static void setSortInterface(SortInterface sortInterface) {
    FastXCollinearPoints.sortInterface = sortInterface;
  }

  private void sort(Point[] points) {
    sort(points, null);
  }

  private void sort(Point[] points, Comparator<Point> comparator) {
    if (sortInterface == null) {
      Arrays.sort(points, comparator);
    } else {
      sortInterface.sort(points, comparator);
    }
  }

  public static void main(String[] args) {
    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
      p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    Stopwatch stopwatch = new Stopwatch();
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    double elapsedTime = stopwatch.elapsedTime();
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdOut.println("numberOfSegments: " + collinear.numberOfSegments());
    StdOut.println("elapsedTime: " + elapsedTime);
    StdDraw.show();
  }
}
