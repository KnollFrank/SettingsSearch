package de.KnollFrank.lib.settingssearch.common.task;

import android.os.AsyncTask;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;
import de.KnollFrank.lib.settingssearch.search.progress.OnUiThreadProgressDisplayer;

public class Tasks {

    public static <Params, Progress, Result> AsyncTask<Params, Progress, Result> executeTaskInParallelWithOtherTasks(
            final AsyncTask<Params, Progress, Result> task,
            final Params... params) {
        return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }

    // FK-TODO: replace all params here and elsewhere with AsyncTask<Params, Progress, Result>
    public static void asynchronouslyWaitForTask1ThenExecuteTask2(
            final Optional<? extends LongRunningTask<MergedPreferenceScreenData>> task1,
            final OnUiThreadProgressDisplayer progressDisplayer,
            final AsyncTask<Void, ?, ?> task2) {
        final AsyncTask<Void, Void, Void> asyncTask =
                new AsyncTask<>() {

                    @Override
                    protected Void doInBackground(final Void... voids) {
                        waitForTask1ThenExecuteTask2(task1, progressDisplayer, task2);
                        return null;
                    }
                };
        Tasks.executeTaskInParallelWithOtherTasks(asyncTask);
    }

    private static void waitForTask1ThenExecuteTask2(
            final Optional<? extends LongRunningTask<MergedPreferenceScreenData>> task1,
            final OnUiThreadProgressDisplayer progressDisplayer,
            final AsyncTask<Void, ?, ?> task2) {
        task1.ifPresentOrElse(
                _task1 -> {
                    waitForTaskWhileDisplayingItsProgress(_task1, progressDisplayer);
                    executeTaskInParallelWithOtherTasks(task2);
                },
                () -> executeTaskInParallelWithOtherTasks(task2));
    }

    private static void waitForTaskWhileDisplayingItsProgress(final LongRunningTask<MergedPreferenceScreenData> task,
                                                              final OnUiThreadProgressDisplayer progressDisplayer) {
        task.addProgressUpdateListener(progressDisplayer);
        waitFor(task);
        task.removeProgressUpdateListener(progressDisplayer);
    }

    private static void waitFor(final AsyncTask<?, ?, ?> task) {
        try {
            task.get();
        } catch (final ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
