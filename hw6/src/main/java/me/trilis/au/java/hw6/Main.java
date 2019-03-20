package me.trilis.au.java.hw6;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        for (int i = 100_000; i <= 1_000_000; i += 100_000) {
            testSpeed(i, 10);
        }
    }

    private static void testSpeed(int size, int numberOfRuns) {
        System.out.println("Size: " + size);
        var parallelSum = 0L;
        var nonParallelSum = 0L;
        for (int j = 0; j < numberOfRuns; j++) {
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
        parallelSum /= numberOfRuns;
        nonParallelSum /= numberOfRuns;
        System.out.println("AVERAGE PARALLEL TIME = " + (parallelSum / 1e9) + " s");
        System.out.println("AVERAGE NON PARALLEL TIME = " + (nonParallelSum / 1e9) + " s");
    }

    static List<Integer> generateList(int size) {
        Random random = new Random(size);
        var result = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            result.add(random.nextInt());
        }
        return result;
    }
}
