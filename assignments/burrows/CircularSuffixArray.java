import edu.princeton.cs.algs4.MergeX;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {

  private static class IndexedSuffix implements Comparable<IndexedSuffix> {
    private int index;
    private String suffix;

    public IndexedSuffix(int i, char[] chars) {
      index = i;
      suffix = String.valueOf(chars);
    }

    @Override
    public int compareTo(IndexedSuffix that) {
      return this.suffix.compareTo(that.suffix);
    }
  }

  private final IndexedSuffix[] suffixArray;

  // circular suffix array of s
  public CircularSuffixArray(String s) {
    if (s == null) {
      throw new IllegalArgumentException();
    }
    suffixArray = new IndexedSuffix[s.length()];
    char[] chars = s.toCharArray();
    for (int i = 0; i < s.length(); i++) {
      if (i > 0) {
        rotate(chars);
      }
      suffixArray[i] = new IndexedSuffix(i, chars);
    }

    MergeX.sort(suffixArray);
  }

  private void rotate(char[] chars) {
    int k = chars.length - 1;
    char swap = chars[0];
    for (int i = 0; i < k; i++) {
      chars[i] = chars[i + 1];
    }
    chars[k] = swap;
  }

  // length of s
  public int length() {
    return suffixArray.length;
  }

  // returns index of ith sorted suffix
  public int index(int i) {
    if (i < 0 || i >= suffixArray.length) {
      throw new IllegalArgumentException();
    }
    return suffixArray[i].index;
  }

  // unit testing (required)
  public static void main(String[] args) {
    String s = "ABRACADABRA!";
    CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
    for (int i = 0; i < circularSuffixArray.length(); i++) {
      int index = circularSuffixArray.index(i);
      StdOut.println(index);
    }
  }
}
