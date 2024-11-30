package de.KnollFrank.lib.settingssearch.common.task;

import android.os.AsyncTask;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import de.KnollFrank.lib.settingssearch.search.progress.ProgressUpdateListener;

public class Tasks {

    public static <Params, Progress, Result> AsyncTask<Params, Progress, Result> executeTaskInParallelWithOtherTasks(
            final AsyncTask<Params, Progress, Result> task,
            final Params... params) {
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }

    public static void asynchronouslyWaitForTask1ThenExecuteTask2(
            final Optional<? extends AsyncTaskWithProgressUpdateListeners<?>> task1,
            final ProgressUpdateListener progressUpdateListener4Task1,
            final AsyncTask<Void, ?, ?> task2) {
        final AsyncTask<Void, Void, Void> asyncTask =
                new AsyncTask<>() {

                    @Override
                    protected Void doInBackground(final Void... voids) {
                        waitForTask1ThenExecuteTask2(task1, progressUpdateListener4Task1, task2);
                        return null;
                    }
                };
        executeTaskInParallelWithOtherTasks(asyncTask);
    }

    private static void waitForTask1ThenExecuteTask2(
            final Optional<? extends AsyncTaskWithProgressUpdateListeners<?>> task1,
            final ProgressUpdateListener progressUpdateListener4Task1,
            final AsyncTask<Void, ?, ?> task2) {
        task1.ifPresentOrElse(
                _task1 -> {
                    waitForTaskWhileListeningToItsProgress(_task1, progressUpdateListener4Task1);
                    executeTaskInParallelWithOtherTasks(task2);
                },
                () -> executeTaskInParallelWithOtherTasks(task2));
    }

    private static void waitForTaskWhileListeningToItsProgress(final AsyncTaskWithProgressUpdateListeners<?> task,
                                                               final ProgressUpdateListener progressUpdateListener) {
        task.addProgressUpdateListener(progressUpdateListener);
        waitFor(task);
        task.removeProgressUpdateListener(progressUpdateListener);
    }

    private static void waitFor(final AsyncTask<?, ?, ?> task) {
        try {
            task.get();
        } catch (final ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
