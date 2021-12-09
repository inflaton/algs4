package com.inflaton.datastructures.queue;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class RandomizedQueue<Item> implements Iterable<Item> {
  // initial capacity of underlying resizing array
  private static final int INIT_CAPACITY = 8;

  private Item[] q; // queue elements
  private int n; // number of elements on queue
  private int first; // index of first element of queue
  private int last; // index of next available slot

  /** Initializes an empty queue. */
  public RandomizedQueue() {
    q = (Item[]) new Object[INIT_CAPACITY];
    n = 0;
    first = 0;
    last = 0;
  }

  /**
   * Is this queue empty?
   *
   * @return true if this queue is empty; false otherwise
   */
  public boolean isEmpty() {
    return n == 0;
  }

  /**
   * Returns the number of items in this queue.
   *
   * @return the number of items in this queue
   */
  public int size() {
    return n;
  }

  // resize the underlying array
  private void resize(int capacity) {
    assert capacity >= n;
    Item[] copy = (Item[]) new Object[capacity];
    for (int i = 0; i < n; i++) {
      copy[i] = q[(first + i) % q.length];
    }
    q = copy;
    first = 0;
    last = n;
  }

  /**
   * Adds the item to this queue.
   *
   * @param item the item to add
   */
  public void enqueue(Item item) {
    if (item == null) throw new IllegalArgumentException("null argument not allowed");

    // double size of array if necessary and recopy to front of array
    if (n == q.length) resize(2 * q.length); // double size of array if necessary
    q[last++] = item; // add item
    if (last == q.length) last = 0; // wrap-around
    n++;
  }

  /**
   * remove and return a random item
   *
   * @return random item
   * @throws java.util.NoSuchElementException if this queue is empty
   */
  public Item dequeue() {
    if (isEmpty()) throw new NoSuchElementException("Queue underflow");

    int i = StdRandom.uniform(n);
    Item item = q[(first + i) % q.length];
    if (i != 0) q[(first + i) % q.length] = q[first];
    q[first] = null; // to avoid loitering
    n--;
    first++;
    if (first == q.length) first = 0; // wrap-around
    // shrink size of array if necessary
    if (n > 0 && n == q.length / 4) resize(q.length / 2);
    return item;
  }

  /**
   * return a random item (but do not remove it).
   *
   * @return random item
   * @throws java.util.NoSuchElementException if this queue is empty
   */
  public Item sample() {
    if (isEmpty()) throw new NoSuchElementException("Queue underflow");
    int i = StdRandom.uniform(n);
    return q[(first + i) % q.length];
  }

  /**
   * Returns an iterator that iterates over the items in this queue in FIFO order.
   *
   * @return an iterator that iterates over the items in this queue in FIFO order
   */
  public Iterator<Item> iterator() {
    return new ArrayIterator();
  }

  // an iterator, doesn't implement remove() since it's optional
  private class ArrayIterator implements Iterator<Item> {
    private int index = 0;
    private Item[] copy;

    public ArrayIterator() {
      randomizeAndCopy();
    }

    // Fisher–Yates shuffle Algorithm works in O(n) time complexity. The assumption here is, we are
    // given a function rand() that generates random number in O(1) time.
    // The idea is to start from the last element, swap it with a randomly selected element from the
    // whole array (including last). Now consider the array from 0 to n-2 (size reduced by 1), and
    // repeat the process till we hit the first element.
    private void randomizeAndCopy() {
      copy = (Item[]) new Object[n];

      for (int i = n - 1; i >= 0; i--) {
        // Start from the last element and swap one by one. We don't
        // need to run for the first element that's why index > 0
        if (i > 0) {
          // Pick a random index from 0 to index
          int j = StdRandom.uniform(i + 1);

          // Swap arr[index] with the element at random index
          Item temp = q[(first + i) % q.length];
          q[(first + i) % q.length] = q[(first + j) % q.length];
          q[(first + j) % q.length] = temp;
        }

        copy[i] = q[(first + i) % q.length];
      }
    }

    public boolean hasNext() {
      return index < n;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    public Item next() {
      if (!hasNext()) throw new NoSuchElementException();
      Item item = copy[index];
      index++;
      return item;
    }
  }

  /**
   * Unit tests the {@code RandomizedQueue} data type.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    RandomizedQueue<String> queue = new RandomizedQueue<String>();
    while (!StdIn.isEmpty()) {
      String item = StdIn.readString();
      if (item.equals("+")) break;
      if (!item.equals("-")) queue.enqueue(item);
      else if (!queue.isEmpty()) StdOut.print(queue.dequeue() + " ");
    }

    int size = queue.size();
    StdOut.println("(" + size + " left on queue)");

    StdOut.println("iterate 1:");
    for (String item : queue) {
      StdOut.print(item);
      StdOut.print(" ");
    }
    StdOut.println();

    StdOut.println("iterate 2:");
    for (String item : queue) {
      StdOut.print(item);
      StdOut.print(" ");
    }
    StdOut.println();

    StdOut.println("iterate 3:");
    for (String item : queue) {
      StdOut.print(item);
      StdOut.print(" ");
    }
    StdOut.println();

    StdOut.println("samples:");
    while (size > 0) {
      String item = queue.sample();
      StdOut.print(item);
      StdOut.print(" ");
      size--;
    }
    StdOut.println();
  }
}
