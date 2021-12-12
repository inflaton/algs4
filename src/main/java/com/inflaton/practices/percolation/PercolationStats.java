package com.inflaton.practices.percolation;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {

  private final double mean;
  private final double stddev;
  private final double delta;

  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException();
    }

    final int total = n * n;
    final int[] blockedSiteIndices = new int[total];
    final double[] thresholds = new double[trials];

    for (int run = 0; run < trials; run++) {
      Percolation percolation = new Percolation(n);
      int numberOfBlockedSites = total;
      for (int i = 0; i < total; i++) {
        blockedSiteIndices[i] = i;
      }

      while (!percolation.percolates()) {
        int r = StdRandom.uniform(numberOfBlockedSites);
        int index = blockedSiteIndices[r];
        int row = index / n + 1;
        int col = index % n + 1;
        percolation.open(row, col);
        numberOfBlockedSites--;
        blockedSiteIndices[r] = blockedSiteIndices[numberOfBlockedSites];
      }

      thresholds[run] = (double) percolation.numberOfOpenSites() / total;
    }

    mean = StdStats.mean(thresholds);
    stddev = StdStats.stddev(thresholds);
    delta = 1.96 * stddev / Math.sqrt(trials);
  }

  // sample mean of percolation threshold
  public double mean() {
    return mean;
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return stddev;
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return mean - delta;
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return mean + delta;
  }

  // test client (see below)
  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println("must have 2+ command-line arguments");
      return;
    }
    Stopwatch stopwatch = new Stopwatch();
    int n = Integer.parseInt(args[0]);
    int trials = Integer.parseInt(args[1]);

    PercolationStats stats = new PercolationStats(n, trials);
    StdOut.println("mean                    = " + stats.mean());
    StdOut.println("stddev                  = " + stats.stddev());
    StdOut.println(
        "95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");

    if (args.length > 2) {
      final double elapsedTime = stopwatch.elapsedTime();
      StdOut.println("total   elapsed time    = " + elapsedTime);
      StdOut.println("average elapsed time    = " + elapsedTime / trials);
    }
  }
}
