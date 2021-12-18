package com.inflaton.datastructures.binarytree;

/** Node that can be printed */
public interface TreeNode {

  // Get getLeft() child
  public TreeNode getLeft();

  // Get getRight() child
  public TreeNode getRight();

  // Get text to be printed
  public String getText();
}
