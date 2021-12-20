// Taken from @MightyPork at:
// https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
package com.inflaton.datastructures.binarytree;

import com.inflaton.datastructures.collection.queue.Queue;
import com.inflaton.datastructures.collection.stack.Stack;

import java.util.ArrayList;
import java.util.List;

public class TreeUtil {
  private static int MAX_WIDTH = 180;

  public static void printTree(TreeNode root) {
    if (root == null) {
      System.out.println("<empty tree>");
      return;
    }

    String display = getTreeDisplay(root);
    String[] lines = display.split("\n");
    int width = 0;
    for (String line : lines) {
      int len = line.length();
      if (len > width) {
        width = len;
      }
    }

    if (width <= MAX_WIDTH) {
      System.out.println(display);
      return;
    }

    TreeNode right = root.getRight();
    if (right != null) {
      printTree(right, true, "");
    }

    printNodeValue(root);

    TreeNode left = root.getLeft();
    if (left != null) {
      printTree(left, false, "");
    }

    System.out.println();
  }

  private static void printNodeValue(TreeNode node) {
    if (node.getText() == null) {
      System.out.print("<null>");
    } else {
      System.out.print(node.getText());
    }
    System.out.print('\n');
  }

  // use string and not StringBuffer on purpose as we need to change the indent at
  // each recursion
  private static void printTree(TreeNode node, boolean isRight, String indent) {
    TreeNode right = node.getRight();
    if (right != null) {
      printTree(right, true, indent + (isRight ? "        " : " |      "));
    }
    System.out.print(indent);
    if (isRight) {
      System.out.print(" /");
    } else {
      System.out.print(" \\");
    }
    System.out.print("----- ");

    printNodeValue(node);

    TreeNode left = node.getLeft();
    if (left != null) {
      printTree(left, false, indent + (isRight ? " |      " : "        "));
    }
  }

  // Print a binary tree.
  public static String getTreeDisplay(TreeNode root) {

    StringBuilder sb = new StringBuilder();
    List<List<String>> lines = new ArrayList<List<String>>();
    List<TreeNode> level = new ArrayList<TreeNode>();
    List<TreeNode> next = new ArrayList<TreeNode>();

    level.add(root);
    int nn = 1;
    int widest = 0;

    while (nn != 0) {
      nn = 0;
      List<String> line = new ArrayList<String>();
      for (TreeNode n : level) {
        if (n == null) {
          line.add(null);
          next.add(null);
          next.add(null);
        } else {
          String aa = n.getText();
          line.add(aa);
          if (aa.length() > widest) widest = aa.length();

          next.add(n.getLeft());
          next.add(n.getRight());

          if (n.getLeft() != null) nn++;
          if (n.getRight() != null) nn++;
        }
      }

      if (widest % 2 == 1) widest++;

      lines.add(line);

      List<TreeNode> tmp = level;
      level = next;
      next = tmp;
      next.clear();
    }

    int perPiece = lines.get(lines.size() - 1).size() * (widest + 4);
    for (int i = 0; i < lines.size(); i++) {
      List<String> line = lines.get(i);
      int hpw = (int) Math.floor(perPiece / 2f) - 1;
      if (i > 0) {
        for (int j = 0; j < line.size(); j++) {

          // split node
          char c = ' ';
          if (j % 2 == 1) {
            if (line.get(j - 1) != null) {
              c = (line.get(j) != null) ? '#' : '#';
            } else {
              if (j < line.size() && line.get(j) != null) c = '#';
            }
          }
          sb.append(c);

          // lines and spaces
          if (line.get(j) == null) {
            for (int k = 0; k < perPiece - 1; k++) {
              sb.append(' ');
            }
          } else {
            for (int k = 0; k < hpw; k++) {
              sb.append(j % 2 == 0 ? " " : "#");
            }
            sb.append(j % 2 == 0 ? "#" : "#");
            for (int k = 0; k < hpw; k++) {
              sb.append(j % 2 == 0 ? "#" : " ");
            }
          }
        }
        sb.append('\n');
      }
      for (int j = 0; j < line.size(); j++) {
        String f = line.get(j);
        if (f == null) f = "";
        int gap1 = (int) Math.ceil(perPiece / 2f - f.length() / 2f);
        int gap2 = (int) Math.floor(perPiece / 2f - f.length() / 2f);

        for (int k = 0; k < gap1; k++) {
          sb.append(' ');
        }
        sb.append(f);
        for (int k = 0; k < gap2; k++) {
          sb.append(' ');
        }
      }
      sb.append('\n');

      perPiece /= 2;
    }

    return sb.toString();
  }

  // This method returns an iterator for a given TreeTraversalOrder.
  // The ways in which you can traverse the tree are in four different ways:
  // preorder, inorder, postorder and levelorder.
  public static Iterable<TreeNode> traverse(TreeNode root, TreeTraversalOrder order) {
    switch (order) {
      case PRE_ORDER:
        return preOrderTraversal(root);
      case IN_ORDER:
        return inOrderTraversal(root);
      case POST_ORDER:
        return postOrderTraversal(root);
      case LEVEL_ORDER:
        return levelOrderTraversal(root);
      default:
        return null;
    }
  }

  public static Iterable<TreeNode> preOrderTraversal(TreeNode root) {
    final Queue<TreeNode> result = new Queue<>();
    if (root != null) {
      final Stack<TreeNode> stack = new Stack<>();
      stack.push(root);

      while (!stack.isEmpty()) {
        TreeNode node = stack.pop();
        result.enqueue(node);

        if (node.getRight() != null) stack.push(node.getRight());
        if (node.getLeft() != null) stack.push(node.getLeft());
      }
    }
    return result;
  }

  public static Iterable<TreeNode> inOrderTraversal(TreeNode root) {
    final Queue<TreeNode> result = new Queue<>();
    if (root != null) {
      final Stack<TreeNode> stack = new Stack<>();
      stack.push(root);
      TreeNode current = root;

      while (!stack.isEmpty()) {
        // Dig left
        while (current != null && current.getLeft() != null) {
          stack.push(current.getLeft());
          current = current.getLeft();
        }

        TreeNode node = stack.pop();
        result.enqueue(node);

        // Try moving down right once
        if (node.getRight() != null) {
          stack.push(node.getRight());
          current = node.getRight();
        }
      }
    }
    return result;
  }

  public static Iterable<TreeNode> postOrderTraversal(TreeNode root) {
    final Stack<TreeNode> result = new Stack<>();
    if (root != null) {
      final Stack<TreeNode> stack = new Stack<>();
      stack.push(root);
      while (!stack.isEmpty()) {
        TreeNode node = stack.pop();
        if (node != null) {
          result.push(node);
          if (node.getLeft() != null) stack.push(node.getLeft());
          if (node.getRight() != null) stack.push(node.getRight());
        }
      }
    }
    return result;
  }

  public static Iterable<TreeNode> levelOrderTraversal(TreeNode root) {
    final Queue<TreeNode> result = new Queue<>();
    if (root != null) {
      final Queue<TreeNode> queue = new Queue<>();
      queue.enqueue(root);

      while (!queue.isEmpty()) {
        TreeNode node = queue.dequeue();
        result.enqueue(node);

        if (node.getLeft() != null) queue.enqueue(node.getLeft());
        if (node.getRight() != null) queue.enqueue(node.getRight());
      }
    }
    return result;
  }

  public static Iterable<TreeNode> morrisTraversal(TreeNode root, TreeTraversalOrder order) {
    if (TreeTraversalOrder.LEVEL_ORDER == order) return levelOrderTraversal(root);
    if (TreeTraversalOrder.POST_ORDER == order) return morrisTraversalPostOrder(root);

    Queue<TreeNode> nodes = new Queue<TreeNode>();
    TreeNode current, predecessor;
    current = root;
    while (current != null) {
      if (current.getLeft() == null) {
        nodes.enqueue(current);
        current = current.getRight();
      } else {
        // Find the inorder predecessor of current
        predecessor = current.getLeft();
        while (predecessor.getRight() != null && predecessor.getRight() != current)
          predecessor = predecessor.getRight();

        // Make current as right child of its inorder predecessor
        if (predecessor.getRight() == null) {
          predecessor.setRight(current);
          if (order == TreeTraversalOrder.PRE_ORDER) nodes.enqueue(current);
          current = current.getLeft();
        }

        // Revert the changes made in the 'if' part
        // to restore the original tree i.e., fix
        // the right child of predecessor
        else {
          predecessor.setRight(null);
          if (order == TreeTraversalOrder.IN_ORDER) nodes.enqueue(current);
          current = current.getRight();
        } // End of if condition predecessor->right == NULL
      } // End of if condition current->left == NULL
    } // End of while

    return nodes;
  }

  private static Iterable<TreeNode> morrisTraversalPostOrder(TreeNode root) {
    Stack<TreeNode> nodes = new Stack<TreeNode>();
    TreeNode current, predecessor;
    current = root;
    while (current != null) {
      if (current.getRight() == null) {
        nodes.push(current);
        current = current.getLeft();
      } else {
        // Find the inorder predecessor of current
        predecessor = current.getRight();
        while (predecessor.getLeft() != null && predecessor.getLeft() != current)
          predecessor = predecessor.getLeft();

        // Make current as left child of its inorder predecessor
        if (predecessor.getLeft() == null) {
          predecessor.setLeft(current);
          nodes.push(current);
          current = current.getRight();
        }

        // Revert the changes made in the 'if' part
        // to restore the original tree i.e., fix
        // the left child of predecessor
        else {
          predecessor.setLeft(null);
          current = current.getLeft();
        } // End of if condition predecessor->left == NULL
      } // End of if condition current->right == NULL
    } // End of while

    return nodes;
  }
}
