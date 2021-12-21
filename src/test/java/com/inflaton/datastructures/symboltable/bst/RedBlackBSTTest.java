package com.inflaton.datastructures.symboltable.bst;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RedBlackBSTTest {
  private static final int TEST_SIZE = 10000;
  private static String[] keys;
  private static RedBlackBST<String, Integer> bstAll;
  private RedBlackBST<String, Integer> bst;

  @BeforeAll
  static void setUpAll() {
    keys = new String[TEST_SIZE];
    for (int i = 0; i < TEST_SIZE; i++) {
      keys[i] = String.valueOf(i);
    }
    StdRandom.shuffle(keys);

    bstAll = new RedBlackBST<String, Integer>();
    for (int i = 0; i < TEST_SIZE; i++) {
      bstAll.put(keys[i], i);
    }
  }

  @BeforeEach
  void setUp() {
    bst = new RedBlackBST<String, Integer>();
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

  @Test
  void testPut() {
    for (int i = 0; i < TEST_SIZE; i++) {
      bst.put(keys[i], i);
    }
    assertEquals(TEST_SIZE, bst.size());
  }

  @Test
  void testGet() {
    for (int i = 0; i < TEST_SIZE; i++) {
      assertEquals(i, bstAll.get(keys[i]));
    }
  }

  @Test
  void testPutAndDelete() {
    for (int i = 0; i < TEST_SIZE; i++) {
      bst.put(keys[i], i);
    }
    assertEquals(TEST_SIZE, bst.size());

    for (int i = 0; i < TEST_SIZE; i++) {
      bst.delete(keys[i]);
    }
    assertEquals(0, bst.size());
  }
}
