package com.inflaton.datastructures.binarytree;

/** Node that can be printed */
public interface TreeNode {

  // Get getLeft() child
  public TreeNode getLeft();

  // Get getRight() child
  public TreeNode getRight();

  // Get text to be printed
  public String getText();

  default void printTree() {
    if (getRight() != null) {
      getRight().printTree(true, "");
    }
    printNodeValue();
    if (getLeft() != null) {
      getLeft().printTree(false, "");
    }
  }

  default void printNodeValue() {
    if (getText() == null) {
      System.out.print("<null>");
    } else {
      System.out.print(getText().toString());
    }
    System.out.print('\n');
  }

  // use string and not StringBuffer on purpose as we need to change the indent at
  // each recursion
  default void printTree(boolean isRight, String indent) {
    if (getRight() != null) {
      getRight().printTree(true, indent + (isRight ? "        " : " |      "));
    }
    System.out.print(indent);
    if (isRight) {
      System.out.print(" /");
    } else {
      System.out.print(" \\");
    }
    System.out.print("----- ");
    printNodeValue();
    if (getLeft() != null) {
      getLeft().printTree(false, indent + (isRight ? " |      " : "        "));
    }
  }
}
