package me.trilis.au.java.hw3;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TreeSetTest {

    @Test
    void testAdd() {
        var set = new TreeSet<Integer>();
        for (int i = 1000; i >= 0; i--) {
            assertTrue(set.add(i));
        }
        for (int i = 1000; i >= 0; i--) {
            assertFalse(set.add(i));
        }
        int i = 0;
        for (int j : set) {
            assertEquals(Integer.valueOf(i++), Integer.valueOf(j));
        }
        i = 1000;
        for (var iterator = set.descendingIterator(); iterator.hasNext(); ) {
            assertEquals(Integer.valueOf(i--), iterator.next());
        }
    }

    @Test
    void testReturnValues() {
        var set = new TreeSet<Integer>();
        assertEquals(0, set.size());
        assertTrue(set.add(3));
        assertEquals(1, set.size());
        assertTrue(set.add(5));
        assertEquals(2, set.size());
        assertTrue(set.add(1));
        assertEquals(3, set.size());
        assertFalse(set.add(1));
        assertEquals(3, set.size());
        assertTrue(set.add(7));
        assertEquals(4, set.size());
        assertTrue(set.remove(5));
        assertFalse(set.remove(5));
    }

    @Test
    void testIterator() {
        var set = new TreeSet<Integer>();
        set.add(3);
        set.add(5);
        set.add(1);
        set.add(7);
        set.remove(3);
        var iterator = set.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals(Integer.valueOf(5), iterator.next());
        assertEquals(Integer.valueOf(7), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testFirstLast() {
        var set = new TreeSet<Integer>();
        assertThrows(NoSuchElementException.class, set::first);
        assertThrows(NoSuchElementException.class, set::last);
        set.add(3);
        assertEquals(Integer.valueOf(3), set.first());
        assertEquals(Integer.valueOf(3), set.last());
        set.add(5);
        assertEquals(Integer.valueOf(3), set.first());
        assertEquals(Integer.valueOf(5), set.last());
        set.add(1);
        assertEquals(Integer.valueOf(1), set.first());
        assertEquals(Integer.valueOf(5), set.last());
        set.add(7);
        assertEquals(Integer.valueOf(1), set.first());
        assertEquals(Integer.valueOf(7), set.last());
        set.remove(1);
        assertEquals(Integer.valueOf(3), set.first());
        assertEquals(Integer.valueOf(7), set.last());
        set.remove(7);
        assertEquals(Integer.valueOf(3), set.first());
        assertEquals(Integer.valueOf(5), set.last());
    }

    @Test
    void testLowerHigher() {
        var set = new TreeSet<Integer>();
        set.add(3);
        set.add(5);
        set.add(1);
        set.add(7);
        set.add(0);
        assertNull(set.lower(0));
        assertNull(set.higher(7));
        assertEquals(Integer.valueOf(0), set.lower(1));
        assertEquals(Integer.valueOf(1), set.lower(2));
        assertEquals(Integer.valueOf(1), set.lower(3));
        assertEquals(Integer.valueOf(3), set.lower(4));
        assertEquals(Integer.valueOf(3), set.lower(5));
        assertEquals(Integer.valueOf(5), set.lower(6));
        assertEquals(Integer.valueOf(5), set.lower(7));
        assertEquals(Integer.valueOf(7), set.lower(8));
        assertEquals(Integer.valueOf(1), set.higher(0));
        assertEquals(Integer.valueOf(3), set.higher(1));
        assertEquals(Integer.valueOf(3), set.higher(2));
        assertEquals(Integer.valueOf(5), set.higher(3));
        assertEquals(Integer.valueOf(5), set.higher(4));
        assertEquals(Integer.valueOf(7), set.higher(5));
        assertEquals(Integer.valueOf(7), set.higher(6));
    }

    @Test
    void testFloorCeiling() {
        var set = new TreeSet<Integer>();
        set.add(3);
        set.add(5);
        set.add(1);
        set.add(7);
        set.add(0);
        assertNull(set.floor(-1));
        assertNull(set.ceiling(8));
        assertEquals(Integer.valueOf(0), set.floor(0));
        assertEquals(Integer.valueOf(1), set.floor(1));
        assertEquals(Integer.valueOf(1), set.floor(2));
        assertEquals(Integer.valueOf(3), set.floor(3));
        assertEquals(Integer.valueOf(3), set.floor(4));
        assertEquals(Integer.valueOf(5), set.floor(5));
        assertEquals(Integer.valueOf(5), set.floor(6));
        assertEquals(Integer.valueOf(7), set.floor(7));
        assertEquals(Integer.valueOf(7), set.floor(8));
        assertEquals(Integer.valueOf(0), set.ceiling(0));
        assertEquals(Integer.valueOf(1), set.ceiling(1));
        assertEquals(Integer.valueOf(3), set.ceiling(2));
        assertEquals(Integer.valueOf(3), set.ceiling(3));
        assertEquals(Integer.valueOf(5), set.ceiling(4));
        assertEquals(Integer.valueOf(5), set.ceiling(5));
        assertEquals(Integer.valueOf(7), set.ceiling(6));
        assertEquals(Integer.valueOf(7), set.ceiling(7));
    }

    @Test
    void testComparator() {
        var set = new TreeSet<Integer>(Comparator.comparingInt(o -> o % 10));
        set.add(156);
        set.add(254);
        set.add(0);
        set.add(32);
        set.add(39);
        var iterator = set.iterator();
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(Integer.valueOf(32), iterator.next());
        assertEquals(Integer.valueOf(254), iterator.next());
        assertEquals(Integer.valueOf(156), iterator.next());
        assertEquals(Integer.valueOf(39), iterator.next());
    }

    @Test
    void testDescendingSet() {
        var set = new TreeSet<Integer>();
        set.add(3);
        set.add(5);
        set.add(1);
        set.descendingSet().add(7);
        set.descendingSet().add(0);
        var iterator = set.iterator();
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals(Integer.valueOf(3), iterator.next());
        assertEquals(Integer.valueOf(5), iterator.next());
        assertEquals(Integer.valueOf(7), iterator.next());
        assertFalse(iterator.hasNext());
        var descendingIterator = set.descendingSet().iterator();
        assertEquals(Integer.valueOf(7), descendingIterator.next());
        assertEquals(Integer.valueOf(5), descendingIterator.next());
        assertEquals(Integer.valueOf(3), descendingIterator.next());
        assertEquals(Integer.valueOf(1), descendingIterator.next());
        assertEquals(Integer.valueOf(0), descendingIterator.next());
        assertFalse(iterator.hasNext());
    }
}