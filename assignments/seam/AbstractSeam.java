import edu.princeton.cs.algs4.IndexMinPQ;

import java.util.ArrayList;

public abstract class AbstractSeam {
  private final ISeamPicture seamPicture;
  private final int numOfVertices;
  private final double[] distTo; // distTo[v] = distance  of shortest s->v path
  private final int[] edgeTo; // edgeTo[v] = last edge on shortest s->v path
  private final IndexMinPQ<Double> pq; // priority queue of vertices

  public AbstractSeam(ISeamPicture seamPicture) {
    if (seamPicture == null) {
      throw new IllegalArgumentException();
    }
    this.seamPicture = seamPicture;

    numOfVertices = seamPicture.width() * seamPicture.height() + 2;
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
      ArrayList<Integer> adjVertices = adjVertices(v, seamPicture);
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
    int[] seam = new int[seamLength(seamPicture)];
    int v = numOfVertices - 1;
    while ((v = edgeTo[v]) > 0) {
      int x = (v - 1) % seamPicture.width();
      int y = (v - 1) / seamPicture.width();
      setSeamValue(seam, x, y);
    }
    return seam;
  }

  protected abstract int seamLength(ISeamPicture seamPicture);

  protected abstract ArrayList<Integer> adjVertices(int v, ISeamPicture seamPicture);

  protected abstract void setSeamValue(int[] seam, int x, int y);

  private double weight(int v) {
    if (v == numOfVertices - 1) {
      return 0;
    }
    int x = (v - 1) % seamPicture.width();
    int y = (v - 1) / seamPicture.width();
    return seamPicture.energy(x, y);
  }

  protected int vertexOf(int x, int y) {
    return y * seamPicture.width() + x + 1;
  }
}
