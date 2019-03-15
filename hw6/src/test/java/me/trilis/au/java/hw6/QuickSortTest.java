package me.trilis.au.java.hw6;

import org.junit.jupiter.api.Test;

import java.util.*;

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

    private void testSpeed(int size) {
        System.out.println("Size: " + size);
        var parallelSum = 0L;
        var nonParallelSum = 0L;
        for (int j = 0; j < 10; j++) {
            var firstList = generateList(size);
            var secondList = new ArrayList<>(firstList);
            var parallelTime = System.nanoTime();
            QuickSort.parallelSort(firstList);
            parallelTime = System.nanoTime() - parallelTime;
            var nonParallelTime = System.nanoTime();
            QuickSort.nonParallelSort(secondList);
            nonParallelTime = System.nanoTime() - nonParallelTime;
            parallelSum += parallelTime;
            nonParallelSum += nonParallelTime;
        }
        System.out.println("AVERAGE PARALLEL TIME = " + (parallelSum / 1e9) + " s");
        System.out.println("AVERAGE NON PARALLEL TIME = " + (nonParallelSum / 1e9) + " s");
    }

    private List<Integer> generateList(int size) {
        Random random = new Random(size);
        var result = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            result.add(random.nextInt());
        }
        return result;
    }
}