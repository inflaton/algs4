package com.inflaton.datastructures.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class RandomizedQueueTest {
  private static final int TEST_SIZE = 1000;
  private RandomizedQueue<Integer> queue;

  @BeforeEach
  public void setup() {
    queue = new RandomizedQueue<>();
  }

  @Test
  public void testDequeueOnEmpty() {
    assertThrows(
        NoSuchElementException.class,
        () -> {
          queue.dequeue();
        });
  }

  @Test
  public void testEnqueueOnNull() {
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          queue.enqueue(null);
        });
  }

  @Test
  public void testIsEmpty() {
    assertTrue(queue.isEmpty());
    queue.enqueue(1);
    assertFalse(queue.isEmpty());
    queue.dequeue();
    assertTrue(queue.isEmpty());
  }

  @Test
  public void testOps() {
    assertTrue(queue.isEmpty());
    assertEquals(0, queue.size());

    queue.enqueue(1);
    assertFalse(queue.isEmpty());
    assertEquals(1, queue.size());
    assertEquals(Integer.valueOf(1), queue.sample());

    queue.enqueue(2);
    assertFalse(queue.isEmpty());
    assertEquals(2, queue.size());

    queue.dequeue();
    assertEquals(1, queue.size());
    assertFalse(queue.isEmpty());

    queue.dequeue();
    assertEquals(0, queue.size());
    assertTrue(queue.isEmpty());
  }

  @Test
  public void testIterator() {
    Iterator<Integer> iterator = queue.iterator();
    assertFalse(iterator.hasNext());

    int i;
    for (i = 0; i < TEST_SIZE; i++) {
      queue.enqueue(i);
      assertEquals(i + 1, queue.size());

      iterator = queue.iterator();
      assertTrue(iterator.hasNext());
    }

    assertEquals(TEST_SIZE, queue.size());

    for (int n : queue) {
      assertTrue(n < TEST_SIZE);
    }

    for (i = 0; i < TEST_SIZE; i++) {
      queue.enqueue(i);
      assertEquals(i + 1 + TEST_SIZE, queue.size());
    }

    assertEquals(TEST_SIZE * 2, queue.size());

    i = 0;
    for (int n : queue) {
      assertTrue(n < TEST_SIZE);
      i++;
    }
    assertEquals(TEST_SIZE * 2, i);

    while (i > 0) {
      queue.dequeue();
      queue.dequeue();
      i -= 2;
    }

    assertEquals(0, queue.size());

    iterator = queue.iterator();
    assertFalse(iterator.hasNext());
  }
}
