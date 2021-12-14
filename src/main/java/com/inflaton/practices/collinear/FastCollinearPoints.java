package com.inflaton.practices.collinear;

import com.inflaton.datastructures.sort.SortInterface;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
  // finds all line segments containing 4 or more points
  private final LineSegment[] segments;

  static SortInterface sortInterface;

  public static void setSortInterface(SortInterface sortInterface) {
    FastCollinearPoints.sortInterface = sortInterface;
  }

  // finds all line segments containing 4 or more points
  public FastCollinearPoints(Point[] points) {
    if (points == null) {
      throw new IllegalArgumentException();
    }
    for (Point point : points) {
      if (point == null) {
        throw new IllegalArgumentException();
      }
    }

    final int n = points.length;
    points = Arrays.copyOf(points, n);
    if (n > 1) {
      sort(points);
      for (int i = 0; i < n - 1; i++) {
        if (points[i].compareTo(points[i + 1]) == 0) {
          throw new IllegalArgumentException();
        }
      }
    }

    ArrayList<LineSegment> segmentsArrayList = findAllLineSegments(points);
    segments = new LineSegment[segmentsArrayList.size()];
    segmentsArrayList.toArray(segments);
  }

  private ArrayList<LineSegment> findAllLineSegments(Point[] points) {
    ArrayList<LineSegment> segmentsArrayList = new ArrayList<>();
    final int n = points.length;
    if (n < 4) {
      return segmentsArrayList;
    }

    Point[] buffer = new Point[n - 1];
    for (int p = 0; p < n; p++) {
      Comparator<Point> slopeOrder = points[p].slopeOrder();

      if (p > 0) {
        System.arraycopy(points, 0, buffer, 0, p);
      }
      if (p < n - 1) {
        System.arraycopy(points, p + 1, buffer, p, n - p - 1);
      }

      sort(buffer, slopeOrder);

      int start = 0;
      int end = 1;
      while (end < n) {
        if (end == n - 1 || slopeOrder.compare(buffer[start], buffer[end]) != 0) {
          int len = end - start;
          if (len >= 3) {
            Point[] segPoints = new Point[len + 1];
            segPoints[0] = points[p];
            System.arraycopy(buffer, start, segPoints, 1, len);
            addUniqueLineSegment(segPoints, segmentsArrayList);
          }
          start = end;
        }
        end++;
      }
    }

    return segmentsArrayList;
  }

  private void addUniqueLineSegment(Point[] segPoints, ArrayList<LineSegment> segmentsArrayList) {
    Point pp = segPoints[0];
    sort(segPoints);
    if (pp == segPoints[0]) {
      LineSegment segment = new LineSegment(segPoints[0], segPoints[segPoints.length - 1]);
      segmentsArrayList.add(segment);
    }
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

  // the number of line segments
  public int numberOfSegments() {
    return segments.length;
  }

  // the line segments
  public LineSegment[] segments() {
    return Arrays.copyOf(segments, segments.length);
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
