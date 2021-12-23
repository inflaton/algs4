import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
  private static final RectHV ROOT_RECT = new RectHV(0, 0, 1, 1);
  private Node root; // root of BST

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
    if (!node.point.equals(p)) {
      int cmp = compare(p, node.point, node.verticalSplit);
      if (cmp < 0) {
        node.left = insert(node.left, p, !verticalSplit);
      } else {
        node.right = insert(node.right, p, !verticalSplit);
      }
      node.size = 1 + size(node.left) + size(node.right);
    }
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
    range(root, ROOT_RECT, rect, result);
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
    if (p == null) {
      throw new IllegalArgumentException();
    }
    return nearest(root, ROOT_RECT, p, null);
  }

  private Point2D nearest(Node node, RectHV nodeRect, Point2D searchPoint, Point2D closetPoint) {
    if (node == null) {
      return closetPoint;
    }

    Point2D p = node.point;
    if (closetPoint == null) {
      closetPoint = p;
    } else {
      double shortestDistanceSquared = searchPoint.distanceSquaredTo(closetPoint);
      if (shortestDistanceSquared < distanceSquaredToRect(searchPoint, nodeRect)) {
        return closetPoint;
      }
      if (shortestDistanceSquared > searchPoint.distanceSquaredTo(p)) {
        closetPoint = p;
      }
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

    // draw the points
    StdDraw.clear();
    kdtree.draw();
    StdDraw.setPenRadius();
    brute.draw();
    StdDraw.show();

    Point2D p = new Point2D(0.249, 0.452);
    StdOut.println("nearest: " + kdtree.nearest(p));
  }
}
