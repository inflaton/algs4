import edu.princeton.cs.algs4.IndexMinPQ;

import java.util.ArrayList;

public class VerticalSeam {
  private final SeamCarver sc;
  private final int numOfVertices;
  private double[] distTo; // distTo[v] = distance  of shortest s->v path
  private int[] edgeTo; // edgeTo[v] = last edge on shortest s->v path
  private IndexMinPQ<Double> pq; // priority queue of vertices

  public VerticalSeam(SeamCarver sc) {
    if (sc == null) {
      throw new IllegalArgumentException();
    }
    this.sc = sc;

    numOfVertices = sc.width() * sc.height() + 2;
    distTo = new double[numOfVertices];
    edgeTo = new int[numOfVertices];

    for (int v = 0; v < numOfVertices; v++) {
      distTo[v] = Double.POSITIVE_INFINITY;
    }

    int s = 0;
    distTo[s] = 0.0;

    // relax vertices in order of distance from s
    pq = new IndexMinPQ<>(numOfVertices);
    pq.insert(s, distTo[s]);

    while (!pq.isEmpty()) {
      int v = pq.delMin();
      ArrayList<Integer> adjVertices = adjVertices(v);
      for (int w : adjVertices) {
        relax(v, w);
      }
    }
  }

  private void relax(int v, int w) {
    double weight = weight(w);
    if (distTo[w] > distTo[v] + weight) {
      distTo[w] = distTo[v] + weight;
      edgeTo[w] = v;
      if (pq.contains(w)) {
        pq.decreaseKey(w, distTo[w]);
      } else {
        pq.insert(w, distTo[w]);
      }
    }
  }

  public int[] seam() {
    int[] seam = new int[sc.height()];
    int v = numOfVertices - 1;
    while ((v = edgeTo[v]) > 0) {
      int x = (v - 1) % sc.width();
      int y = (v - 1) / sc.width();
      seam[y] = x;
    }
    return seam;
  }

  private double weight(int v) {
    if (v == numOfVertices - 1) {
      return 0;
    }
    int x = (v - 1) % sc.width();
    int y = (v - 1) / sc.width();
    return sc.energy(x, y);
  }

  private ArrayList<Integer> adjVertices(int v) {
    ArrayList<Integer> arrayList = new ArrayList<>();
    if (v < numOfVertices - 1) {
      if (v == 0) { // virtual start vertex
        for (int i = 0; i < sc.width(); i++) {
          arrayList.add(i + 1);
        }
      } else {
        int x = (v - 1) % sc.width();
        int y = (v - 1) / sc.width();

        if (y == sc.height() - 1) { // last row
          arrayList.add(numOfVertices - 1);
        } else {
          if (x > 0) {
            arrayList.add(vertexOf(x - 1, y + 1));
          }
          arrayList.add(vertexOf(x, y + 1));
          if (x < sc.width() - 1) {
            arrayList.add(vertexOf(x + 1, y + 1));
          }
        }
      }
    }

    return arrayList;
  }

  private int vertexOf(int x, int y) {
    return y * sc.width() + x + 1;
  }
}
