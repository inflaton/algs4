package com.inflaton.practices.gempuzzle;

import com.inflaton.datastructures.priorityqueue.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Solver {

  private static class Node {
    Board board;
    Node next, prev;
    private final int moves, manhattan;

    public Node(Board board, int moves) {
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

  private Comparator<Node> nodeComparator =
      new Comparator<Node>() {
        public int compare(Node o1, Node o2) {
          return Integer.compare(o1.getPriority(), o2.getPriority());
        }
      };

  private final Node head;
  private int moves;

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    MinPQ<Node> pq = new MinPQ<>(nodeComparator);
    MinPQ<Node> twinPQ = new MinPQ<>(nodeComparator);

    Node node = new Node(initial, 0);
    pq.insert(node);
    head = node;

    node = new Node(initial.twin(), 0);
    twinPQ.insert(node);

    Node current = null;
    Node twinCurrent = null;
    moves = -1;

    while (!pq.isEmpty() && !twinPQ.isEmpty()) {
      current = moveOneStep(current, pq);
      twinCurrent = moveOneStep(twinCurrent, twinPQ);

      if (current.isGoal()) {
        moves = current.moves;
        while (current != head) {
          current.prev.next = current;
          current = current.prev;
        }
        break;
      }
      if (twinCurrent.isGoal()) {
        break;
      }
    }
  }

  private Node moveOneStep(Node current, MinPQ<Node> pq) {
    current = pq.delMin();
    int moves = current.moves + 1;

    for (Board board : current.board.neighbors()) {
      if (current.prev == null || board != current.prev.board) {
        Node node = new Node(board, moves);
        node.prev = current;
        pq.insert(node);
      }
    }

    return current;
  }

  // is the initial board solvable? (see below)
  public boolean isSolvable() {
    return moves >= 0;
  }

  // min number of moves to solve initial board; -1 if unsolvable
  public int moves() {
    return moves;
  }

  private class BoardIterator implements Iterator<Board> {
    private Node current;

    public BoardIterator() {
      current = head;
    }

    public boolean hasNext() {
      return current != null;
    }

    public Board next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      Board board = current.board;
      current = current.next;
      return board;
    }
  }

  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution() {
    if (!isSolvable()) {
      return null;
    }

    return new Iterable<Board>() {
      public Iterator<Board> iterator() {
        return new BoardIterator();
      }
    };
  }

  // test client (see below)
  public static void main(String[] args) {

    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable()) StdOut.println("No solution possible");
    else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solver.solution()) StdOut.println(board);
    }
  }
}
