package me.trilis.au.java.hw1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LinkedListTest {

    @Test
    public void remove() {
        var list = new LinkedList<Integer>();
        list.add(2);
        list.add(3);
        assertEquals((Integer) 3, list.remove(1));
        assertEquals((Integer) 2, list.remove(0));
        try {
            list.remove(0);
            fail();
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test
    public void set() {
        var list = new LinkedList<Integer>();
        list.add(2);
        list.add(3);
        list.set(0, 5);
        list.set(1, 6);
        assertEquals((Integer) 5, list.get(0));
        assertEquals((Integer) 6, list.get(1));
        try {
            list.set(7, 7);
            fail();
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test
    public void add() {
        var list = new LinkedList<Integer>();
        list.add(2);
        list.add(3);
        list.add(0, 25);
        list.add(2, 4);
        assertEquals((Integer) 2, list.get(0));
        assertEquals((Integer) 25, list.get(1));
        assertEquals((Integer) 3, list.get(2));
        assertEquals((Integer) 4, list.get(3));
        try {
            list.add(4, 4);
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    @Test
    public void listIterator() {
        var list = new LinkedList<Integer>();
        list.add(2);
        list.add(3);
        list.add(5);
        var iter = list.listIterator();
        assertEquals(0, iter.nextIndex());
        assertEquals((Integer) 2, iter.next());
        assertEquals(1, iter.nextIndex());
        assertEquals((Integer) 3, iter.next());
        iter.remove();
        assertEquals(1, iter.nextIndex());
        assertEquals(0, iter.previousIndex());
        iter.add(10);
        assertEquals((Integer) 10, iter.next());
    }

    @Test
    public void size() {
        var list = new LinkedList<Integer>();
        assertEquals(0, list.size());
        list.add(2);
        assertEquals(1, list.size());
        list.add(3);
        assertEquals(2, list.size());
        list.add(0, 25);
        assertEquals(3, list.size());
        list.add(2, 4);
        assertEquals(4, list.size());
        list.remove(1);
        assertEquals(3, list.size());
        list.remove(2);
        assertEquals(2, list.size());
    }
}