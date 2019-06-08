package me.trilis.au.java.hw7;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Interface representing task that will be completed later or the
 * result of the completed task.
 * @param <T> type of the result.
 */
public interface LightFuture<T> {
    /**
     * Checks if task is already completed.
     * @return true if task is completed and false otherwise.
     */
    boolean isReady();

    /**
     * If task is already completed, returns its result. Otherwise waits for
     * its completion and then returns result.
     * @return result of the task.
     * @throws InterruptedException if thread in which
     * this task is running is interrupted.
     * @throws LightExecutionException if this task throws any exception.
     */
    T get() throws InterruptedException, LightExecutionException;

    /**
     * Creates new LightFuture with the following task: first it should complete current task.
     * then apply the given function to its result.
     * @param function the given function.
     * @param <S> returning type of this function.
     * @return new LightFuture.
     */
    <S> LightFuture<S> thenApply(@NotNull Function<? super T, ? extends S> function);
}
