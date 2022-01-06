package com.inflaton.datastructures.graph.edgeweightedgraph;

import com.inflaton.datastructures.unionfind.UF;
import edu.princeton.cs.algs4.Queue;

public abstract class AbstractMST {

  private static final double FLOATING_POINT_EPSILON = 1E-12;

  protected final Queue<Edge> mst = new Queue<Edge>(); // edges in MST
  protected double weight; // weight of MST

  public Iterable<Edge> edges() {
    return mst;
  }

  public double weight() {
    return weight;
  }

  // check optimality conditions (takes time proportional to E V lg* V)
  protected boolean check(EdgeWeightedGraph G) {

    // check total weight
    double total = 0.0;
    for (Edge e : edges()) {
      total += e.weight();
    }
    if (Math.abs(total - weight()) > FLOATING_POINT_EPSILON) {
      System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", total, weight());
      return false;
    }

    // check that it is acyclic
    UF uf = new UF(G.V());
    for (Edge e : edges()) {
      int v = e.either(), w = e.other(v);
      if (uf.find(v) == uf.find(w)) {
        System.err.println("Not a forest");
        return false;
      }
      uf.union(v, w);
    }

    // check that it is a spanning forest
    for (Edge e : G.edges()) {
      int v = e.either(), w = e.other(v);
      if (uf.find(v) != uf.find(w)) {
        System.err.println("Not a spanning forest");
        return false;
      }
    }

    // check that it is a minimal spanning forest (cut optimality conditions)
    for (Edge e : edges()) {

      // all edges in MST except e
      uf = new UF(G.V());
      for (Edge f : mst) {
        int x = f.either(), y = f.other(x);
        if (f != e) uf.union(x, y);
      }

      // check that e is min weight edge in crossing cut
      for (Edge f : G.edges()) {
        int x = f.either(), y = f.other(x);
        if (uf.find(x) != uf.find(y)) {
          if (f.weight() < e.weight()) {
            System.err.println("Edge " + f + " violates cut optimality conditions");
            return false;
          }
        }
      }
    }

    return true;
  }
}
