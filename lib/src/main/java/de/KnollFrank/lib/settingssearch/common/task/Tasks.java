package de.KnollFrank.lib.settingssearch.common.task;

import android.os.AsyncTask;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;

public class Tasks {

    // FK-TODO: replace all params here and elsewhere with AsyncTask<Params, Progress, Result>
    public static void asynchronouslyWaitForTask1ThenExecuteTask2(
            final Optional<LongRunningTask<MergedPreferenceScreenData>> task1,
            final Runnable onWaitingForTask1,
            final LongRunningTaskWithProgressContainer<MergedPreferenceScreen> task2) {
        final LongRunningTask<Object> task =
                new LongRunningTask<>(
                        () -> {
                            waitForTask1ThenExecuteTask2(task1, onWaitingForTask1, task2);
                            return null;
                        },
                        _void -> {
                        });
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static void waitForTask1ThenExecuteTask2(
            final Optional<LongRunningTask<MergedPreferenceScreenData>> task1,
            final Runnable onWaitingForTask1,
            final LongRunningTaskWithProgressContainer<MergedPreferenceScreen> task2) {
        task1.ifPresentOrElse(
                _task1 -> {
                    onWaitingForTask1.run();
                    waitFor(_task1);
                    task2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                },
                () -> task2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR));
    }

    private static <Params, Progress, Result> void waitFor(final AsyncTask<Params, Progress, Result> task) {
        try {
            task.get();
        } catch (final ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
