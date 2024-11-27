package de.KnollFrank.lib.settingssearch.common.task;

import android.view.View;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class Tasks {

    public static <V> void execute(final Callable<V> doInBackground,
                                   final Consumer<V> onPostExecute,
                                   final View progressContainer) {
        final LongRunningTaskWithProgressContainer<V> task =
                new LongRunningTaskWithProgressContainer<>(
                        doInBackground,
                        onPostExecute,
                        progressContainer);
        task.execute();
    }
}
