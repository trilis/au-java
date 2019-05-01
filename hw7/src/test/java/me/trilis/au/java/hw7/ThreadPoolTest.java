package me.trilis.au.java.hw7;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ThreadPoolTest {

    @Test
    void testSimple() throws LightExecutionException, InterruptedException {
        int n = 10;
        var pool = new ThreadPool(n);
        var futures = new LightFuture[n];
        for (int i = 0; i < n; i++) {
            int j = i;
            futures[i] = pool.addTask(() -> {
                try {
                    Thread.sleep(20);
                    return j;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return -1;
                }
            });
        }
        for (int i = 0; i < n; i++) {
            assertEquals(i, futures[i].get());
        }
    }

    @Test
    void testThenApply() throws LightExecutionException, InterruptedException {
        int n = 10;
        var pool = new ThreadPool(n);
        var futures = new ArrayList<LightFuture<Integer>>(n);
        for (int i = 0; i < n; i++) {
            int j = i;
            futures.add(pool.addTask(() -> {
                try {
                    Thread.sleep(20);
                    return j;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return -1;
                }
            }));
        }
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < 2; k++) {
                futures.set(i, futures.get(i).thenApply((Integer j) -> {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return j * j;
                }));
            }
        }
        for (int i = 0; i < n; i++) {
            assertEquals(i * i * i * i, (int) futures.get(i).get());
        }
    }

    @Test
    void testNumberOfThreads() {
        testNumberOfThreads(1);
        testNumberOfThreads(10);
        testNumberOfThreads(100);
    }

    private void testNumberOfThreads(int n) {
        var previousCount = Thread.activeCount();
        var pool = new ThreadPool(n);
        assertEquals(n, Thread.activeCount() - previousCount);
        for (int i = 0; i < 10 * n; i++) {
            pool.addTask(() -> 0);
        }
        assertEquals(n, Thread.activeCount() - previousCount);
    }

    @Test
    void testNonPositiveThreadNumber() {
        assertThrows(IllegalArgumentException.class, () -> new ThreadPool(0));
        assertThrows(IllegalArgumentException.class, () -> new ThreadPool(-5));
    }

    @Test
    void testThrowingOnNulls() {
        ThreadPool pool = new ThreadPool(10);
        assertThrows(IllegalArgumentException.class, () -> pool.addTask(null));
        var future = pool.addTask(() -> 0);
        assertThrows(IllegalArgumentException.class, () -> future.thenApply(null));
    }

    @Test
    void testThrowingInsideTask() {
        ThreadPool pool = new ThreadPool(10);
        var future = pool.addTask(() -> {
            throw new IllegalStateException();
        });
        assertThrows(LightExecutionException.class, future::get);
    }
}