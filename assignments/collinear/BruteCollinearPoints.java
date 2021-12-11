import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {
  private final ArrayList<LineSegment> segmentArrayList;

  // finds all line segments containing 4 points
  public BruteCollinearPoints(Point[] points) {
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

    segmentArrayList = new ArrayList<>();
    findAllLineSegments(points);
  }

  private void findAllLineSegments(Point[] points) {
    final int n = points.length;
    if (n < 4) {
      return;
    }

    for (int p = 0; p < n; p++) {
      Comparator<Point> slopeOrder = points[p].slopeOrder();
      for (int q = p + 1; q < n; q++) {
        for (int r = q + 1; r < n; r++) {
          for (int s = r + 1; s < n; s++) {
            // check whether the three slopes between p and q, between p and r, and
            // between p and s are all equal.
            if (slopeOrder.compare(points[q], points[r]) == 0
                && slopeOrder.compare(points[r], points[s]) == 0) {
              Point[] segPoints = {points[p], points[q], points[r], points[s]};
              addUniqueLineSegment(segPoints);
            }
          }
        }
      }
    }
  }

  private void addUniqueLineSegment(Point[] segPoints) {
    Arrays.sort(segPoints);
    LineSegment segment = new LineSegment(segPoints[0], segPoints[segPoints.length - 1]);
    String str = segment.toString();
    boolean found = false;
    for (LineSegment ls : segmentArrayList) {
      if (ls.toString().equals(str)) {
        found = true;
        break;
      }
    }
    if (!found) {
      segmentArrayList.add(segment);
    }
  }

  // the number of line segments
  public int numberOfSegments() {
    return segmentArrayList.size();
  }

  // the line segments
  public LineSegment[] segments() {
    LineSegment[] segments = new LineSegment[segmentArrayList.size()];
    segmentArrayList.toArray(segments);
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
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
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
