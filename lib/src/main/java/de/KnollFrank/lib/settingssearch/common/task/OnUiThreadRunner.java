package de.KnollFrank.lib.settingssearch.common.task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.function.Consumer;

public class OnUiThreadRunner {

    private final Consumer<Runnable> runNonBlockingOnUiThread;

    public OnUiThreadRunner(final Consumer<Runnable> runNonBlockingOnUiThread) {
        this.runNonBlockingOnUiThread = runNonBlockingOnUiThread;
    }

    public void runNonBlockingOnUiThread(final Runnable runnable) {
        runNonBlockingOnUiThread.accept(runnable);
    }

    public <V> V runBlockingOnUiThread(final Callable<V> callable) {
        final RunnableFuture<V> task = new FutureTask<>(callable);
        runNonBlockingOnUiThread.accept(task);
        return get(task);
    }

    private static <V> V get(final RunnableFuture<V> task) {
        try {
            return task.get();
        } catch (final ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
