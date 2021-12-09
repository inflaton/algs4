import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
  public static void main(String[] args) {
    int k = Integer.parseInt(args[0]);

    RandomizedQueue<String> queue = new RandomizedQueue<String>();
    while (!StdIn.isEmpty()) {
      String item = StdIn.readString();
      queue.enqueue(item);
    }

    while (k > 0 && !queue.isEmpty()) {
      StdOut.println(queue.dequeue());
      k--;
    }
  }
}
