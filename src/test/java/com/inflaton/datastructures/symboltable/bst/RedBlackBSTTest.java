package com.inflaton.datastructures.symboltable.bst;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RedBlackBSTTest {
  private static final int TEST_SIZE = 10000;
  private String[] keys;
  private RedBlackBST<String, Integer> bst;

  @BeforeEach
  void setUp() {
    bst = new RedBlackBST<String, Integer>();

    int n = TEST_SIZE / 2;
    keys = new String[TEST_SIZE];
    for (int i = 0; i < TEST_SIZE; i++) {
      keys[i] = String.valueOf(StdRandom.uniform(n));
    }
  }

  @Test
  void testPrintTree() {
    for (int i = 0; i < 20; i++) {
      StdOut.println("height:" + bst.height() + "\tsize:" + bst.size());
      bst.printTree();
      bst.put(keys[i], i);
    }
    bst.printTree();
  }
}
