import edu.princeton.cs.algs4.*;

public class PointSET {
  private SET<Point2D> point2DSET;

  // construct an empty set of points
  public PointSET() {
    point2DSET = new SET<>();
  }

  // is the set empty?
  public boolean isEmpty() {
    return point2DSET.isEmpty();
  }

  // number of points in the set
  public int size() {
    return point2DSET.size();
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }
    point2DSET.add(p);
  }

  // does the set contain point p?
  public boolean contains(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }
    return point2DSET.contains(p);
  }

  // draw all points to standard draw
  public void draw() {
    for (Point2D p : point2DSET) {
      p.draw();
    }
  }

  // all points that are inside the rectangle (or on the boundary)
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) {
      throw new IllegalArgumentException();
    }
    Queue<Point2D> result = new Queue<>();
    for (Point2D point2D : point2DSET) {
      if (rect.contains(point2D)) {
        result.enqueue(point2D);
      }
    }
    return result;
  }

  // a nearest neighbor in the set to point p; null if the set is empty
  public Point2D nearest(Point2D p) {
    Point2D pointFound = null;
    double minDistanceSquared = Double.POSITIVE_INFINITY;
    for (Point2D point2D : point2DSET) {
      double distanceSquared = p.distanceSquaredTo(point2D);
      if (minDistanceSquared > distanceSquared) {
        minDistanceSquared = distanceSquared;
        pointFound = point2D;
      }
    }
    return pointFound;
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
    if (args.length > 1) {
      processRangeSearchQueries(brute);
    } else {
      processNearestNeighorQueries(brute);
    }
  }

  private static void processRangeSearchQueries(PointSET brute) {
    double x0 = 0.0, y0 = 0.0; // initial endpoint of rectangle
    double x1 = 0.0, y1 = 0.0; // current location of mouse
    boolean isDragging = false; // is the user dragging a rectangle

    // draw the points
    StdDraw.clear();
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(0.01);
    brute.draw();
    StdDraw.show();

    // process range search queries
    StdDraw.enableDoubleBuffering();

    while (true) {

      // user starts to drag a rectangle
      if (StdDraw.isMousePressed() && !isDragging) {
        x0 = x1 = StdDraw.mouseX();
        y0 = y1 = StdDraw.mouseY();
        isDragging = true;
      }

      // user is dragging a rectangle
      else if (StdDraw.isMousePressed() && isDragging) {
        x1 = StdDraw.mouseX();
        y1 = StdDraw.mouseY();
      }

      // user stops dragging rectangle
      else if (!StdDraw.isMousePressed() && isDragging) {
        isDragging = false;
      }

      // draw the points
      StdDraw.clear();
      StdDraw.setPenColor(StdDraw.BLACK);
      StdDraw.setPenRadius(0.01);
      brute.draw();

      // draw the rectangle
      RectHV rect =
          new RectHV(Math.min(x0, x1), Math.min(y0, y1), Math.max(x0, x1), Math.max(y0, y1));
      StdDraw.setPenColor(StdDraw.BLACK);
      StdDraw.setPenRadius();
      rect.draw();

      // draw the range search results for brute-force data structure in red
      StdDraw.setPenRadius(0.03);
      StdDraw.setPenColor(StdDraw.RED);
      for (Point2D p : brute.range(rect)) p.draw();

      StdDraw.show();
      StdDraw.pause(20);
    }
  }

  private static void processNearestNeighorQueries(PointSET brute) {
    // process nearest neighbor queries
    StdDraw.enableDoubleBuffering();
    while (true) {

      // the location (x, y) of the mouse
      double x = StdDraw.mouseX();
      double y = StdDraw.mouseY();
      Point2D query = new Point2D(x, y);

      // draw all of the points
      StdDraw.clear();
      StdDraw.setPenColor(StdDraw.BLACK);
      StdDraw.setPenRadius(0.01);
      brute.draw();

      // draw in red the nearest neighbor (using brute-force algorithm)
      StdDraw.setPenRadius(0.03);
      StdDraw.setPenColor(StdDraw.RED);
      brute.nearest(query).draw();

      StdDraw.show();
      StdDraw.pause(40);
    }
  }
}
