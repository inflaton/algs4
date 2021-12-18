package com.inflaton.datastructures.binarytree;

/** Node that can be printed */
public interface TreeNode {

  // Get left child
  public TreeNode getLeft();

  // Get right child
  public TreeNode getRight();

  // Get text to be printed
  public String getText();
}
