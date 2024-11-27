package de.KnollFrank.lib.settingssearch.common.task;

import android.os.AsyncTask;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import de.KnollFrank.lib.settingssearch.MergedPreferenceScreen;
import de.KnollFrank.lib.settingssearch.db.preference.pojo.MergedPreferenceScreenData;

public class Tasks {

    // FK-TODO: replace all params here and elsewhere with AsyncTask<Params, Progress, Result>
    public static void executeAsynchronouslyTask1AndThenTask2(
            final Optional<LongRunningTask<MergedPreferenceScreenData>> task1,
            final LongRunningTaskWithProgressContainer<MergedPreferenceScreen> task2) {
        final LongRunningTask<Object> task =
                new LongRunningTask<>(
                        () -> {
                            executeTask1AndThenTask2(task1, task2);
                            return null;
                        },
                        _void -> {
                        });
        task.execute();
    }

    private static void executeTask1AndThenTask2(
            final Optional<LongRunningTask<MergedPreferenceScreenData>> task1,
            LongRunningTaskWithProgressContainer<MergedPreferenceScreen> task2) {
        task1.ifPresentOrElse(
                _task1 -> {
                    waitFor(_task1);
                    task2.execute();
                },
                task2::execute);
    }

    private static <Params, Progress, Result> void waitFor(final AsyncTask<Params, Progress, Result> task) {
        try {
            task.get();
        } catch (final ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
