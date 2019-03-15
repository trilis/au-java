package me.trilis.au.java.test1;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.Assert.*;

public class SmartListTest {

    @Test
    public void testSimple() {
        SmartList<Integer> list = newList();

        assertEquals(Collections.<Integer>emptyList(), list);

        list.add(1);
        assertEquals(Collections.singletonList(1), list);

        list.add(2);
        assertEquals(Arrays.asList(1, 2), list);
    }

    @Test
    public void testGetSet() {
        SmartList<Object> list = newList();

        list.add(1);

        assertEquals(1, list.get(0));
        assertEquals(1, list.set(0, 2));
        assertEquals(2, list.get(0));
        assertEquals(2, list.set(0, 1));

        list.add(2);

        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));

        assertEquals(1, list.set(0, 2));

        assertEquals(Arrays.asList(2, 2), list);
    }

    @Test
    public void testRemove() throws Exception {
        SmartList<Object> list = newList();

        list.add(1);
        list.remove(0);
        assertEquals(Collections.emptyList(), list);

        list.add(2);
        list.remove((Object) 2);
        assertEquals(Collections.emptyList(), list);

        list.add(1);
        list.add(2);
        assertEquals(Arrays.asList(1, 2), list);

        list.remove(0);
        assertEquals(Collections.singletonList(2), list);

        list.remove(0);
        assertEquals(Collections.emptyList(), list);
    }

    @Test
    public void testIteratorRemove() throws Exception {
        SmartList<Object> list = newList();
        assertFalse(list.iterator().hasNext());

        list.add(1);

        Iterator<Object> iterator = list.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());

        iterator.remove();
        assertFalse(iterator.hasNext());
        assertEquals(Collections.emptyList(), list);

        list.addAll(Arrays.asList(1, 2));

        iterator = list.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next());

        iterator.remove();
        assertTrue(iterator.hasNext());
        assertEquals(Collections.singletonList(2), list);
        assertEquals(2, iterator.next());

        iterator.remove();
        assertFalse(iterator.hasNext());
        assertEquals(Collections.emptyList(), list);
    }


    @Test
    public void testCollectionConstructor() throws Exception {
        assertEquals(Collections.emptyList(), newList(Collections.emptyList()));
        assertEquals(
                Collections.singletonList(1),
                new SmartList<>(Collections.singletonList(1)));

        assertEquals(
                Arrays.asList(1, 2),
                new SmartList<>(Arrays.asList(1, 2)));
    }

    @Test
    public void testAddManyElementsThenRemove() throws Exception {
        SmartList<Object> list = newList();
        for (int i = 0; i < 7; i++) {
            list.add(i + 1);
        }

        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7), list);

        for (int i = 0; i < 7; i++) {
            list.remove(list.size() - 1);
            assertEquals(6 - i, list.size());
        }

        assertEquals(Collections.emptyList(), list);
    }

    @Test
    public void testExceptions() {
        List<Object> list = newList();
        try {
            list.remove(0);
            fail();
        } catch (IndexOutOfBoundsException ignored) {
        }
        list.add(0);
        list.add(1);
        try {
            list.add(3, 2);
            fail();
        } catch (IndexOutOfBoundsException ignored) {
        }
        try {
            list.add(-1, 2);
            fail();
        } catch (IndexOutOfBoundsException ignored) {
        }
        try {
            list.set(3, 2);
            fail();
        } catch (IndexOutOfBoundsException ignored) {
        }
        try {
            list.get(3);
            fail();
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    private static <T> SmartList<T> newList() {
        try {
            return (SmartList<T>) getListClass().getConstructor().newInstance();
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> SmartList<T> newList(Collection<T> collection) {
        try {
            return (SmartList<T>) getListClass().getConstructor(Collection.class).newInstance(collection);
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> getListClass() throws ClassNotFoundException {
        return Class.forName("me.trilis.au.java.test1.SmartList");
    }
}