/******************************************************************************
 *  Compilation:  javac-algs4 PuzzleChecker.java
 *  Execution:    java-algs4 PuzzleChecker filename1.txt filename2.txt ...
 *  Dependencies: Board.java Solver.java
 *
 *  This program creates an initial board from each filename specified
 *  on the command line and finds the minimum number of moves to
 *  reach the goal state.
 *
 *  % java-algs4 PuzzleChecker puzzle*.txt
 *  puzzle00.txt: 0
 *  puzzle01.txt: 1
 *  puzzle02.txt: 2
 *  puzzle03.txt: 3
 *  puzzle04.txt: 4
 *  puzzle05.txt: 5
 *  puzzle06.txt: 6
 *  ...
 *  puzzle3x3-impossible: -1
 *  ...
 *  puzzle42.txt: 42
 *  puzzle43.txt: 43
 *  puzzle44.txt: 44
 *  puzzle45.txt: 45
 *
 ******************************************************************************/

package com.inflaton.practices.gempuzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class PuzzleChecker {

  public static void main(String[] args) {

    // for each command-line argument
    for (String filename : args) {

      // read in the board specified in the filename
      In in = new In(filename);
      int n = in.readInt();
      int[][] tiles = new int[n][n];
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          tiles[i][j] = in.readInt();
        }
      }

      StdOut.print(filename + ": ");
      // solve the slider puzzle
      Board initial = new Board(tiles);
      Solver solver = new Solver(initial);
      StdOut.println(solver.moves());
    }
  }
}

// src/test/data/gempuzzle/puzzle38.txt: 40

// src/test/data/gempuzzle/puzzle3x3-32.txt: 14

// src/test/data/gempuzzle/puzzle49.txt: Exception in thread "main" java.lang.OutOfMemoryError: Java
// heap space
//        at java.base/java.util.ArrayList.grow(ArrayList.java:239)
//        at java.base/java.util.ArrayList.grow(ArrayList.java:244)
//        at java.base/java.util.ArrayList.add(ArrayList.java:454)
//        at java.base/java.util.ArrayList.add(ArrayList.java:467)
//        at Board$NeighborIterator.addNeighbor(Board.java:177)
//        at Board$NeighborIterator.<init>(Board.java:163)
//        at Board$1.iterator(Board.java:136)
//        at Solver.moveOneStep(Solver.java:87)
//        at Solver.<init>(Solver.java:67)
//        at PuzzleChecker.main(PuzzleChecker.java:51)
