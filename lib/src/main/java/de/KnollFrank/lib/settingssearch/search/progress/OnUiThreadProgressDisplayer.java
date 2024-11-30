package de.KnollFrank.lib.settingssearch.search.progress;

import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;

public class OnUiThreadProgressDisplayer implements ProgressUpdateListener {

    private final ProgressUpdateListener delegate;
    private final OnUiThreadRunner onUiThreadRunner;

    public OnUiThreadProgressDisplayer(final ProgressUpdateListener delegate,
                                       final OnUiThreadRunner onUiThreadRunner) {
        this.delegate = delegate;
        this.onUiThreadRunner = onUiThreadRunner;
    }

    @Override
    public void onProgressUpdate(final String progress) {
        onUiThreadRunner.runNonBlockingOnUiThread(() -> delegate.onProgressUpdate(progress));
    }
}
