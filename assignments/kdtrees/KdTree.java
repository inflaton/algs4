import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
  private Node root; // root of BST
  private static final RectHV rootRect = new RectHV(0, 0, 1, 1);

  private static class Node {
    private final Point2D point; // sorted by key
    private Node left; // left and right subtrees
    private Node right; // left and right subtrees
    private int size; // number of nodes in subtree
    private final boolean verticalSplit;

    public Node(Point2D point, boolean verticalSplit, int size) {
      this.point = point;
      this.verticalSplit = verticalSplit;
      this.size = size;
    }
  }

  // construct an empty set of points
  public KdTree() {}

  // is the set empty?
  public boolean isEmpty() {
    return root == null;
  }

  // number of points in the set
  public int size() {
    return size(root);
  }

  // return number of key-value pairs in BST rooted at x
  private int size(Node x) {
    if (x == null) {
      return 0;
    } else {
      return x.size;
    }
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }
    root = insert(root, p, true);
  }

  private Node insert(Node node, Point2D p, boolean verticalSplit) {
    if (node == null) {
      return new Node(p, verticalSplit, 1);
    }
    int cmp = compare(p, node.point, node.verticalSplit);
    if (cmp < 0) {
      node.left = insert(node.left, p, !verticalSplit);
    } else {
      node.right = insert(node.right, p, !verticalSplit);
    }
    node.size = 1 + size(node.left) + size(node.right);
    return node;
  }

  private int compare(Point2D p, Point2D q, boolean verticalSplit) {
    if (verticalSplit) {
      return Double.compare(p.x(), q.x());
    }
    return Double.compare(p.y(), q.y());
  }

  // does the set contain point p?
  public boolean contains(Point2D p) {
    return contains(root, p);
  }

  private boolean contains(Node node, Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException();
    }
    if (node == null) {
      return false;
    }
    if (node.point.equals(p)) {
      return true;
    }
    int cmp = compare(p, node.point, node.verticalSplit);
    if (cmp < 0) {
      return contains(node.left, p);
    }
    return contains(node.right, p);
  }

  // draw all points to standard draw
  public void draw() {
    draw(root);
  }

  private void draw(Node node) {
    if (node == null) {
      return;
    }
    Point2D p = node.point;
    if (node.verticalSplit) {
      StdDraw.setPenColor(StdDraw.RED);
      StdDraw.line(p.x(), 0, p.x(), 1);
    } else {
      StdDraw.setPenColor(StdDraw.BLUE);
      StdDraw.line(0, p.y(), 1, p.y());
    }
    StdDraw.setPenColor(StdDraw.BLACK);
    p.draw();

    draw(node.left);
    draw(node.right);
  }

  // all points that are inside the rectangle (or on the boundary)
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) {
      throw new IllegalArgumentException();
    }
    Queue<Point2D> result = new Queue<>();
    range(root, rootRect, rect, result);
    return result;
  }

  private void range(Node node, RectHV nodeRect, RectHV searchRect, Queue<Point2D> result) {
    if (node == null) {
      return;
    }
    Point2D p = node.point;
    if (searchRect.contains(p)) {
      result.enqueue(p);
    }

    Node[] childNodes = {node.left, node.right};
    RectHV[] childRects = getChildRects(node, nodeRect);
    for (int i = 0; i < childNodes.length; i++) {
      if (searchRect.intersects(childRects[i])) {
        range(childNodes[i], childRects[i], searchRect, result);
      }
    }
  }

  private RectHV[] getChildRects(Node node, RectHV nodeRect) {
    Point2D p = node.point;
    RectHV leftRect =
        node.verticalSplit
            ? new RectHV(nodeRect.xmin(), nodeRect.ymin(), p.x(), nodeRect.ymax())
            : new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), p.y());
    RectHV rightRect =
        node.verticalSplit
            ? new RectHV(p.x(), nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax())
            : new RectHV(nodeRect.xmin(), p.y(), nodeRect.xmax(), nodeRect.ymax());
    return new RectHV[] {leftRect, rightRect};
  }

  // a nearest neighbor in the set to point p; null if the set is empty
  public Point2D nearest(Point2D p) {
    return nearest(root, rootRect, p, new Point2D(-1, -1));
  }

  private Point2D nearest(Node node, RectHV nodeRect, Point2D searchPoint, Point2D closetPoint) {
    if (node == null) {
      return closetPoint;
    }

    double shortestDistanceSquared = searchPoint.distanceSquaredTo(closetPoint);
    if (shortestDistanceSquared < distanceSquaredToRect(searchPoint, nodeRect)) {
      return closetPoint;
    }

    Point2D p = node.point;
    double distanceSquared = searchPoint.distanceSquaredTo(p);
    if (shortestDistanceSquared > distanceSquared) {
      closetPoint = p;
    }

    Node[] childNodes = {node.left, node.right};
    RectHV[] childRects = getChildRects(node, nodeRect);
    double min = Double.POSITIVE_INFINITY;
    int index = 0;
    for (int i = 0; i < childNodes.length; i++) {
      double childDistanceSquared = distanceSquaredToRect(searchPoint, childRects[i]);
      if (min > childDistanceSquared) {
        index = i;
        min = childDistanceSquared;
      }
    }

    for (int i = 0; i < 2; i++) {
      closetPoint = nearest(childNodes[index], childRects[index], searchPoint, closetPoint);
      index = 1 - index;
    }

    return closetPoint;
  }

  private double distanceSquaredToRect(Point2D p, RectHV r) {
    if (r.contains(p)) {
      return 0;
    }

    double dxSquared =
        Math.min((p.x() - r.xmin()) * (p.x() - r.xmin()), (p.x() - r.xmax()) * (p.x() - r.xmax()));
    if (p.y() > r.ymin() && p.y() < r.ymax()) {
      return dxSquared;
    }

    double dySquared =
        Math.min((p.y() - r.ymin()) * (p.y() - r.ymin()), (p.y() - r.ymax()) * (p.y() - r.ymax()));
    if (p.x() > r.xmin() && p.x() < r.xmax()) {
      return dySquared;
    }

    return dxSquared + dySquared;
  }

  // unit testing of the methods (optional)
  public static void main(String[] args) {
    String filename = args[0];
    In in = new In(filename);
    KdTree kdtree = new KdTree();
    PointSET brute = new PointSET();

    while (!in.isEmpty()) {
      double x = in.readDouble();
      double y = in.readDouble();
      Point2D p = new Point2D(x, y);
      kdtree.insert(p);
      brute.insert(p);
    }
    if (args.length > 1) {
      processRangeSearchQueries(kdtree, brute);
    } else {
      processNearestNeighborQueries(kdtree, brute);
    }
  }

  private static void processRangeSearchQueries(KdTree kdtree, PointSET brute) {
    double x0 = 0.0;
    double y0 = 0.0; // initial endpoint of rectangle
    double x1 = 0.0;
    double y1 = 0.0; // current location of mouse
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
        x1 = StdDraw.mouseX();
        y1 = StdDraw.mouseY();
        x0 = x1;
        y0 = y1;
        isDragging = true;
      } else if (StdDraw.isMousePressed()) {
        x1 = StdDraw.mouseX();
        y1 = StdDraw.mouseY();
      } else if (isDragging) {
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

      // draw the range search results for kdtree-force data structure in red
      StdDraw.setPenRadius(0.03);
      StdDraw.setPenColor(StdDraw.RED);
      for (Point2D p : kdtree.range(rect)) {
        p.draw();
      }

      StdDraw.show();
      StdDraw.pause(20);
    }
  }

  private static void processNearestNeighborQueries(KdTree kdtree, PointSET brute) {
    // process nearest neighbor queries
    StdDraw.enableDoubleBuffering();
    while (true) {

      // draw all of the points
      StdDraw.clear();
      StdDraw.setPenColor(StdDraw.BLACK);
      StdDraw.setPenRadius(0.01);
      brute.draw();

      StdDraw.setPenRadius(0.03);
      StdDraw.setPenColor(StdDraw.RED);

      // the location (x, y) of the mouse
      double x = StdDraw.mouseX();
      double y = StdDraw.mouseY();
      Point2D query = new Point2D(x, y);
      kdtree.nearest(query).draw();

      StdDraw.show();
      StdDraw.pause(40);
    }
  }
}
