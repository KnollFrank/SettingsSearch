package de.KnollFrank.lib.settingssearch.search.progress;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;

public class ProgressDisplayer implements ProgressUpdateListener {

    private final View progressContainer;
    private final @IdRes int progressTextId;

    public ProgressDisplayer(final View progressContainer, final @IdRes int progressTextId) {
        this.progressContainer = progressContainer;
        this.progressTextId = progressTextId;
    }

    @Override
    public void onProgressUpdate(final String progress) {
        progressContainer.setVisibility(View.VISIBLE);
        // FK-TODO: do not use an "@IdRes int progressTextId" which could be any UI element, instead use a getter method which returns a TextView from progressContainer. Dito for the ids of SearchPreferenceFragmentLayout
        progressContainer
                .<TextView>findViewById(progressTextId)
                .setText(progress);
    }
}
