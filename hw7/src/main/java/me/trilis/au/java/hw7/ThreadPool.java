package me.trilis.au.java.hw7;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.function.Function;
import java.util.function.Supplier;

/** Simple thread pool with fixed number of threads. */
public class ThreadPool {

    private final TaskQueue taskQueue = new TaskQueue();
    private final Thread[] threads;
    private volatile boolean isShutdown = false;

    /**
     * Creates thread pool and starts threads in it.
     * @param numberOfThreads number of threads in this thread pool.
     * @throws IllegalArgumentException if number of threads is not positive.
     */
    public ThreadPool(int numberOfThreads) {
        if (numberOfThreads <= 0) {
            throw new IllegalArgumentException("Non-positive number of threads");
        }
        threads = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(() -> {
                while (!isShutdown) {
                    try {
                        taskQueue.pop().compute();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        for (var thread : threads) {
            thread.start();
        }
    }

    /**
     * Shuts down this thread pool by interrupting all threads running at the moment.
     * All unfinished tasks will be abandoned, and no new tasks will be accepted.
     */
    public void shutdown() {
        isShutdown = true;
        for (var thread : threads) {
            thread.interrupt();
        }
    }

    /**
     * Adds new task to this thread pool
     * @param supplier the function to compute.
     * @param <T> returning type of the function to compute.
     * @throws IllegalStateException if this thread pool was shut down.
     * @return LightFuture object
     */
    public <T> LightFuture<T> addTask(@NotNull Supplier<T> supplier) {
        if (isShutdown) {
            throw new IllegalStateException("New tasks are not allowed after shutdown");
        }
        var task = new TaskFuture<>(supplier);
        taskQueue.push(task);
        return task;
    }

    /**
     * Implementation of LightFuture interface for this thread pool.
     * @param <T> type of the result.
     */
    public class TaskFuture<T> implements LightFuture<T> {

        private final Supplier<T> supplier;
        private T result;
        private volatile boolean isReady = false;
        private RuntimeException exception = null;
        private final ArrayDeque<TaskFuture> applyQueue = new ArrayDeque<>();

        /**
         * Creates new TaskFuture.
         * @param supplier the function to be computed as task.
         */
        public TaskFuture(@NotNull Supplier<T> supplier) {
            this.supplier = supplier;
        }

        /** {@inheritDoc} */
        @Override
        public boolean isReady() {
            return isReady;
        }

        /** {@inheritDoc} */
        @Override
        public T get() throws InterruptedException, LightExecutionException {
            synchronized (this) {
                while (!isReady) {
                    wait();
                }
                if (exception != null) {
                    throw new LightExecutionException(exception);
                }
                return result;
            }
        }

        /** {@inheritDoc} */
        @Override
        public <S> LightFuture<S> thenApply(@NotNull Function<? super T, ? extends S> function) {
            var task = new TaskFuture<S>(() -> {
                if (exception != null) {
                    throw exception;
                } else {
                    return function.apply(result);
                }
            });
            synchronized (applyQueue) {
                if (isReady) {
                    taskQueue.push(task);
                } else {
                    applyQueue.addFirst(task);
                }
            }
            return task;
        }

        private void compute() {
            try {
                result = supplier.get();
            } catch (RuntimeException e) {
                exception = e;
            }
            isReady = true;
            synchronized (this) {
                notifyAll();
            }
            synchronized (applyQueue) {
                for (var task : applyQueue) {
                    taskQueue.push(task);
                }
            }
        }
    }

    private class TaskQueue {

        private final ArrayDeque<TaskFuture<?>> queue = new ArrayDeque<>();

        private synchronized void push(@NotNull TaskFuture<?> task) {
            queue.addFirst(task);
            if (queue.size() == 1) {
                notifyAll();
            }
        }

        private synchronized TaskFuture<?> pop() throws InterruptedException {
            while (queue.size() == 0) {
                wait();
            }
            return queue.removeLast();
        }
    }
}
