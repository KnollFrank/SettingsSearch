package de.KnollFrank.lib.settingssearch.search.progress;

import android.view.View;

import de.KnollFrank.lib.settingssearch.search.ui.ProgressContainerUI;

public class ProgressDisplayerFactory {

    public static ProgressDisplayer createProgressDisplayer(final View rootView,
                                                            final ProgressContainerUI progressContainerUI) {
        final View progressContainer = progressContainerUI.getProgressContainer(rootView);
        return new ProgressDisplayer(
                progressContainer,
                progressContainerUI.getProgressText(progressContainer));
    }
}
