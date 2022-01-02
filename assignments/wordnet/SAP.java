import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SAP {

  private Digraph digraph;

  // constructor takes a digraph (not necessarily a DAG)
  public SAP(Digraph digraph) {
    this.digraph = digraph;
  }

  // length of shortest ancestral path between v and w; -1 if no such path
  public int length(int v, int w) {
    int[] result = findShortestAncestralPath(v, w);
    return result == null ? -1 : result[0];
  }

  // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such
  // path
  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    int[] result = findShortestAncestralPath(v, w);
    return result == null ? -1 : result[0];
  }

  private int[] findShortestAncestralPath(int v, int w) {
    validateVertex(v);
    validateVertex(w);
    Integer[] array1 = {v};
    Integer[] array2 = {w};
    return findShortestAncestralPath(Arrays.asList(array1), Arrays.asList(array2));
  }

  private int[] findShortestAncestralPath(Iterable<Integer> v, Iterable<Integer> w) {
    if (v == null || w == null) {
      throw new IllegalArgumentException();
    }

    int numOfVertices = digraph.V();

    boolean[] marked = new boolean[numOfVertices];
    int[] distTo = new int[numOfVertices];
    int[] edgeTo = new int[numOfVertices];
    bfs(v, marked, distTo, edgeTo);

    boolean[] marked2 = new boolean[numOfVertices];
    int[] distTo2 = new int[numOfVertices];
    int[] edgeTo2 = new int[numOfVertices];
    bfs(w, marked2, distTo2, edgeTo2);

    int[] result = null;
    int len = Integer.MAX_VALUE;
    for (int i = 0; i < numOfVertices; i++) {
      if (marked2[i] && marked[i]) {
        int dist = distTo2[i] + distTo[i];
        if (dist < len) {
          len = dist;
          if (result == null) {
            result = new int[] {len, i};
          } else {
            result[0] = len;
            result[1] = i;
          }
        }
      }
    }

    return result;
  }

  // throw an IllegalArgumentException unless {@code 0 <= v < V}
  private void validateVertex(int v) {
    if (v < 0 || v >= digraph.V()) {
      throw new IllegalArgumentException(
          "vertex " + v + " is not between 0 and " + (digraph.V() - 1));
    }
  }

  private void bfs(int s, boolean[] marked, int[] distTo, int[] edgeTo) {
    validateVertex(s);
    Queue<Integer> q = new Queue<Integer>();
    q.enqueue(s);
    bfs(q, marked, distTo, edgeTo);
  }

  // BFS from multiple sources
  private void bfs(Iterable<Integer> sources, boolean[] marked, int[] distTo, int[] edgeTo) {
    Queue<Integer> q = new Queue<Integer>();
    for (int s : sources) {
      validateVertex(s);

      marked[s] = true;
      distTo[s] = 0;
      q.enqueue(s);
    }
    bfs(q, marked, distTo, edgeTo);
  }

  private void bfs(Queue<Integer> q, boolean[] marked, int[] distTo, int[] edgeTo) {
    while (!q.isEmpty()) {
      int v = q.dequeue();
      for (int w : digraph.adj(v)) {
        if (!marked[w]) {
          edgeTo[w] = v;
          distTo[w] = distTo[v] + 1;
          marked[w] = true;
          q.enqueue(w);
        }
      }
    }
  }

  // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
  public int ancestor(int v, int w) {
    int[] result = findShortestAncestralPath(v, w);
    return result == null ? -1 : result[1];
  }

  // a common ancestor that participates in shortest ancestral path; -1 if no such path
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    int[] result = findShortestAncestralPath(v, w);
    return result == null ? -1 : result[1];
  }

  // do unit testing of this class
  public static void main(String[] args) {
    In in = new In(args[0]);
    Digraph digraph = new Digraph(in);
    SAP sap = new SAP(digraph);
    while (!StdIn.isEmpty()) {
      int v = StdIn.readInt();
      int w = StdIn.readInt();
      int length = sap.length(v, w);
      int ancestor = sap.ancestor(v, w);
      StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
  }
}
