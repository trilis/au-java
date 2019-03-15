package me.trilis.au.java.hw6;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** This class contains parallel and non parallel implementations of quick sort algorithm. */
public class QuickSort {

    /**
     * Sorts the specified list according to its natural order in parallel, using
     * number of threads equal to the number of available to JVM processors.
     */
    public static <T extends Comparable<? super T>> void parallelSort(List<T> list) {
        var threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        var latch = new CountDownLatch(list.size());
        threadPool.submit(new SortTask<>(0, list.size() - 1, list, latch, threadPool));
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            threadPool.shutdown();
        }
    }

    /** Sorts the specified list according to its natural order without parallelizing. */
    public static <T extends Comparable<? super T>> void nonParallelSort(List<T> list) {
        nonParallelSort(list, 0, list.size() - 1);
    }

    private static <T extends Comparable<? super T>> void nonParallelSort(
            List<T> list, int left, int right) {
        if (left < right) {
            var middle = partition(list, left, right);
            nonParallelSort(list, left, middle);
            nonParallelSort(list, middle + 1, right);
        }
    }

    private static <T extends Comparable<? super T>> int partition(
            List<T> list, int left, int right) {
        T pivot = list.get((left + right) / 2);
        int i = left;
        int j = right;
        while (i <= j) {
            while (list.get(i).compareTo(pivot) < 0) {
                i++;
            }
            while (list.get(j).compareTo(pivot) > 0) {
                j--;
            }
            if (i >= j) {
                break;
            }
            Collections.swap(list, i++, j--);
        }
        return j;
    }

    private static class SortTask<T extends Comparable<? super T>> implements Runnable {

        private final int left, right;
        private final List<T> list;
        private final ExecutorService threadPool;
        private final CountDownLatch latch;

        private SortTask(int left, int right, List<T> list,
                         CountDownLatch latch, ExecutorService threadPool) {
            this.left = left;
            this.right = right;
            this.list = list;
            this.latch = latch;
            this.threadPool = threadPool;
        }

        @Override
        public void run() {
            if (right - left >= 1000) {
                var middle = partition(list, left, right);
                threadPool.submit(new SortTask<>(left, middle, list, latch, threadPool));
                threadPool.submit(new SortTask<>(middle + 1, right, list, latch, threadPool));
            } else {
                nonParallelSort(list, left, right);
                for (int i = left; i <= right; i++) {
                    latch.countDown();
                }
            }
        }
    }

}
