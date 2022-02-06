/******************************************************************************
 *  Compilation:  javac LempelZivWelch.java
 *  Execution:    java LempelZivWelch - < input.txt   (compress)
 *  Execution:    java LempelZivWelch + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   https://algs4.cs.princeton.edu/55compression/abraLZW.txt
 *                https://algs4.cs.princeton.edu/55compression/ababLZW.txt
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *
 ******************************************************************************/

package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.TernarySearchTrie;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * The {@code LempelZivWelch} class provides static methods for compressing and expanding a binary
 * input using LempelZivWelch compression over the 8-bit extended ASCII alphabet with 12-bit
 * codewords.
 *
 * <p>Starting with Oracle Java 7u6, the substring method takes time and space linear in the length
 * of the extracted substring (instead of constant time an space as in earlier versions). As a
 * result, compression takes quadratic time in the original {@code LZW} class. See <a href =
 * "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a> for more
 * details.
 *
 * <p>This class, along with {@code TernarySearchTrie} - the enhanced version of {@code TST}, fixes
 * the above issue.The key to the fix is to use the new method {@code
 * TernarySearchTrie.longestPrefixOf(String query, int startIndex)} in {@code compress()}.
 *
 * <p>For additional documentation, see <a
 * href="https://algs4.cs.princeton.edu/55compression">Section 5.5</a> of <i>Algorithms, 4th
 * Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class LempelZivWelch {
  private static final int R = 256; // number of input chars
  private static final int L = 4096; // number of codewords = 2^W
  private static final int W = 12; // codeword width

  // Do not instantiate.
  private LempelZivWelch() {}

  /**
   * Reads a sequence of 8-bit bytes from standard input; compresses them using LempelZivWelch
   * compression with 12-bit codewords; and writes the results to standard output.
   */
  public static void compress() {
    String input = BinaryStdIn.readString();
    TernarySearchTrie<Integer> st = new TernarySearchTrie<Integer>();

    // since TernarySearchTrie is not balanced, it would be better to insert in a different order
    for (int i = 0; i < R; i++) {
      st.put("" + (char) i, i);
    }

    int code = R + 1; // R is codeword for EOF
    int index = 0;
    while (index < input.length()) {
      String s = st.longestPrefixOf(input, index); // Find max prefix match s.
      BinaryStdOut.write(st.get(s), W); // Print s's encoding.
      int t = s.length();
      if (t < input.length() && code < L) {
        // Add s to symbol table.
        st.put(input.substring(index, index + t + 1), code++);
      }
      index += t; // Scan past s in input.
    }
    BinaryStdOut.write(R, W);
    BinaryStdOut.close();
  }

  /**
   * Reads a sequence of bit encoded using LempelZivWelch compression with 12-bit codewords from
   * standard input; expands them; and writes the results to standard output.
   */
  public static void expand() {
    String[] st = new String[L];
    int i; // next available codeword value

    // initialize symbol table with all 1-character strings
    for (i = 0; i < R; i++) {
      st[i] = "" + (char) i;
    }
    st[i++] = ""; // (unused) lookahead for EOF

    int codeword = BinaryStdIn.readInt(W);
    if (codeword == R) {
      return; // expanded message is empty string
    }
    String val = st[codeword];

    while (true) {
      BinaryStdOut.write(val);
      codeword = BinaryStdIn.readInt(W);
      if (codeword == R) {
        break;
      }
      String s = st[codeword];
      if (i == codeword) { // special case hack
        s = val + val.charAt(0);
      }
      if (i < L) {
        st[i++] = val + s.charAt(0);
      }
      val = s;
    }
    BinaryStdOut.close();
  }

  /**
   * Sample client that calls {@code compress()} if the command-line argument is "-" an {@code
   * expand()} if it is "+".
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    if (args[0].equals("-")) {
      compress();
    } else if (args[0].equals("+")) {
      expand();
    } else {
      throw new IllegalArgumentException("Illegal command line argument");
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
