package com.inflaton.datastructures.binarytree;

/** Node that can be printed */
public interface TreeNode {

  // Get left child
  public TreeNode getLeft();

  // Set left child
  public void setLeft(TreeNode node);

  // Get right child
  public TreeNode getRight();

  // Set right child
  public void setRight(TreeNode node);

  // Get text to be printed
  public String getText();
}
