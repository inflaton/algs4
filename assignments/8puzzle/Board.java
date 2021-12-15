import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Board {

  private final int n;
  private final int[] tiles;
  private ArrayList<Board> neighbors;
  private Board twin;

  // create a board from an n-by-n array of tiles,
  // where tiles[row][col] = tile at (row, col)
  public Board(int[][] tiles) {
    if (tiles == null
        || tiles.length < 2
        || tiles.length >= 128
        || tiles[0].length != tiles.length) {
      throw new IllegalArgumentException();
    }

    this.n = tiles.length;
    this.tiles = new int[n * n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (tiles[i][j] < 0 || tiles[i][j] > n * n - 1) {
          throw new IllegalArgumentException();
        }
        this.tiles[i * n + j] = tiles[i][j];
      }
    }
  }

  // used in createSwap only
  private Board(Board copyFrom) {
    this.tiles = Arrays.copyOf(copyFrom.tiles, copyFrom.tiles.length);
    this.n = copyFrom.n;
  }

  private int get(int i, int j) {
    return tiles[i * n + j];
  }

  // string representation of this board
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(n).append("\n");
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        int tile = get(i, j);
        if (tile < 10) {
          sb.append(" ");
        }
        sb.append(tile).append(" ");
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  // board dimension n
  public int dimension() {
    return n;
  }

  // number of tiles out of place
  public int hamming() {
    int hamming = 0;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        int tile = get(i, j);
        if (tile != 0 && tile != i * n + j + 1) {
          hamming++;
        }
      }
    }
    return hamming;
  }

  // sum of Manhattan distances between tiles and goal
  public int manhattan() {
    int manhattan = 0;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        int v = get(i, j);
        if (v == 0) continue;
        int row = (v - 1) / n;
        int col = (v - 1) % n;

        int delta = row - i;
        if (delta < 0) {
          delta = -delta;
        }
        manhattan += delta;

        delta = col - j;
        if (delta < 0) {
          delta = -delta;
        }
        manhattan += delta;
      }
    }
    return manhattan;
  }

  // is this board the goal board?
  public boolean isGoal() {
    int i;
    for (i = 0; i < tiles.length - 1; i++) {
      if (tiles[i] != i + 1) {
        return false;
      }
    }
    return tiles[i] == 0;
  }

  // does this board equal y?
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Board board = (Board) obj;
    return n == board.n && Arrays.equals(tiles, board.tiles);
  }

  private Board createSwap(int row, int col, int r, int c) {
    Board b = new Board(this);
    b.tiles[row * n + col] = get(r, c);
    b.tiles[r * n + c] = get(row, col);
    return b;
  }

  private void addNeighbor(int row, int col, int r, int c) {
    neighbors.add(createSwap(row, col, r, c));
  }

  private ArrayList<Board> getNeighbors() {
    if (neighbors == null) {
      neighbors = new ArrayList<>();

      int row = -1;
      int col = -1;
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          int v = get(i, j);
          if (v == 0) {
            row = i;
            col = j;
            break;
          }
        }
      }

      if (row > 0) {
        addNeighbor(row, col, row - 1, col);
      }
      if (row < n - 1) {
        addNeighbor(row, col, row + 1, col);
      }
      if (col > 0) {
        addNeighbor(row, col, row, col - 1);
      }
      if (col < n - 1) {
        addNeighbor(row, col, row, col + 1);
      }
    }

    return neighbors;
  }

  // all neighboring boards
  public Iterable<Board> neighbors() {
    return getNeighbors();
  }

  // a board that is obtained by exchanging any pair of tiles
  public Board twin() {
    if (twin == null) twin = getTwin();
    return twin;
  }

  private Board getTwin() {
    int index = StdRandom.uniform(n * n);
    if (tiles[index] == 0) {
      if (index == 0) index++;
      else index--;
    }
    int row = index / n;
    int col = index % n;

    if (row > 0 && get(row - 1, col) != 0) {
      return createSwap(row, col, row - 1, col);
    }
    if (row < n - 1 && get(row + 1, col) != 0) {
      return createSwap(row, col, row + 1, col);
    }
    if (col > 0 && get(row, col - 1) != 0) {
      return createSwap(row, col, row, col - 1);
    }
    return createSwap(row, col, row, col + 1);
  }

  // unit testing (not graded)
  public static void main(String[] args) { // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) tiles[i][j] = in.readInt();
    Board board = new Board(tiles);
    StdOut.println(board);
    assert board.equals(new Board(tiles));

    StdOut.println("dimension: " + board.dimension());
    StdOut.println("hamming: " + board.hamming());
    StdOut.println("manhattan: " + board.manhattan());
    StdOut.println("isGoal: " + board.isGoal());

    StdOut.println("neighbors: ");
    Iterator<Board> it = board.neighbors().iterator();
    while (it.hasNext()) {
      Board neighbor = it.next();
      StdOut.println("manhattan: " + neighbor.manhattan());
      StdOut.println("hamming: " + neighbor.hamming());
      StdOut.println(neighbor);
      assert !neighbor.equals(board);
    }

    Board twin = board.twin();
    StdOut.println("twin 1: ");
    StdOut.println("manhattan: " + twin.manhattan());
    StdOut.println("hamming: " + twin.hamming());
    StdOut.println(twin);

    twin = board.twin();
    StdOut.println("twin 2: ");
    StdOut.println("manhattan: " + twin.manhattan());
    StdOut.println("hamming: " + twin.hamming());
    StdOut.println(twin);
  }
}
