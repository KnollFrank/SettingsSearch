package de.KnollFrank.lib.settingssearch.search.progress;

import android.view.View;

import de.KnollFrank.lib.settingssearch.common.task.OnUiThreadRunner;

public class ProgressDisplayerFactory {

    public static IProgressDisplayer createOnUiThreadProgressDisplayer(final View progressContainer,
                                                                       final OnUiThreadRunner onUiThreadRunner) {
        return new OnUiThreadProgressDisplayer(
                new ProgressDisplayer(progressContainer),
                onUiThreadRunner);
    }
}
