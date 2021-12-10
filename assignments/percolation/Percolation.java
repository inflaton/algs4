import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private final WeightedQuickUnionUF uf;
  private final int n;
  private final boolean[] sites;
  private int openSites;

  // creates n-by-n grid, with all sites initially blocked
  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException();
    }
    this.n = n;
    int size = n * n + 1;
    sites = new boolean[size];
    uf = new WeightedQuickUnionUF(size);
  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {
    if (isOpen(row, col)) {
      return;
    }

    int index = getIndex(row, col);
    sites[index] = true;
    openSites++;

    if (col > 1 && isOpen(row, col - 1)) {
      uf.union(index, index - 1);
    }

    if (col < n && isOpen(row, col + 1)) {
      uf.union(index, index + 1);
    }

    if (row == 1) {
      uf.union(index, 0);
    } else if (isOpen(row - 1, col)) {
      uf.union(index, index - n);
    }

    if (row < n && isOpen(row + 1, col)) {
      uf.union(index, index + n);
    }
  }

  private int getIndex(int row, int col) {
    if (row < 1 || row > n || col < 1 || col > n) {
      throw new IllegalArgumentException();
    }
    return (row - 1) * n + col;
  }

  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    return sites[getIndex(row, col)];
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    final int index = getIndex(row, col);
    return sites[index] && uf.find(0) == uf.find(index);
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return openSites;
  }

  // does the system percolate?
  public boolean percolates() {
    for (int col = 1; col <= n; col++) {
      if (isFull(n, col)) {
        return true;
      }
    }

    return false;
  }

  // test client (optional)
  public static void main(String[] args) {
    int n = StdIn.readInt();
    Percolation percolation = new Percolation(n);

    Stopwatch stopwatch = new Stopwatch();

    int i = 0;
    while (!StdIn.isEmpty()) {
      int row = StdIn.readInt();
      int col = StdIn.readInt();
      percolation.open(row, col);
      i++;
    }

    double elapsedTime = stopwatch.elapsedTime();

    for (int row = 1; row <= n; row++) {
      for (int col = 1; col <= n; col++) {
        int v = 0;
        if (percolation.isOpen(row, col)) {
          v |= 1;
        }
        if (percolation.isFull(row, col)) {
          v |= 2;
        }
        StdOut.print(v);
      }
      StdOut.println();
    }

    StdOut.println("i: " + i);
    StdOut.println("numberOfOpenSites: " + percolation.numberOfOpenSites());
    StdOut.println("percolates: " + percolation.percolates());
    StdOut.println("elapsed time: " + elapsedTime);
  }
}
