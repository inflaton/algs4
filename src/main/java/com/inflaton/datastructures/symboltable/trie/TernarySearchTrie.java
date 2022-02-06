/******************************************************************************
 *  Compilation:  javac TernarySearchTrie.java
 *  Execution:    java TernarySearchTrie < words.txt
 *  Dependencies: StdIn.java
 *  Data files:   https://algs4.cs.princeton.edu/52trie/shellsST.txt
 *
 *  Symbol table with string keys, implemented using a ternary search
 *  trie (TernarySearchTrie).
 *
 *
 *  % java TernarySearchTrie < shellsST.txt
 *  keys(""):
 *  by 4
 *  sea 6
 *  sells 1
 *  she 0
 *  shells 3
 *  shore 7
 *  the 5
 *
 *  longestPrefixOf("shellsort"):
 *  shells
 *
 *  keysWithPrefix("shor"):
 *  shore
 *
 *  keysThatMatch(".he.l."):
 *  shells
 *
 *  % java TernarySearchTrie
 *  theory the now is the time for all good men
 *
 *  Remarks
 *  --------
 *    - can't use a key that is the empty string ""
 *
 ******************************************************************************/

package com.inflaton.datastructures.symboltable.trie;

import com.inflaton.datastructures.collection.queue.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * The {@code TernarySearchTrie} class represents an symbol table of key-value pairs, with string
 * keys and generic values. It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 * <em>delete</em>, <em>size</em>, and <em>is-empty</em> methods. It also provides character-based
 * methods for finding the string in the symbol table that is the <em>longest prefix</em> of a given
 * prefix, finding all strings in the symbol table that <em>start with</em> a given prefix, and
 * finding all strings in the symbol table that <em>match</em> a given pattern. A symbol table
 * implements the <em>associative array</em> abstraction: when associating a value with a key that
 * is already in the symbol table, the convention is to replace the old value with the new value.
 * Unlike {@link java.util.Map}, this class uses the convention that values cannot be {@code
 * null}—setting the value associated with a key to {@code null} is equivalent to deleting the key
 * from the symbol table.
 *
 * <p>This implementation uses a ternary search trie.
 *
 * <p>For additional documentation, see <a href="https://algs4.cs.princeton.edu/52trie">Section
 * 5.2</a> of <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class TernarySearchTrie<T> {
  private int size; // size
  private Node<T> root; // root of TernarySearchTrie

  private static class Node<T> {
    private char ch; // character
    private Node<T> left;
    private Node<T> mid;
    private Node<T> right;
    private T val; // value associated with string
  }

  /** Initializes an empty string symbol table. */
  public TernarySearchTrie() {}

  /**
   * Returns the number of key-value pairs in this symbol table.
   *
   * @return the number of key-value pairs in this symbol table
   */
  public int size() {
    return size;
  }

  /**
   * check if this symbol table contains the given key.
   *
   * @param key the key
   * @return {@code true} if this symbol table contains {@code key} and {@code false} otherwise
   * @throws IllegalArgumentException if {@code key} is {@code null}
   */
  public boolean contains(String key) {
    if (key == null) {
      throw new IllegalArgumentException("argument to contains() is null");
    }
    return get(key) != null;
  }

  /**
   * Returns the value associated with the given key.
   *
   * @param key the key
   * @return the value associated with the given key if the key is in the symbol table and {@code
   *     null} if the key is not in the symbol table
   * @throws IllegalArgumentException if {@code key} is {@code null}
   */
  public T get(String key) {
    if (key == null) {
      throw new IllegalArgumentException("calls get() with null argument");
    }
    if (key.length() == 0) {
      throw new IllegalArgumentException("key must have length >= 1");
    }
    Node<T> x = get(root, key, 0);
    if (x == null) {
      return null;
    }
    return x.val;
  }

  // return subtrie corresponding to given key
  private Node<T> get(Node<T> x, String key, int d) {
    if (x == null) {
      return null;
    }
    if (key.length() == 0) {
      throw new IllegalArgumentException("key must have length >= 1");
    }
    char c = key.charAt(d);
    if (c < x.ch) {
      return get(x.left, key, d);
    } else if (c > x.ch) {
      return get(x.right, key, d);
    } else if (d < key.length() - 1) {
      return get(x.mid, key, d + 1);
    } else {
      return x;
    }
  }

  /**
   * Inserts the key-value pair into the symbol table, overwriting the old value with the new value
   * if the key is already in the symbol table. If the value is {@code null}, this effectively
   * deletes the key from the symbol table.
   *
   * @param key the key
   * @param val the value
   * @throws IllegalArgumentException if {@code key} is {@code null}
   */
  public void put(String key, T val) {
    if (key == null) {
      throw new IllegalArgumentException("calls put() with null key");
    }
    if (!contains(key)) {
      size++;
    } else if (val == null) { // delete existing key
      size--;
    }
    root = put(root, key, val, 0);
  }

  private Node<T> put(Node<T> x, String key, T val, int d) {
    char c = key.charAt(d);
    if (x == null) {
      x = new Node<T>();
      x.ch = c;
    }
    if (c < x.ch) {
      x.left = put(x.left, key, val, d);
    } else if (c > x.ch) {
      x.right = put(x.right, key, val, d);
    } else if (d < key.length() - 1) {
      x.mid = put(x.mid, key, val, d + 1);
    } else {
      x.val = val;
    }
    return x;
  }

  /**
   * Returns the string in the symbol table that is the longest prefix of {@code query}, or {@code
   * null}, if no such string.
   *
   * @param query the query string
   * @return the string in the symbol table that is the longest prefix of {@code query}, or {@code
   *     null} if no such string
   * @throws IllegalArgumentException if {@code query} is {@code null}
   */
  public String longestPrefixOf(String query) {
    return longestPrefixOf(query, 0);
  }

  /**
   * Returns the string in the symbol table that is the longest prefix of {@code query}, or {@code
   * null}, if no such string.
   *
   * @param query the query string
   * @param startIndex the start index in query string
   * @return the string in the symbol table that is the longest prefix of {@code query}, or {@code
   *     null} if no such string
   * @throws IllegalArgumentException if {@code query} is {@code null}
   */
  public String longestPrefixOf(String query, int startIndex) {
    if (query == null || startIndex < 0 || startIndex >= query.length()) {
      throw new IllegalArgumentException("calls longestPrefixOf() with wrong arguments");
    }
    int length = 0;
    Node<T> x = root;
    int i = 0;
    while (x != null && i + startIndex < query.length()) {
      char c = query.charAt(i + startIndex);
      if (c < x.ch) {
        x = x.left;
      } else if (c > x.ch) {
        x = x.right;
      } else {
        i++;
        if (x.val != null) {
          length = i;
        }
        x = x.mid;
      }
    }
    return query.substring(startIndex, startIndex + length);
  }

  /**
   * Returns all keys in the symbol table as an {@code Iterable}. To iterate over all of the keys in
   * the symbol table named {@code st}, use the foreach notation: {@code for (Key key : st.keys())}.
   *
   * @return all keys in the symbol table as an {@code Iterable}
   */
  public Iterable<String> keys() {
    Queue<String> queue = new Queue<String>();
    collect(root, new StringBuilder(), queue);
    return queue;
  }

  /**
   * Returns all of the keys in the set that start with {@code prefix}.
   *
   * @param prefix the prefix
   * @return all of the keys in the set that start with {@code prefix}, as an iterable
   * @throws IllegalArgumentException if {@code prefix} is {@code null}
   */
  public Iterable<String> keysWithPrefix(String prefix) {
    if (prefix == null) {
      throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
    }
    Queue<String> queue = new Queue<String>();
    Node<T> x = get(root, prefix, 0);
    if (x == null) {
      return queue;
    }
    if (x.val != null) {
      queue.enqueue(prefix);
    }
    collect(x.mid, new StringBuilder(prefix), queue);
    return queue;
  }

  // all keys in subtrie rooted at x with given prefix
  private void collect(Node<T> x, StringBuilder prefix, Queue<String> queue) {
    if (x == null) {
      return;
    }
    collect(x.left, prefix, queue);
    if (x.val != null) {
      queue.enqueue(prefix.toString() + x.ch);
    }
    collect(x.mid, prefix.append(x.ch), queue);
    prefix.deleteCharAt(prefix.length() - 1);
    collect(x.right, prefix, queue);
  }

  private void collect(
      Node<T> x, StringBuilder prefix, int i, String pattern, Queue<String> queue) {
    if (x == null) {
      return;
    }
    char c = pattern.charAt(i);
    if (c == '.' || c < x.ch) {
      collect(x.left, prefix, i, pattern, queue);
    }
    if (c == '.' || c == x.ch) {
      if (i == pattern.length() - 1 && x.val != null) {
        queue.enqueue(prefix.toString() + x.ch);
      }
      if (i < pattern.length() - 1) {
        collect(x.mid, prefix.append(x.ch), i + 1, pattern, queue);
        prefix.deleteCharAt(prefix.length() - 1);
      }
    }
    if (c == '.' || c > x.ch) {
      collect(x.right, prefix, i, pattern, queue);
    }
  }

  /**
   * Returns all of the keys in the symbol table that match {@code pattern}, where the character '.'
   * is interpreted as a wildcard character.
   *
   * @param pattern the pattern
   * @return all of the keys in the symbol table that match {@code pattern}, as an iterable, where .
   *     is treated as a wildcard character.
   */
  public Iterable<String> keysThatMatch(String pattern) {
    Queue<String> queue = new Queue<String>();
    collect(root, new StringBuilder(), 0, pattern, queue);
    return queue;
  }

  /**
   * Unit tests the {@code TernarySearchTrie} data type.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {

    // build symbol table from standard input
    TernarySearchTrie<Integer> st = new TernarySearchTrie<Integer>();
    for (int i = 0; !StdIn.isEmpty(); i++) {
      String key = StdIn.readString();
      st.put(key, i);
    }

    // print results
    if (st.size() < 100) {
      StdOut.println("keys(\"\"):");
      for (String key : st.keys()) {
        StdOut.println(key + " " + st.get(key));
      }
      StdOut.println();
    }

    StdOut.println("longestPrefixOf(\"shellsort\"):");
    StdOut.println(st.longestPrefixOf("shellsort"));
    StdOut.println();

    StdOut.println("longestPrefixOf(\"shell\"):");
    StdOut.println(st.longestPrefixOf("shell"));
    StdOut.println();

    StdOut.println("keysWithPrefix(\"shor\"):");
    for (String s : st.keysWithPrefix("shor")) {
      StdOut.println(s);
    }
    StdOut.println();

    StdOut.println("keysThatMatch(\".he.l.\"):");
    for (String s : st.keysThatMatch(".he.l.")) {
      StdOut.println(s);
    }
  }
}

/******************************************************************************
 *  Copyright 2002-2020, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
