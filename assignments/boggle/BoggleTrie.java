import edu.princeton.cs.algs4.Queue;

public class BoggleTrie {
  private static final int R = 26; // uppercase letters only

  private Node root; // root of trie
  private int numberOfKeys; // number of keys in trie

  // R-way trie node
  private static class Node {
    private boolean isWord;
    private Node[] next = new Node[R];
  }

  private Node get(Node x, String key, int d) {
    if (x == null) {
      return null;
    }
    if (d == key.length()) {
      return x;
    }
    char c = key.charAt(d);
    return get(x.next[c - 'A'], key, d + 1);
  }

  /**
   * check if this symbol table contain the given key.
   *
   * @param key the key
   * @return {@code true} if this symbol table contains {@code key} and {@code false} otherwise
   * @throws IllegalArgumentException if {@code key} is {@code null}
   */
  public boolean contains(String key) {
    if (key == null) {
      throw new IllegalArgumentException("argument to contains() is null");
    }
    Node x = get(root, key, 0);
    return x != null && x.isWord;
  }

  /**
   * check if this symbol table contain keys in the set that start with {@code prefix}.
   *
   * @param prefix the prefix
   * @return all of the keys in the set that start with {@code prefix}, as an iterable
   */
  public boolean hasKeysWithPrefix(String prefix) {
    if (prefix == null) {
      throw new IllegalArgumentException("argument to contains() is null");
    }
    Node x = get(root, prefix, 0);
    return x != null;
  }

  /**
   * Inserts the key-value pair into the symbol table, overwriting the old value with the new value
   * if the key is already in the symbol table. If the value is {@code null}, this effectively
   * deletes the key from the symbol table.
   *
   * @param key the key
   * @throws IllegalArgumentException if {@code key} is {@code null}
   */
  public void add(String key) {
    if (key == null) {
      throw new IllegalArgumentException("argument to put() is null");
    }
    root = add(root, key, 0);
  }

  private Node add(Node x, String key, int d) {
    if (x == null) {
      x = new Node();
    }
    if (d == key.length()) {
      if (!x.isWord) {
        numberOfKeys++;
      }
      x.isWord = true;
      return x;
    }
    char c = key.charAt(d);
    x.next[c - 'A'] = add(x.next[c - 'A'], key, d + 1);
    return x;
  }

  /**
   * Returns the number of key-value pairs in this symbol table.
   *
   * @return the number of key-value pairs in this symbol table
   */
  public int size() {
    return numberOfKeys;
  }

  /*
   * Is this symbol table empty?
   *
   * @return {@code true} if this symbol table is empty and {@code false} otherwise
   */
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Returns all of the keys in the set that start with {@code prefix}.
   *
   * @param prefix the prefix
   * @return all of the keys in the set that start with {@code prefix}, as an iterable
   */
  public Iterable<String> keysWithPrefix(String prefix) {
    Queue<String> results = new Queue<String>();
    Node x = get(root, prefix, 0);
    collect(x, new StringBuilder(prefix), results);
    return results;
  }

  private void collect(Node x, StringBuilder prefix, Queue<String> results) {
    if (x == null) {
      return;
    }
    if (x.isWord) {
      results.enqueue(prefix.toString());
    }
    for (char c = 0; c < R; c++) {
      prefix.append(c + 'A');
      collect(x.next[c], prefix, results);
      prefix.deleteCharAt(prefix.length() - 1);
    }
  }
}
