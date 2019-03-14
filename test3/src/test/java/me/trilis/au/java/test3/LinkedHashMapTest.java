package me.trilis.au.java.test3;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedHashMapTest {

    @Test
    void testOrder() {
        var map = new LinkedHashMap<String, String>();
        map.put("abc", "def");
        map.put("def", "ghi");
        map.put("", "abacaba");
        map.put("xxx", "yyy");
        map.put("abc", "ghi");
        map.remove("");
        var iterator = map.keySet().iterator();
        assertEquals("def", iterator.next());
        assertEquals("xxx", iterator.next());
        assertEquals("abc", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testContains() {
        var map = new LinkedHashMap<String, String>();
        map.put("abc", "def");
        assertTrue(map.containsKey("abc"));
        map.put("def", "ghi");
        assertTrue(map.containsKey("def"));
        map.put("", "abacaba");
        assertTrue(map.containsKey(""));
        map.put("xxx", "yyy");
        assertTrue(map.containsKey("xxx"));
        map.remove("");
        assertFalse(map.containsKey(""));
    }

    @Test
    void testMany() {
        var map = new LinkedHashMap<Integer, Integer>();
        for (int i = 0; i < 1000; i++) {
            map.put(i, 0);
        }
        var iterator = map.keySet().iterator();
        for (int i = 0; i < 1000; i++) {
            assertEquals(Integer.valueOf(i), iterator.next());
        }
        assertFalse(iterator.hasNext());
        for (int i = 999; i >= 0; i--) {
            map.put(i, 1);
        }
        iterator = map.keySet().iterator();
        for (int i = 999; i >= 0; i--) {
            assertEquals(Integer.valueOf(i), iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testReturnValues() {
        var map = new LinkedHashMap<String, String>();
        assertNull(map.put("abc", "def"));
        assertNull(map.put("def", "ghi"));
        assertNull(map.put("", "abacaba"));
        assertNull(map.put("xxx", "yyy"));
        assertEquals("def", map.put("abc", "ghi"));
        assertEquals("abacaba", map.remove(""));
    }
}