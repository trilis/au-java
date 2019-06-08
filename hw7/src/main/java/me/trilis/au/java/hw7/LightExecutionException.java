package me.trilis.au.java.hw7;

/**
 * LightFuture throws this exception in case if the task in it throws any exception.
 */
public class LightExecutionException extends Exception {

    /**
     * Creates new exception.
     * @param cause cause of this exception.
     */
    public LightExecutionException(Throwable cause) {
        initCause(cause);
    }
}
