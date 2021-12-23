import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
  private final SET<Point2D> points;

  // construct an empty set of points
  public PointSET() {
    points = new SET<>();
  }

  // is the set empty?
  public boolean isEmpty() {
    return points.isEmpty();
  }

  // number of points in the set
  public int size() {
    return points.size();
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }
    points.add(p);
  }

  // does the set contain point p?
  public boolean contains(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }
    return points.contains(p);
  }

  // draw all points to standard draw
  public void draw() {
    for (Point2D p : points) {
      p.draw();
    }
  }

  // all points that are inside the rectangle (or on the boundary)
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) {
      throw new IllegalArgumentException();
    }
    Queue<Point2D> result = new Queue<>();
    for (Point2D point2D : points) {
      if (rect.contains(point2D)) {
        result.enqueue(point2D);
      }
    }
    return result;
  }

  // a nearest neighbor in the set to point p; null if the set is empty
  public Point2D nearest(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }
    Point2D nearest = null;
    double minDistanceSquared = Double.POSITIVE_INFINITY;
    for (Point2D point2D : points) {
      double distanceSquared = p.distanceSquaredTo(point2D);
      if (minDistanceSquared > distanceSquared) {
        minDistanceSquared = distanceSquared;
        nearest = point2D;
      }
    }
    return nearest;
  }

  // unit testing of the methods (optional)
  public static void main(String[] args) {
    String filename = args[0];
    In in = new In(filename);
    PointSET brute = new PointSET();

    while (!in.isEmpty()) {
      double x = in.readDouble();
      double y = in.readDouble();
      Point2D p = new Point2D(x, y);
      brute.insert(p);
    }

    // draw the points
    StdDraw.clear();
    StdDraw.setPenColor(StdDraw.BLACK);
    brute.draw();
    StdDraw.show();
  }
}
