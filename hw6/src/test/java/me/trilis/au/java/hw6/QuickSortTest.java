package me.trilis.au.java.hw6;

import org.junit.jupiter.api.Test;

import java.util.*;

import static me.trilis.au.java.hw6.Main.generateList;
import static org.junit.jupiter.api.Assertions.*;

class QuickSortTest {

    @Test
    void testNonParallel() {
        for (int i = 0; i <= 10; i++) {
            testNonParallel(i);
        }
        for (int i = 10; i <= 100000; i *= 2) {
            testNonParallel(i);
        }
    }

    private void testNonParallel(int size) {
        var list = generateList(size);
        var answer = new ArrayList<>(list);
        Collections.sort(answer);
        QuickSort.nonParallelSort(list);
        assertArrayEquals(answer.toArray(), list.toArray());
    }


    @Test
    void testParallel() {
        for (int i = 0; i <= 10; i++) {
            testParallel(i);
        }
        for (int i = 1000; i <= 100000; i *= 2) {
            testParallel(i);
        }
    }

    private void testParallel(int size) {
        var list = generateList(size);
        var answer = new ArrayList<>(list);
        Collections.sort(answer);
        QuickSort.parallelSort(list);
        assertArrayEquals(answer.toArray(), list.toArray());
    }

}