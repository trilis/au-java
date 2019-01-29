package me.trilis.au.java.hw1;

import org.junit.Test;

import static org.junit.Assert.*;

public class HashTableTest {

    @Test
    public void get() {
        var hashTable = new HashTable();
        for (int i = 0; i < 100; i++) {
            assertNull(hashTable.get(String.valueOf(i)));
            hashTable.put(String.valueOf(i), String.valueOf(i * i));
            assertEquals(String.valueOf(i * i), hashTable.get(String.valueOf(i)));
        }
    }

    @Test
    public void contains() {
        var hashTable = new HashTable(100);
        for (int i = 0; i < 100; i++) {
            hashTable.put(String.valueOf(i), String.valueOf(i * i));
            assertTrue(hashTable.contains(String.valueOf(i)));
        }
        hashTable.clear();
        for (int i = 0; i < 100; i++) {
            assertFalse(hashTable.contains(String.valueOf(i)));
        }
    }

    @Test
    public void putTwice() {
        var hashTable = new HashTable();
        for (int i = 0; i < 100; i++) {
            hashTable.put(String.valueOf(i), String.valueOf(i * i));
            hashTable.put(String.valueOf(i), String.valueOf(i * i * 2));
            assertEquals(String.valueOf(i * i * 2), hashTable.get(String.valueOf(i)));
        }
    }

    @Test
    public void remove() {
        var hashTable = new HashTable();
        for (int i = 0; i < 100; i++) {
            hashTable.remove(String.valueOf(i));
            assertFalse(hashTable.contains(String.valueOf(i)));
            hashTable.put(String.valueOf(i), String.valueOf(i * i));
            assertTrue(hashTable.contains(String.valueOf(i)));
        }
        for (int i = 0; i < 100; i++) {
            hashTable.remove(String.valueOf(i));
            assertFalse(hashTable.contains(String.valueOf(i)));
        }
    }

    @Test
    public void size() {
        var hashTable = new HashTable();
        for (int i = 0; i < 100; i++) {
            assertEquals(i, hashTable.size());
            hashTable.put(String.valueOf(i), String.valueOf(i * i));
        }
    }

}
