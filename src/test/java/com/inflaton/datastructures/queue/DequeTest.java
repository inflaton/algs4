package com.inflaton.datastructures.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class DequeTest {
  private static final int TEST_SIZE = 1000;
  private Deque<Integer> queue;

  @BeforeEach
  public void setup() {
    queue = new Deque<>();
  }

  @Test
  public void testRemoveFirstOnEmpty() {
    assertThrows(
        NoSuchElementException.class,
        () -> {
          queue.removeFirst();
        });
  }

  @Test
  public void testRemoveLastOnEmpty() {
    assertThrows(
        NoSuchElementException.class,
        () -> {
          queue.removeLast();
        });
  }

  @Test
  public void testAddFirstOnNull() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          queue.addFirst(null);
        });
  }

  @Test
  public void testAddLastOnNull() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          queue.addLast(null);
        });
  }

  @Test
  public void testIsEmpty() {
    assertTrue(queue.isEmpty());
    queue.addFirst(1);
    assertFalse(queue.isEmpty());
    queue.removeLast();
    assertTrue(queue.isEmpty());
  }

  @Test
  public void testSize() {
    assertTrue(queue.isEmpty());
    assertEquals(0, queue.size());

    queue.addFirst(1);
    assertFalse(queue.isEmpty());
    assertEquals(1, queue.size());

    queue.addLast(2);
    assertFalse(queue.isEmpty());
    assertEquals(2, queue.size());

    queue.removeFirst();
    assertEquals(1, queue.size());
    assertFalse(queue.isEmpty());

    queue.removeLast();
    assertEquals(0, queue.size());
    assertTrue(queue.isEmpty());
  }

  @Test
  public void testAddRemove() {
    assertTrue(queue.isEmpty());
    assertEquals(0, queue.size());

    queue.addFirst(1);
    assertFalse(queue.isEmpty());
    assertEquals(1, queue.size());

    queue.addLast(2);
    assertFalse(queue.isEmpty());
    assertEquals(2, queue.size());

    assertEquals(Integer.valueOf(1), queue.removeFirst());
    assertEquals(1, queue.size());
    assertFalse(queue.isEmpty());

    assertEquals(Integer.valueOf(2), queue.removeFirst());
    assertEquals(0, queue.size());
    assertTrue(queue.isEmpty());

    queue.addFirst(10);
    assertFalse(queue.isEmpty());
    assertEquals(1, queue.size());

    queue.addFirst(20);
    assertFalse(queue.isEmpty());
    assertEquals(2, queue.size());

    assertEquals(Integer.valueOf(10), queue.removeLast());
    assertEquals(1, queue.size());
    assertFalse(queue.isEmpty());

    assertEquals(Integer.valueOf(20), queue.removeLast());
    assertEquals(0, queue.size());
    assertTrue(queue.isEmpty());
  }

  @Test
  public void testIterator() {
    Iterator<Integer> iterator = queue.iterator();
    assertFalse(iterator.hasNext());

    int i;
    for (i = 0; i < TEST_SIZE; i++) {
      queue.addFirst(i);
      assertEquals(i + 1, queue.size());

      iterator = queue.iterator();
      assertTrue(iterator.hasNext());
    }

    for (int n : queue) {
      assertEquals(--i, n);
    }

    for (i = 0; i < TEST_SIZE; i++) {
      queue.addLast(i);
      assertEquals(i + 1 + TEST_SIZE, queue.size());
    }

    assertEquals(TEST_SIZE * 2, queue.size());

    i = 0;
    for (int n : queue) {
      if (i < TEST_SIZE) {
        assertEquals(TEST_SIZE - i - 1, n);
      } else {
        assertEquals(i - TEST_SIZE, n);
      }
      i++;
    }
    assertEquals(TEST_SIZE * 2, i);

    while (i > 0) {
      queue.removeFirst();
      queue.removeLast();
      i -= 2;
    }

    assertEquals(0, queue.size());

    iterator = queue.iterator();
    assertFalse(iterator.hasNext());
  }
}
