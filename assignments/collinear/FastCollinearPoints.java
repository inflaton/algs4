import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
  private final ArrayList<Point[]> segmentPointsArrayList;

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
    if (n > 1) {
      Arrays.sort(points);
      for (int i = 0; i < n - 1; i++) {
        if (points[i].compareTo(points[i + 1]) == 0) {
          throw new IllegalArgumentException();
        }
      }
    }

    segmentPointsArrayList = new ArrayList<>();
    findAllLineSegments(points);
  }

  private void findAllLineSegments(Point[] points) {
    final int n = points.length;
    if (n < 4) {
      return;
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

      Arrays.sort(buffer, slopeOrder);

      int startIndex = 0;
      int endIndex = 1;
      while (endIndex < n) {
        if (endIndex == n - 1 || slopeOrder.compare(buffer[startIndex], buffer[endIndex]) != 0) {
          int len = endIndex - startIndex;
          if (len >= 3) {
            Point[] segPoints = new Point[len + 1];
            segPoints[0] = points[p];
            System.arraycopy(buffer, startIndex, segPoints, 1, len);
            addUniqueLineSegment(segPoints);
          }
          startIndex = endIndex;
        }
        endIndex++;
      }
    }
  }

  private void addUniqueLineSegment(Point[] segPoints) {
    Arrays.sort(segPoints);
    boolean found = false;
    for (Point[] pa : segmentPointsArrayList) {
      if (pa.length == segPoints.length
          && segPoints[0].compareTo(pa[0]) == 0
          && segPoints[segPoints.length - 1].compareTo(pa[segPoints.length - 1]) == 0) {
        found = true;
        break;
      }
    }

    if (!found) {
      segmentPointsArrayList.add(segPoints);
    }
  }

  // the number of line segments
  public int numberOfSegments() {
    return segmentPointsArrayList.size();
  }

  // the line segments
  public LineSegment[] segments() {
    LineSegment[] segments = new LineSegment[segmentPointsArrayList.size()];
    int i = 0;
    for (Point[] segPoints : segmentPointsArrayList) {
      segments[i] = new LineSegment(segPoints[0], segPoints[segPoints.length - 1]);
      i++;
    }
    return segments;
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