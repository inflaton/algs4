public class BoggleTrie {
  private static final int R = 26; // uppercase letters only
  private Node root; // root of trie

  // R-way trie node
  public static class Node {
    private String word;
    private Node[] next = new Node[R];

    public boolean isWord() {
      return word != null;
    }

    public String getWord() {
      return word;
    }
  }

  public Node getNextNode(Node node, char c) {
    if (node == null) {
      node = root;
    }
    return node.next[c - 'A'];
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
    return x != null && x.isWord();
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
      throw new IllegalArgumentException("argument to add() is null");
    }
    try {
      root = add(root, key, 0);
    } catch (IllegalArgumentException exception) {
      // StdOut.println("ignored invalid key: " + key);
    }
  }

  private Node add(Node x, String key, int d) {
    if (x == null) {
      x = new Node();
    }

    if (d == key.length()) {
      if (!x.isWord()) {
        x.word = key;
      }
      return x;
    }

    char c = key.charAt(d);
    int nextIndex = d + 1;
    boolean isQu = (c == 'Q');
    if (c < 'A' || c > 'Z' || isQu && (nextIndex == key.length() || key.charAt(nextIndex) != 'U')) {
      throw new IllegalArgumentException("wrong key: " + key);
    }
    if (isQu) {
      nextIndex++;
    }
    x.next[c - 'A'] = add(x.next[c - 'A'], key, nextIndex);
    return x;
  }
}
