package com.inflaton.practices.gempuzzle;

import com.inflaton.datastructures.priorityqueue.MinPQ;
import com.inflaton.datastructures.collection.stack.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Comparator;

public class Solver {

  private static class SearchNode {
    Board board;
    SearchNode parent;
    private final int moves, manhattan;

    public SearchNode(Board board, int moves) {
      this.board = board;
      this.moves = moves;
      manhattan = board.manhattan();
    }

    public int getPriority() {
      return manhattan + moves;
    }

    public boolean isGoal() {
      return manhattan == 0;
    }
  }

  private final Stack<Board> boardStack = new Stack<>();

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    if (initial == null) throw new IllegalArgumentException();
    final Comparator<SearchNode> nodeComparator =
        Comparator.comparingInt(SearchNode::getPriority).thenComparingInt(o -> o.manhattan);
    MinPQ<SearchNode> pq = new MinPQ<>(nodeComparator);
    MinPQ<SearchNode> twinPQ = new MinPQ<>(nodeComparator);

    SearchNode searchNode = new SearchNode(initial, 0);
    pq.insert(searchNode);

    searchNode = new SearchNode(initial.twin(), 0);
    twinPQ.insert(searchNode);

    SearchNode current = null;
    SearchNode twinCurrent = null;

    while (!pq.isEmpty() && !twinPQ.isEmpty()) {
      current = runSearch(pq, false);
      twinCurrent = runSearch(twinPQ, true);

      if (current.isGoal()) {
        do {
          boardStack.push(current.board);
          current = current.parent;
        } while (current != null);
        break;
      }
      if (twinCurrent.isGoal()) {
        break;
      }
    }
  }

  private SearchNode runSearch(MinPQ<SearchNode> pq, boolean isTwin) {
    SearchNode current = pq.delMin();
    int newMoves = current.moves + 1;

    for (Board board : current.board.neighbors()) {
      if (current.parent == null || !board.equals(current.parent.board)) {
        SearchNode searchNode = new SearchNode(board, newMoves);
        searchNode.parent = current;
        pq.insert(searchNode);
      }
    }
    // no need to maintain backtrace for twin board
    if (isTwin) current.parent = null;
    return current;
  }

  // is the initial board solvable? (see below)
  public boolean isSolvable() {
    return !boardStack.isEmpty();
  }

  // min number of moves to solve initial board; -1 if unsolvable
  public int moves() {
    return boardStack.size() - 1;
  }

  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution() {
    if (!isSolvable()) {
      return null;
    }

    return boardStack;
  }

  // test client (see below)
  public static void main(String[] args) {

    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);

    Stopwatch stopwatch = new Stopwatch();
    // solve the puzzle
    Solver solver = new Solver(initial);
    double elapsedTime = stopwatch.elapsedTime();

    // print solution to standard output
    if (!solver.isSolvable()) StdOut.println("No solution possible");
    else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      int i = 0;
      for (Board board : solver.solution()) {
        if (args.length > 1) StdOut.print("Step: " + i + " ==> ");
        StdOut.println(board);
        i++;
      }
    }

    StdOut.println("elapsedTime: " + elapsedTime);
  }
}
