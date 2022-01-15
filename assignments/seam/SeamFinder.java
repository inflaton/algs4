import edu.princeton.cs.algs4.IndexMinPQ;

import java.util.ArrayList;

public class SeamFinder {
  private final ISeamPicture picture;
  private final int numOfVertices;
  private final double[] energy;
  private final double[] distTo; // distTo[v] = distance  of shortest s->v path
  private final int[] edgeTo; // edgeTo[v] = last edge on shortest s->v path
  private final IndexMinPQ<Double> pq; // priority queue of vertices
  private final ISeamHelper helper;

  public SeamFinder(ISeamPicture seamPicture, ISeamHelper seamHelper) {
    if (seamPicture == null) {
      throw new IllegalArgumentException();
    }
    this.picture = seamPicture;
    this.helper = seamHelper;

    numOfVertices = seamPicture.width() * seamPicture.height() + 2;
    energy = new double[numOfVertices];
    distTo = new double[numOfVertices];
    edgeTo = new int[numOfVertices];

    for (int v = 0; v < numOfVertices; v++) {
      distTo[v] = Double.POSITIVE_INFINITY;
      energy[v] = calcEnergy(v);
    }

    int s = 0;
    distTo[s] = 0.0;

    // relax vertices in order of distance from s
    pq = new IndexMinPQ<>(numOfVertices);
    pq.insert(s, distTo[s]);

    while (!pq.isEmpty()) {
      int v = pq.delMin();
      ArrayList<Integer> adjVertices = seamHelper.adjVertices(v, seamPicture);
      for (int w : adjVertices) {
        relax(v, w);
      }
    }
  }

  private void relax(int v, int w) {
    double weight = energy[w];
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
    int[] seam = new int[helper.seamLength(picture)];
    int v = edgeTo[numOfVertices - 1];
    while (v > 0) {
      int x = (v - 1) % picture.width();
      int y = (v - 1) / picture.width();
      helper.setSeamValue(seam, x, y);
      v = edgeTo[v];
    }
    return seam;
  }

  private double calcEnergy(int v) {
    if (v == 0 || v == numOfVertices - 1) {
      return 0;
    }
    int x = (v - 1) % picture.width();
    int y = (v - 1) / picture.width();
    return picture.energy(x, y);
  }
}
