import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private static class Node<Item> {
        Item item;
        Node<Item> next, prev;
    }

    private Node<Item> head, tail;
    private int size;

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        ensureNonNull(item);
        Node<Item> newNode = new Node<Item>();
        newNode.item = item;

        if (head != null) {
            newNode.next = head;
            head.prev = newNode;
        }
        head = newNode;

        if (tail == null) {
            tail = newNode;
        }
        size++;
    }

    private void ensureNonNull(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null argument not allowed");
        }
    }

    // add the item to the back
    public void addLast(Item item) {
        ensureNonNull(item);
        Node<Item> newNode = new Node<Item>();
        newNode.item = item;
        if (tail != null) {
            tail.next = newNode;
            newNode.prev = tail;
        }
        tail = newNode;

        if (head == null) {
            head = newNode;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        ensureNotEmpty();
        Item item = head.item;
        head = head.next;

        if (head != null) {
            head.prev = null;
        }
        size--;
        if (size == 0) {
            tail = null;
        }
        return item;
    }

    private void ensureNotEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    // remove and return the item from the back
    public Item removeLast() {
        ensureNotEmpty();
        Item item = tail.item;
        tail = tail.prev;

        if (tail != null) {
            tail.next = null;
        }
        size--;
        if (size == 0) {
            head = null;
        }
        return item;
    }

    // an iterator, doesn't implement remove() since it's optional
    private class LinkedIterator implements Iterator<Item> {
        private Node<Item> iterator;

        public LinkedIterator(Node<Item> head) {
            iterator = head;
        }

        public boolean hasNext() {
            return iterator != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = iterator.item;
            iterator = iterator.next;
            return item;
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new LinkedIterator(head);
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        StdOut.println("isEmpty: " + deque.isEmpty());

        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        deque.addFirst(4);
        deque.addLast(5);
        deque.addLast(6);
        deque.addLast(7);
        deque.addLast(8);

        StdOut.println("isEmpty: " + deque.isEmpty());
        StdOut.println("size: " + deque.size());
        for (int i : deque) {
            StdOut.println(i);
        }

        StdOut.println("removed first: " + deque.removeFirst());
        StdOut.println("removed first: " + deque.removeFirst());
        StdOut.println("removed last: " + deque.removeLast());
        StdOut.println("removed last: " + deque.removeLast());

        StdOut.println("isEmpty: " + deque.isEmpty());
        StdOut.println("size: " + deque.size());
        for (int i : deque) {
            StdOut.println(i);
        }
    }

}
