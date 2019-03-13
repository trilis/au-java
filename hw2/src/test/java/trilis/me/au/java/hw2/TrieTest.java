package trilis.me.au.java.hw2;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class TrieTest {

    @Test
    public void testAdd() {
        Random random = new Random(0);
        Trie trie = new Trie();
        var strings = new ArrayList<String>();
        for (int i = 0; i < 1000; i++) {
            StringBuilder string = new StringBuilder();
            for (int j = 0; j < 10; j++) {
                string.append('a' + random.nextInt(26));
            }
            trie.add(string.toString());
            strings.add(string.toString());
        }
        for (var string : strings) {
            assertTrue(trie.contains(string));
        }
    }

    @Test
    public void testRemove() {
        Trie trie = new Trie();
        for (int i = 0; i < 100; i++) {
            assertFalse(trie.remove(String.valueOf(i)));
            assertTrue(trie.add(String.valueOf(i)));
        }
        assertFalse(trie.contains(String.valueOf(100)));
        for (int i = 0; i < 100; i++) {
            assertTrue(trie.contains(String.valueOf(i)));
            assertTrue(trie.remove(String.valueOf(i)));
            assertFalse(trie.contains(String.valueOf(i)));
        }
    }

    @Test
    public void testHowManyStartWithPrefix() {
        Trie trie = new Trie();
        trie.add("abacaba");
        trie.add("aaaa");
        trie.add("abad");
        trie.add("abc");
        trie.add("b");
        trie.add("");
        assertEquals(0, trie.howManyStartWithPrefix("c"));
        assertEquals(1, trie.howManyStartWithPrefix("b"));
        assertEquals(6, trie.howManyStartWithPrefix(""));
        assertEquals(4, trie.howManyStartWithPrefix("a"));
        assertEquals(3, trie.howManyStartWithPrefix("ab"));
        assertEquals(2, trie.howManyStartWithPrefix("aba"));
        assertEquals(1, trie.howManyStartWithPrefix("aa"));
    }

    @Test
    public void testSize() {
        Trie trie = new Trie();
        for (int i = 0; i < 100; i++) {
            assertEquals(i, trie.size());
            trie.add(String.valueOf(i));
            trie.add(String.valueOf(i));
            assertEquals(i + 1, trie.size());
        }
        for (int i = 99; i >= 0; i--) {
            assertEquals(i + 1, trie.size());
            trie.remove(String.valueOf(i));
            trie.remove(String.valueOf(i));
            assertEquals(i, trie.size());
        }
    }

    @Test
    public void testSerializationOneString() throws IOException {
        var out = new ByteArrayOutputStream();
        Trie trie = new Trie();
        trie.add("abacaba");
        trie.serialize(out);
        var expectedOut = new ByteArrayOutputStream();
        var expectedData = new DataOutputStream(expectedOut);
        expectedData.writeBytes(
        "1 0 97 1 0 98 1 0 97 1 0 99 1 0 97 1 0 98 1 0 97 0 1 ");
        assertArrayEquals(expectedOut.toByteArray(), out.toByteArray());
    }

    @Test
    public void testSerializationManyStrings() throws IOException {
        var out = new ByteArrayOutputStream();
        Trie trie = new Trie();
        trie.add("abc");
        trie.add("abb");
        trie.add("bab");
        trie.add("bca");
        trie.serialize(out);
        var expectedOut = new ByteArrayOutputStream();
        var expectedData = new DataOutputStream(expectedOut);
        expectedData.writeBytes(
        "2 0 97 1 0 98 2 0 98 0 1 99 0 1 98 2 0 97 1 0 98 0 1 99 1 0 97 0 1 ");
        System.out.println(out.toString());
        assertArrayEquals(expectedOut.toByteArray(), out.toByteArray());
    }

    @Test
    public void testDeserialization() {
        var in = new ByteArrayInputStream(("2 1 120 2 1 120 1 0 120 0 1 121 2 0 97" +
                " 1 0 100 0 1 121 0 1 121 1 0 120 1 0 97 0 1 ").getBytes());
        Trie trie = new Trie();
        trie.deserialize(in);
        assertEquals(6, trie.size());
        assertTrue(trie.contains("xxx"));
        assertTrue(trie.contains("xyad"));
        assertTrue(trie.contains("x"));
        assertTrue(trie.contains("yxa"));
        assertTrue(trie.contains(""));
        assertTrue(trie.contains("xyy"));
    }

    @Test
    public void testSerializationDeserialization() throws IOException {
        Random random = new Random(0);
        Trie trie = new Trie();
        var strings = new ArrayList<String>();
        for (int i = 0; i < 1000; i++) {
            StringBuilder string = new StringBuilder();
            for (int j = 0; j < 10; j++) {
                string.append('a' + random.nextInt(26));
            }
            trie.add(string.toString());
            strings.add(string.toString());
        }
        var out = new ByteArrayOutputStream();
        trie.serialize(out);
        var in = new ByteArrayInputStream(out.toByteArray());
        trie.deserialize(in);
        for (var string : strings) {
            assertTrue(trie.contains(string));
        }
    }
}
