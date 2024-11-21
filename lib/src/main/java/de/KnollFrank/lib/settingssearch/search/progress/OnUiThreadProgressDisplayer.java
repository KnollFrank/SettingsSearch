package de.KnollFrank.lib.settingssearch.search.progress;

import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;

class OnUiThreadProgressDisplayer implements IProgressDisplayer {

    private final IProgressDisplayer delegate;
    private final OnUiThreadRunner onUiThreadRunner;

    public OnUiThreadProgressDisplayer(final IProgressDisplayer delegate,
                                       final OnUiThreadRunner onUiThreadRunner) {
        this.delegate = delegate;
        this.onUiThreadRunner = onUiThreadRunner;
    }

    @Override
    public void displayProgress(final String progress) {
        onUiThreadRunner.runNonBlockingOnUiThread(() -> delegate.displayProgress(progress));
    }
}
