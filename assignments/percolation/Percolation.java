import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private WeightedQuickUnionUF uf;
  int n, openSites;
  boolean[] sites;

  // creates n-by-n grid, with all sites initially blocked
  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException();
    }
    this.n = n;
    int size = n * n + 2;
    sites = new boolean[size];
    uf = new WeightedQuickUnionUF(size);

    int top = 0;
    int bottom = n * n + 1;
    for (int i = 0; i < n; i++) {
      uf.union(top, i + 1);
      uf.union(bottom, bottom - i - 1);
    }
  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {
    if (row < 1 || row > n || col < 1 || col > n) {
      throw new IllegalArgumentException();
    }

    int index = (row - 1) * n + col;
    sites[index] = true;
    openSites++;

    if (col > 1 && isOpen(row, col - 1)) {
      uf.union(index, index - 1);
    }

    if (col < n && isOpen(row, col + 1)) {
      uf.union(index, index + 1);
    }

    if (row > 1 && isOpen(row - 1, col)) {
      uf.union(index, index - n);
    }

    if (row < n && isOpen(row + 1, col)) {
      uf.union(index, index + n);
    }
  }

  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    return sites[(row - 1) * n + col];
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    return uf.find(0) == uf.find((row - 1) * n + col);
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return openSites;
  }

  // does the system percolate?
  public boolean percolates() {
    int top = 0;
    int bottom = n * n + 1;
    return uf.find(top) == uf.find(bottom);
  }

  // test client (optional)
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    int total = n * n;
    Percolation percolation = new Percolation(n);

    Stopwatch stopwatch = new Stopwatch();
    while (!percolation.percolates()) {
      int r = StdRandom.uniform(total - percolation.numberOfOpenSites());
      int i = 0;
      boolean found = false;
      for (int row = 1; row <= n; row++) {
        for (int col = 1; col <= n; col++) {
          if (!percolation.isOpen(row, col)) {
            if (r == i) {
              percolation.open(row, col);
              found = true;
              break;
            }
            i++;
          }
        }
        if (found) {
          break;
        }
      }
    }
    double elapsedTime = stopwatch.elapsedTime();
    double threshold = (double) percolation.numberOfOpenSites() / (n * n);
    StdOut.println("threshold:" + threshold);
    StdOut.println("elapsed time:" + elapsedTime);
  }
}
